"""Export static cubes/meshes from a bbmodel into the mod's equipment mesh format.

Python 3 standard library only. Unsupported features fail instead of silently disappearing.
The source file is never modified. Invoke from the project root; see docs/ADDING_ITEMS.md.
"""
import argparse
import base64
from collections import defaultdict
import hashlib
import json
import math
from pathlib import Path


def rotate(point, origin, angles):
    x, y, z = (point[i] - origin[i] for i in range(3))
    a, b, c = (math.radians(v) for v in angles)
    y, z = y * math.cos(a) - z * math.sin(a), y * math.sin(a) + z * math.cos(a)
    x, z = x * math.cos(b) + z * math.sin(b), -x * math.sin(b) + z * math.cos(b)
    x, y = x * math.cos(c) - y * math.sin(c), x * math.sin(c) + y * math.cos(c)
    return [x + origin[0], y + origin[1], z + origin[2]]


def export(source, resources, namespace, item_id, scale, offset):
    raw = source.read_bytes()
    model = json.loads(raw)
    if model.get('animations') or model.get('box_uv') or model['meta'].get('box_uv'):
        raise ValueError('Only static per-face-UV models are supported')
    groups = {g['uuid']: g for g in model.get('groups', [])}
    parents = {}

    def walk(nodes, chain):
        for node in nodes:
            if isinstance(node, str):
                parents[node] = chain
            else:
                group = groups.get(node['uuid'], node)
                if group.get('visibility', True) and group.get('export', True):
                    walk(node.get('children', []), chain + [group])
    walk(model['outliner'], [])

    textures = model['textures']
    batches = defaultdict(list)
    used_textures = set()
    counts = {'cubes': 0, 'meshes': 0, 'quads': 0, 'triangles': 0}
    elements = []
    for element in model['elements']:
        if element['uuid'] not in parents or not element.get('export', True) or not element.get('visibility', True):
            continue
        if element.get('box_uv') or element.get('inflate', 0):
            raise ValueError('Box UV/inflated elements need explicit conversion first')
        elements.append(element)
    # Determine the actual world-space lower edge after all saved group rotations.
    def world(point, element):
        p = rotate(point, element.get('origin', [0, 0, 0]), element.get('rotation', [0, 0, 0]))
        for group in reversed(parents[element['uuid']]):
            p = rotate(p, group.get('origin', [0, 0, 0]), group.get('rotation', [0, 0, 0]))
        return p

    all_points = []
    for e in elements:
        if e['type'] == 'cube':
            all_points.extend(world([x, y, z], e) for x in (e['from'][0], e['to'][0])
                              for y in (e['from'][1], e['to'][1]) for z in (e['from'][2], e['to'][2]))
        elif e['type'] == 'mesh':
            all_points.extend(world(p, e) for p in e['vertices'].values())
        else:
            raise ValueError('Unsupported element type: ' + e['type'])
    bottom = min(p[1] for p in all_points)

    def position(point, e):
        p = world(point, e)
        return [(p[0] * scale[0] + offset[0]) / 16,
                (-(p[1] - bottom) * scale[1] + offset[1]) / 16,
                (p[2] * scale[2] + offset[2]) / 16]

    for e in elements:
        is_cube = e['type'] == 'cube'
        counts['cubes' if is_cube else 'meshes'] += 1
        if is_cube:
            x1, y1, z1 = e['from']
            x2, y2, z2 = e['to']
            face_points = {
                'north': [(x2,y2,z1),(x1,y2,z1),(x1,y1,z1),(x2,y1,z1)],
                'south': [(x1,y2,z2),(x2,y2,z2),(x2,y1,z2),(x1,y1,z2)],
                'east': [(x2,y2,z2),(x2,y2,z1),(x2,y1,z1),(x2,y1,z2)],
                'west': [(x1,y2,z1),(x1,y2,z2),(x1,y1,z2),(x1,y1,z1)],
                'up': [(x1,y2,z2),(x2,y2,z2),(x2,y2,z1),(x1,y2,z1)],
                'down': [(x1,y1,z1),(x2,y1,z1),(x2,y1,z2),(x1,y1,z2)],
            }
            center = position([(x1+x2)/2, (y1+y2)/2, (z1+z2)/2], e)
        for face_name, face in e['faces'].items():
            ref = face.get('texture')
            if ref is None or ref is False:
                continue
            if isinstance(ref, int):
                texture_index = ref
            else:
                texture_index = next(i for i, t in enumerate(textures) if t['uuid'] == ref)
            tex = textures[texture_index]
            uw, vh = tex.get('uv_width', 16), tex.get('uv_height', 16)
            if is_cube:
                points = [position(p, e) for p in face_points[face_name]]
                u1, v1, u2, v2 = face['uv']
                uvs = [[u2,v1],[u1,v1],[u1,v2],[u2,v2]]
                rotation = face.get('rotation', 0)
                if rotation not in (0,90,180,270):
                    raise ValueError('Unsupported face UV rotation')
                steps = rotation // 90
                uvs = uvs[steps:] + uvs[:steps]
            else:
                keys = face['vertices']
                if len(keys) not in (3,4):
                    raise ValueError('Only triangles and quads are supported')
                points = [position(e['vertices'][k], e) for k in keys]
                uvs = [face['uv'][k] for k in keys]
                center = [0, sum(p[1] for p in points)/len(points), 0]
            a, b = ([points[1][i]-points[0][i] for i in range(3)],
                    [points[2][i]-points[0][i] for i in range(3)])
            n = [a[1]*b[2]-a[2]*b[1], a[2]*b[0]-a[0]*b[2], a[0]*b[1]-a[1]*b[0]]
            length = math.sqrt(sum(v*v for v in n))
            if length < 1e-12:
                raise ValueError('Degenerate face in ' + e['name'])
            n = [v/length for v in n]
            mid = [sum(p[i] for p in points)/len(points) for i in range(3)]
            if sum(n[i]*(mid[i]-center[i]) for i in range(3)) < 0:
                n = [-v for v in n]
                points.reverse()
                uvs.reverse()
            counts['triangles' if len(points)==3 else 'quads'] += 1
            vertices = [[round(v, 7) for v in p + [uv[0]/uw, uv[1]/vh] + n]
                        for p, uv in zip(points, uvs)]
            if len(vertices)==3:
                vertices.append(vertices[-1][:])
            batches[texture_index].append(vertices)
            used_textures.add(texture_index)

    asset = resources / 'assets' / namespace
    packed = []
    for i in sorted(used_textures):
        tex_path = f'textures/entity/equipment/{item_id}/material_{i}.png'
        dest = asset / tex_path
        dest.parent.mkdir(parents=True, exist_ok=True)
        source_data = textures[i].get('source', '')
        if not source_data.startswith('data:image/png;base64,'):
            raise ValueError('Textures must be embedded PNGs')
        dest.write_bytes(base64.b64decode(source_data.split(',',1)[1]))
        packed.append({'texture': f'{namespace}:{tex_path}', 'quads': batches[i]})
    result = {'format_version': 1, 'source_sha256': hashlib.sha256(raw).hexdigest(),
              'fit': {'scale':scale, 'offset':offset, 'source_bottom':bottom},
              'statistics': counts, 'batches': packed}
    target = asset / 'models' / 'equipment' / f'{item_id}.json'
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_text(json.dumps(result, separators=(',',':'))+'\n', encoding='utf-8')
    print(json.dumps({'model':str(target), 'textures':len(packed), **counts}))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('source', type=Path)
    parser.add_argument('--resources', type=Path, default=Path('src/main/resources'))
    parser.add_argument('--namespace', default='mushokutenseiorigin')
    parser.add_argument('--id', required=True)
    parser.add_argument('--scale', type=float, nargs=3, default=[1.08,1.0,2.08])
    parser.add_argument('--offset', type=float, nargs=3, default=[0,-4.1,0])
    args = parser.parse_args()
    export(args.source, args.resources, args.namespace, args.id, args.scale, args.offset)
