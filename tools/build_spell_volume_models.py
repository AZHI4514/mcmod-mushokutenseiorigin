"""Author closed spell meshes through Blockbench MCP, then export its actual geometry.

The mod uses these exports directly, not handwritten runtime replacement models.
One small animated material per spell; no copied Iron's textures or code.
"""
from pathlib import Path
import sys,json,math,base64,hashlib
import numpy as np
from PIL import Image
sys.path.insert(0,'D:/Skill/skills/minecraft-modeling-item/scripts')
from bb_client import Client
ROOT=Path(__file__).parent
OUT=Path(sys.argv[sys.argv.index('--output')+1]) if '--output' in sys.argv else ROOT/'generated'
OUT.mkdir(exist_ok=True)
SPECS=json.loads(Path('D:/LibreSprite-art-source/magic_effects_39/manifest.json').read_text(encoding='utf-8'))
TAU=math.tau
def mesh(name,v,f):
 volume=0
 for face in f:
  for i in range(1,len(face)-1):
   volume+=float(np.dot(v[face[0]],np.cross(v[face[i]],v[face[i+1]])))/6
 if volume<0:f=[face[::-1] for face in f]
 return dict(name=name,vertices=[[round(t*16,6) for t in p] for p in v],faces=f)
def sphere(name,scale=(1,1,1),at=(0,0,0),rough=0):
 n,m=12,6;v=[];f=[]
 for j in range(m+1):
  lat=-math.pi/2+j*math.pi/m
  for i in range(n):
   a=i*TAU/n;r=.5*(1+rough*math.sin(i*5.1+j*7.3))
   v.append([at[0]+math.cos(a)*math.cos(lat)*r*scale[0],at[1]+math.sin(lat)*r*scale[1],at[2]+math.sin(a)*math.cos(lat)*r*scale[2]])
 for j in range(m):
  for i in range(n):
   a=j*n+i;b=j*n+(i+1)%n;c=b+n;d=a+n
   f.append([a,b,c,d] if j not in (0,m-1) else [a,c,d] if j==0 else [a,b,c])
 return mesh(name,v,f)
def tube(name,points,radii,sides=5):
 v=[];f=[]
 for j,p in enumerate(points):
  tangent=np.array(points[min(j+1,len(points)-1)])-np.array(points[max(j-1,0)])
  tangent=tangent/max(np.linalg.norm(tangent),1e-6)
  normal=np.cross(tangent,[0,1,0] if abs(tangent[1])<.9 else [1,0,0]);normal/=max(np.linalg.norm(normal),1e-6)
  cross=np.cross(tangent,normal)
  for k in range(sides):
   q=np.array(p)+radii[j]*(normal*math.cos(k*TAU/sides)+cross*math.sin(k*TAU/sides));v.append(q.tolist())
 for j in range(len(points)-1):
  for k in range(sides):f.append([j*sides+k,j*sides+(k+1)%sides,(j+1)*sides+(k+1)%sides,(j+1)*sides+k])
 for end,rev in [(0,True),(len(points)-1,False)]:
  idx=len(v);v.append(points[end])
  for k in range(sides):
   tri=[idx,end*sides+k,end*sides+(k+1)%sides];f.append(tri[::-1] if rev else tri)
 return mesh(name,v,f)
def ring(name,r=.4,minor=.035,y=0,tilt=0,start=0,arc=TAU):
 pts=[];n=24
 for i in range(n+1):
  a=start+i*arc/n;x=r*math.cos(a);z=r*math.sin(a)
  pts.append([x,y+z*math.sin(tilt),z*math.cos(tilt)])
 return tube(name,pts,[minor]*(n+1),4)
def spike(name,at=(0,0,0),scale=(.14,.5,.14),axis='y'):
 v=[[0,-.5,0],[.5,-.22,0],[0,-.22,.5],[-.5,-.22,0],[0,-.22,-.5],[0,.5,0]]
 if axis=='z':v=[[x,z,y] for x,y,z in v]
 v=[[at[k]+p[k]*scale[k] for k in range(3)] for p in v]
 return mesh(name,v,[[0,2,1],[0,3,2],[0,4,3],[0,1,4],[5,1,2],[5,2,3],[5,3,4],[5,4,1]])
def slab(name,bulge=.06):
 # Closed curved wall, including rim. Actual thickness in every view.
 n=8;v=[];f=[]
 for side in (-1,1):
  for y in (-.5,.5):
   for i in range(n+1):
    x=i/n-.5;v.append([x,y,side*.07+bulge*(1-4*x*x)])
 stride=n+1
 for i in range(n):
  f.extend([[i,i+1,stride+i+1,stride+i],[2*stride+i,3*stride+i,3*stride+i+1,2*stride+i+1],
            [i,2*stride+i,2*stride+i+1,i+1],[stride+i,stride+i+1,3*stride+i+1,3*stride+i]])
 f.extend([[0,stride,3*stride,2*stride],[n,2*stride+n,3*stride+n,stride+n]])
 return mesh(name,v,f)
def blade(name):
 # Curved blade with a central ridge and tapered, closed sharp edge.
 n=18;v=[];f=[]
 for i in range(n+1):
  x=i/n-.5;curve=.15*(1-4*x*x);thick=.09*math.sin(math.pi*i/n)+.006
  v.extend([[x,curve-thick,0],[x,curve,.5*math.sin(math.pi*i/n)+.015],[x,curve+thick,0],[x,curve,-.5*math.sin(math.pi*i/n)-.015]])
 for i in range(n):
  for k in range(4):f.append([i*4+k,(i+1)*4+k,(i+1)*4+(k+1)%4,i*4+(k+1)%4])
 f.extend([[3,2,1,0],[n*4,n*4+1,n*4+2,n*4+3]])
 return mesh(name,v,f)
def build(spec):
 sid=spec['spell'];shape=spec['shape'];family=spec['family'];parts=[]
 if sid in ('water_shield','physical_shield','ice_fortress','waterfall'):
  parts=[slab('core')]
  if sid!='waterfall':
   for x in (-.46,.46):parts.append(tube('rim',[[x,-.5,0],[x,.5,0]],[.018,.018],4))
 elif sid=='magic_shield':
  parts=[sphere('shell',(1,1,1)),ring('flow',.48,.016,tilt=.5),ring('flow',.48,.016,tilt=-.5)]
 elif sid in ('heat_island','burning_place'):
  parts=[ring('core',.46,.025)]
  for i in range(8 if sid=='heat_island' else 12):
   a=i*TAU/(8 if sid=='heat_island' else 12);r=.45
   pts=[[r*math.cos(a),-.1,r*math.sin(a)],[(r-.02)*math.cos(a+.1),.1,(r-.02)*math.sin(a+.1)],[(r-.05)*math.cos(a+.2),.4,(r-.05)*math.sin(a+.2)]]
   parts.append(tube('flame' if sid=='burning_place' else 'steam',pts,[.04,.025,.003],4))
 elif sid in ('healing','ex_healing','detoxification','antidote_ii'):
  parts=[ring('flow',.4,.018,y=-.3,tilt=.2),ring('flow',.35,.018,y=.05,tilt=-.3)]
  for i in range(6):
   a=i*TAU/6;parts.append(spike('spark',(math.cos(a)*.3,.2,math.sin(a)*.3),(.035,.12,.035)))
 elif sid in ('water_splash','icicle_field','explosion','wind_blast','heat_hand','flame_pillar','wind_bind','flamethrower'):
  if sid in ('water_splash','icicle_field','explosion'):
   parts=[ring('shock',.46,.025),sphere('shell',(.8,.35,.8))]
   for i in range(6):
    a=i*TAU/6;parts.append(spike('spark',(.35*math.cos(a),0,.35*math.sin(a)),(.08,.45,.08)))
  elif sid=='wind_bind':
   for j in range(2):
    pts=[[.4*math.cos(i*TAU/24+j*math.pi),i/24-.5,.4*math.sin(i*TAU/24+j*math.pi)] for i in range(25)]
    parts.append(tube('flow',pts,[.02]*25,4))
  else:
   for j in range(5):
    a=j*TAU/5;parts.append(tube('flame' if family=='fire' else 'flow',[[.2*math.cos(a),.2*math.sin(a),-.5],[.1*math.cos(a+.5),.1*math.sin(a+.5),0],[0,0,.5]],[.065,.04,.003],5))
 elif shape in ('spear','crystal'):
  vertical=sid in ('ice_pillar','ice_lance','earth_lance')
  parts=[spike('core',scale=(.5,1,.5) if vertical else (.5,.5,1),axis='y' if vertical else 'z')]
  for i in range(4):
   a=i*TAU/4
   parts.append(spike('shard',(.27*math.cos(a),-.15 if vertical else .27*math.sin(a),.27*math.sin(a) if vertical else -.2),(.2,.55,.2) if vertical else (.2,.2,.65),axis='y' if vertical else 'z'))
 elif shape=='crescent':
  parts=[blade('core')]
  for i in range(2):parts.append(tube('wake',[[-.45,.04,-.15-i*.18],[0,.17,-.25-i*.18],[.45,.04,-.15-i*.18]],[.008,.024,.008],4))
 elif sid=='flood_flush':
  parts=[tube('core',[[-.5,.05,0],[-.25,.2,.1],[0,.26,.14],[.25,.2,.1],[.5,.05,0]],[.12,.16,.18,.16,.12],8)]
 elif shape=='rock':
  parts=[sphere('core',rough=.12)]
  if sid!='earth_pillar':parts+=[ring('flow',.51,.015,tilt=.6)]
 elif family=='wind':
  parts=[sphere('core',(.5,.5,.65))]
  for j in range(2):
   pts=[[.4*math.cos(i*TAU/20+j*math.pi),.4*math.sin(i*TAU/20+j*math.pi),i/20-.5] for i in range(21)]
   parts.append(tube('flow',pts,[.025]*21,4))
 else:
  parts=[sphere('core',(.78,.78,.78))]
  for j in range(3 if family=='fire' else 2):
   pts=[]
   for i in range(17):
    a=i*TAU/16+j*TAU/3;r=.38-i*.009
    pts.append([r*math.cos(a),r*math.sin(a),.2-i*.055])
   parts.append(tube('flame' if family=='fire' else 'flow',pts,[.04*(1-i/19) for i in range(17)],4))
 return parts
def surface(spec):
 src=Path('D:/LibreSprite-art-source/magic_effects_39/assets/mushokutenseiorigin/textures/effect/spells')/(spec['spell']+'.png')
 im=np.asarray(Image.open(src)).copy();y,x=np.mgrid[:64,:64]
 for f in range(24):
  tile=im[f*64:(f+1)*64]
  # Closed geometry supplies the silhouette: surface alpha must not cut holes into it.
  tile[:,:,3]=255
  flow=.83+.17*np.sin(x*.2+y*.27-f*TAU/24)
  tile[:,:,:3]=(tile[:,:,:3]*flow[:,:,None]).astype('uint8')
 path=OUT/'textures'/(spec['spell']+'.png');path.parent.mkdir(exist_ok=True)
 Image.fromarray(im).save(path)
 return path
def unpack(r):
 if r.get('isError'):raise RuntimeError(str(r)[:2000])
 return r.get('structuredContent')
def compile_runtime(model):
 result=[]
 for e in model['elements']:
  if e['type']!='mesh':continue
  verts=e['vertices'];quads=[]
  for face in e['faces'].values():
   keys=face['vertices'];p=[verts[k] for k in keys]
   normal=np.cross(np.array(p[1])-p[0],np.array(p[2])-p[0])
   if np.linalg.norm(normal)<1e-6:continue
   uv=face['uv'];arr=[[*[round(t/16,6) for t in verts[k]],*[round(t/64,6) for t in uv[k]]] for k in keys]
   if len(arr)==3:arr.append(arr[-1])
   quads.append(arr)
  result.append(dict(name=e['name'],quads=quads))
 return dict(parts=result)
def main():
 c=Client(timeout=60)
 log=[]
 for index,spec in enumerate(SPECS):
  sid=spec['spell'];folder=OUT/sid;folder.mkdir(exist_ok=True)
  if (folder/'runtime.json').exists() and '--force' not in sys.argv:
   model=json.loads((folder/'runtime.json').read_text(encoding='utf-8'))
   log.append(dict(spell=sid,meshes=len(model['parts']),faces=sum(len(p['quads']) for p in model['parts']),source='Blockbench MCP export',reference=spec['reference']))
   continue
  parts=build(spec);faces=sum(len(p['faces']) for p in parts)
  assert faces<650,(sid,faces)
  unpack(c.tool('create_project',{'name':'magic_animate_effect_'+sid,'format':'free'}))
  c.tool('add_group',{'name':'effect','origin':[0,0,0]})
  png=surface(spec)
  data='data:image/png;base64,'+base64.b64encode(Image.open(png).crop((0,0,64,64))._repr_png_()).decode()
  unpack(c.tool('create_texture',{'name':sid+'_surface','width':64,'height':64,'data':data,'render_mode':'emissive','render_sides':'front'}))
  unpack(c.tool('place_mesh',{'elements':parts,'group':'effect','texture':sid+'_surface'}))
  # Native mesh API does not expose bulk cylindrical projection. Keep undo scoped to UV edits only.
  code="Undo.initEdit({elements:Mesh.all,uv_only:true});for(const m of Mesh.all){for(const f of Object.values(m.faces)){const vs=f.vertices.map(k=>m.vertices[k]);const a=new THREE.Vector3(...vs[1]).sub(new THREE.Vector3(...vs[0]));const b=new THREE.Vector3(...vs[2]).sub(new THREE.Vector3(...vs[0]));const n=a.cross(b);const axis=Math.abs(n.y)>Math.abs(n.x)&&Math.abs(n.y)>Math.abs(n.z)?1:Math.abs(n.z)>Math.abs(n.x)?2:0;for(const k of f.vertices){const p=m.vertices[k];f.uv[k]=axis===1?[(p[0]/16+.5)*64,(p[2]/16+.5)*64]:axis===2?[(p[0]/16+.5)*64,(.5-p[1]/16)*64]:[(p[2]/16+.5)*64,(.5-p[1]/16)*64];}}}Undo.finishEdit('Project spell surface UV');Canvas.updateAll();return {meshes:Mesh.all.length,faces:Mesh.all.reduce((n,m)=>n+Object.keys(m.faces).length,0)};"
  unpack(c.tool('risky_eval',{'code':'(()=>{'+code+'})()'}))
  c.tool('create_animation',{'name':sid+'_pulse','loop':True,'animation_length':2,'bones':{'effect':[{'time':0,'scale':[1,1,1]},{'time':1,'scale':[1.035,.98,1.035]},{'time':2,'scale':[1,1,1]}]}})
  r=c.tool('export_model',{'codec_id':'project','max_content_length':2000000})
  obj=unpack(r)
  if not obj:
   obj=json.loads(next(x['text'] for x in r['content'] if x['type']=='text'))
  content=obj.get('content')
  if content is None:raise RuntimeError(str(obj)[:1000])
  model=json.loads(content) if isinstance(content,str) else content
  (folder/(sid+'.bbmodel')).write_text(json.dumps(model,ensure_ascii=False),encoding='utf-8')
  runtime=compile_runtime(model)
  (folder/'runtime.json').write_text(json.dumps(runtime,separators=(',',':')),encoding='utf-8')
  log.append(dict(spell=sid,meshes=len(parts),faces=faces,source='Blockbench MCP export',reference=spec['reference']))
  print(index+1,sid,len(parts),faces,flush=True)
 (OUT/'audit.json').write_text(json.dumps(log,indent=2),encoding='utf-8');c.close()
if __name__=='__main__':main()
