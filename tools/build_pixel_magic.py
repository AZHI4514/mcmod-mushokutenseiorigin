"""Build 39 compact, pixel-art spell meshes through the running Blockbench MCP.

Writes candidates only. publish_pixel_magic.py explicitly installs reviewed exports.
Geometry carries silhouettes; four palette lanes carry surface detail, never film frames.
"""
from pathlib import Path
import sys, json, math, base64, hashlib
from PIL import Image, ImageDraw
import numpy as np
sys.path.insert(0, 'D:/Skill/skills/minecraft-modeling-item/scripts')
from bb_client import Client

ROOT = Path(__file__).resolve().parent
OUT = Path(sys.argv[sys.argv.index('--output')+1]) if '--output' in sys.argv else ROOT / 'generated'
OUT.mkdir(parents=True,exist_ok=True)
SPEC_PATH = ROOT.parent / 'model-source/magic_effects/design-specs.json'
if not SPEC_PATH.exists(): SPEC_PATH=Path('D:/LibreSprite-art-source/magic_effects_39/manifest.json')
SPECS = json.loads(SPEC_PATH.read_text(encoding='utf-8'))
TAU = math.tau
PALETTES = {
 'water': ['1663ad','32b5e9','c6f9ff','f2ffff'],
 'ice': ['286ba6','79c9ef','d4f7ff','ffffff'],
 'fire': ['b52619','f56318','ffd044','fff4c0'],
 'earth': ['443631','795541','ad8661','d6b393'],
 'wind': ['688c9c','a6d3d9','e3ffff','ffffff'],
 'heal': ['276837','5bcf61','b5ff8a','f2ffcd'],
 'detox': ['15776b','46cca4','baff9c','edffe3'],
 'warm': ['9e4934','dc8650','ffd59a','fff0c8'],
 'barrier': ['284a91','668ce8','afdfff','e9faff'],
 'purple': ['603091','a35de6','e0a4ff','fff0ff'],
}

def box(at, size):
 x,y,z=at; a,b,c=[v/2 for v in size]
 v=[[x+sx*a,y+sy*b,z+sz*c] for sx,sy,sz in [(-1,-1,-1),(1,-1,-1),(1,1,-1),(-1,1,-1),(-1,-1,1),(1,-1,1),(1,1,1),(-1,1,1)]]
 return v,[[0,3,2,1],[4,5,6,7],[0,4,7,3],[1,2,6,5],[0,1,5,4],[3,7,6,2]]

def combine(name, solids, material=1):
 v=[]; f=[]
 for vs,fs in solids:
  n=len(v); v+=vs; f += [[n+i for i in face] for face in fs]
 return dict(name=name, vertices=[[round(p*16,5) for p in q] for q in v], faces=f, material=material)

def blocks(name, entries, material=1):
 return combine(name,[box(at,size) for at,size in entries],material)

def stepped(name, scale=(1,1,1), material=1, seed=0):
 # Union of three perpendicular cuboids; suppress all internal faces using occupancy.
 # Only the silhouette is stepped, not every pixel of the texture.
 n=6; cells=set()
 for x in range(n):
  for y in range(n):
   for z in range(n):
    q=[(t+.5)/n*2-1 for t in (x,y,z)]
    if sum(t*t for t in q) < .9 + .08*math.sin(x*2+y*3+z+seed): cells.add((x,y,z))
 v=[]; f=[]
 # Greedy merge exposed grid faces into rectangles.
 for axis in range(3):
  u,w=[a for a in range(3) if a!=axis]
  for sign in (-1,1):
   for layer in range(n):
    mask=set()
    for cell in cells:
     if cell[axis]!=layer: continue
     other=list(cell);other[axis]+=sign
     if tuple(other) not in cells:mask.add((cell[u],cell[w]))
    while mask:
     a,b=min(mask);aw=1
     while (a+aw,b) in mask:aw+=1
     bh=1
     while all((i,b+bh) in mask for i in range(a,a+aw)):bh+=1
     mask.difference_update((i,j) for i in range(a,a+aw) for j in range(b,b+bh))
     coords=[]
     for i,j in [(a,b),(a+aw,b),(a+aw,b+bh),(a,b+bh)]:
      p=[0.,0.,0.];p[axis]=(layer+(sign>0))/n-.5;p[u]=i/n-.5;p[w]=j/n-.5
      coords.append([p[k]*scale[k] for k in range(3)])
     normal=np.cross(np.array(coords[1])-coords[0],np.array(coords[2])-coords[0])
     if normal[axis]*sign<0:coords.reverse()
     idx=len(v);v+=coords;f.append(list(range(idx,idx+4)))
 return dict(name=name,vertices=[[round(t*16,5) for t in p] for p in v],faces=f,material=material)

def arc(name, radius=.4, y=0, start=0, length=TAU, vertical=False, material=2, thickness=.035):
 # Continuous square-section ribbon; connected volume, not a necklace of cubes.
 n=20;v=[];f=[]
 for i in range(n+1):
  a=start+i*length/n;cx=round(radius*math.cos(a)*16)/16;cz=round(radius*math.sin(a)*16)/16
  for radial,h in [(-1,-1),(1,-1),(1,1),(-1,1)]:
   x=cx+math.cos(a)*thickness*radial;z=cz+math.sin(a)*thickness*radial
   p=[x,y+h*thickness*.5,z];v.append([p[0],p[2],p[1]] if vertical else p)
 for i in range(n):
  for k in range(4):f.append([i*4+k,i*4+(k+1)%4,(i+1)*4+(k+1)%4,(i+1)*4+k])
 f.extend([[3,2,1,0],[n*4,n*4+1,n*4+2,n*4+3]])
 # Establish consistent outward winding for both orientations.
 volume=sum(float(np.dot(v[q[0]],np.cross(v[q[j]],v[q[j+1]])))/6 for q in f for j in range(1,len(q)-1))
 if volume<0:f=[q[::-1] for q in f]
 return combine(name,[(v,f)],material)

def crystal(name,at=(0,0,0),size=(.22,1,.22),axis='y',material=2):
 # Square-prism crystal with a real four-faced point, distinct from round spikes.
 x,y,z=at;a,b,c=size
 vs=[[-a/2,-b/2,-c/2],[a/2,-b/2,-c/2],[a/2,b*.2,-c/2],[-a/2,b*.2,-c/2],[-a/2,-b/2,c/2],[a/2,-b/2,c/2],[a/2,b*.2,c/2],[-a/2,b*.2,c/2],[0,b/2,0]]
 if axis=='z':vs=[[p[0],p[2],p[1]] for p in vs]
 vs=[[p[0]+x,p[1]+y,p[2]+z] for p in vs]
 fs=[[0,4,7,3],[1,2,6,5],[0,1,5,4],[0,3,2,1],[4,5,6,7],[3,7,8],[7,6,8],[6,2,8],[2,3,8]]
 # Axis swap changes handedness.
 if axis=='z':fs=[f[::-1] for f in fs]
 return combine(name,[(vs,fs)],material)

def build(s):
 sid=s['spell'];family=s['family'];parts=[]
 def add(p):parts.append(p)
 def flame(j,at,height=.5):
  x,y,z=at
  add(blocks('flame_'+str(j),[((x,y,z),(.15,height*.55,.15)),((x+.035,y+height*.34,z),(.09,height*.4,.1)),((x+.065,y+height*.6,z),(.045,height*.22,.045))],1))
  add(blocks('ember_'+str(j),[((x,y+height*.85,z),(.035,.055,.035))],3))
 if sid=='magic_shield':
  # Surrounding field, not a second forward-facing physical shield.
  for side in range(4):
   a=side*TAU/4;x=math.sin(a)*.43;z=math.cos(a)*.43
   size=(.80,.82,.025) if side%2==0 else (.025,.82,.80)
   add(blocks('panel_'+str(side),[((x,0,z),size)],0))
  for j in range(2):add(arc('halo_'+str(j),.48,-.4+j*.8,material=2,thickness=.03))
  add(blocks('rim_0',[((x,0,z),(.025,.84,.025)) for x in (-.4,.4) for z in (-.4,.4)],3))
 elif sid in ('water_shield','physical_shield'):
  for i in range(3):
   # Broad transparent panels with gaps and a stepped, luminous perimeter.
   z=(abs(i-1))*.08
   add(blocks('panel_'+str(i),[(((i-1)*.30,0,z),(.29,.8,.045))],0))
  for j in range(2):add(arc('rune_'+str(j),.46,(-.06 if j else .06),j*.17,vertical=True,material=2,thickness=.025))
  add(blocks('rim_0',[((x,y,0),(.06,.06,.075)) for x,y in [(-.39,-.35),(.39,-.35),(-.39,.35),(.39,.35),(0,.48),(0,-.48)]],3))
 elif sid in ('ice_fortress','waterfall'):
  for i in range(7):
   x=(i-3)/7;h=.82+abs(i-3)*.045
   if sid=='ice_fortress':add(crystal('pillar_'+str(i),(x,0,0),(.14,h,.18),material=1+i%2))
   else:add(blocks('cascade_'+str(i),[((x,0,(i%2)*.03),(.13,h,.09))],1))
  add(blocks('foam_0',[(((i-3)/7,-.45,.08),(.14,.06,.12)) for i in range(7)],3))
 elif sid in ('healing','ex_healing','detoxification','antidote_ii'):
  count=3 if sid in ('ex_healing','antidote_ii') else 2
  for j in range(count):
   add(arc('halo_'+str(j),.38-j*.035,-.35+j*.27,j*.6,length=TAU*.8,material=1+j%2,thickness=.025))
  for j in range(6):
   a=j*TAU/6;x=math.cos(a)*.28;z=math.sin(a)*.28
   add(blocks('mote_'+str(j),[((x,.1,z),(.025,.12,.025)),((x,.1,z),(.08,.025,.025))],3))
 elif sid in ('heat_island','burning_place'):
  add(arc('ground_0',.46,-.15,material=0,thickness=.05))
  for j in range(12 if sid=='burning_place' else 6):
   a=j*TAU/(12 if sid=='burning_place' else 6)
   if sid=='burning_place':flame(j,(.43*math.cos(a),-.1,.43*math.sin(a)),.4+(j%3)*.12)
   else:add(blocks('heat_'+str(j),[((.4*math.cos(a),0,.4*math.sin(a)),(.035,.12,.035))],2))
 elif sid in ('flamethrower','flame_pillar','heat_hand'):
  for j in range(7):
   if sid=='flamethrower':
    z=-.45+j*.14;r=.06+j*.025
    add(blocks('jet_'+str(j),[((math.sin(j*2)*r,math.cos(j*2)*r,z),(.11+j*.035,.11+j*.035,.24))],1+j%3))
   elif sid=='flame_pillar':
    a=j*TAU/7;flame(j,(.25*math.cos(a),-.25,.25*math.sin(a)),.65+(j%3)*.15)
   else:add(blocks('heat_'+str(j),[((math.sin(j*2)*.28,(j-3)*.11,math.cos(j*2)*.28),(.06,.08,.06))],2))
 elif sid in ('flame_slice','sonic_boom','wind_slice','icicle_edge'):
  # Thick crescent, with deliberate stepped ends and layered trailing ribs.
  entries=[]
  for i in range(9):
   x=(i-4)/9;y=.22*(1-(x/.5)**2);h=.08+.12*(1-abs(i-4)/5)
   entries.append(((x,y,0),(.115,h,.10+(1-abs(i-4)/5)*.16)))
  add(blocks('blade_0',entries,2))
  for j in range(2):add(blocks('wake_'+str(j),[((x,y-.04,-.16-j*.12),(sz[0],sz[1]*.45,.045)) for ((x,y,z),sz) in entries[1:-1]],1))
  if family=='fire':
   for j in range(3):flame(j,((j-1)*.27,0,-.12),.2)
 elif sid in ('earth_lance','ice_lance','ice_pillar','ice_blade','water_arrow','icicle_break','ice_smash'):
  vertical=sid in ('earth_lance','ice_lance','ice_pillar','ice_smash')
  add(crystal('core_0',size=(.42,1,.42) if vertical else (.30,1,.30),axis='y' if vertical else 'z',material=1))
  count=6 if sid in ('ice_smash','icicle_break') else 3
  for j in range(count):
   a=j*TAU/count;r=.24
   at=(r*math.cos(a),-.18,r*math.sin(a)) if vertical else (r*math.cos(a),r*math.sin(a),-.15)
   add(crystal('shard_'+str(j),at,(.14,.55+(j%2)*.15,.14),axis='y' if vertical else 'z',material=2+j%2))
 elif sid in ('water_splash','icicle_field'):
  add(arc('shock_0',.42,-.2,material=2,thickness=.04))
  for j in range(10):
   a=j*TAU/10;r=.22+(j%2)*.17
   if sid=='icicle_field':add(crystal('growth_'+str(j),(r*math.cos(a),0,r*math.sin(a)),(.07,.5+(j%3)*.18,.07),material=1+j%2))
   else:add(blocks('splash_'+str(j),[((r*math.cos(a),-.05,r*math.sin(a)),(.07,.32+(j%3)*.12,.07))],1+j%3))
 elif sid=='flood_flush':
  for j in range(7):
   x=(j-3)/7;h=.45+.17*math.cos(j)
   add(blocks('wave_'+str(j),[((x,-.18,0),(.15,h,.3)),((x,h/2-.18,-.08),(.15,.09,.15))],1))
   add(blocks('foam_'+str(j),[((x,h/2-.10,-.08),(.15,.055,.15))],3))
 elif sid=='wind_bind':
  for j in range(3):add(arc('vortex_'+str(j),.4,-.35+j*.35,j*.7,length=TAU*.8,material=1+j%2,thickness=.03))
 elif sid=='wind_blast':
  for j in range(4):add(arc('pressure_'+str(j),.12+j*.09,-.35+j*.22,j*.3,length=TAU*.85,vertical=True,material=2,thickness=.028))
 elif sid in ('rock_bullet','stone_cannon','earth_pillar'):
  add(stepped('stone_0',(1,1,1.25 if sid=='stone_cannon' else 1),1,seed=3))
  if sid=='earth_pillar':
   for j in range(4):add(blocks('debris_'+str(j),[(((j%2-.5)*.7,-.2,(j//2-.5)*.7),(.14,.2,.14))],j%3))
  else:add(blocks('chips_0',[((-.32,.15,-.3),(.12,.12,.12)),((.27,-.1,-.37),(.15,.1,.1))],0))
 elif sid in ('fireball','ex_flame','explosion'):
  add(stepped('core_0',(.72,.72,.72),3))
  add(stepped('mantle_0',(.92,.92,.92),1,seed=2))
  for j in range(5):
   a=j*TAU/5
   add(blocks('tail_'+str(j),[((.26*math.cos(a),.26*math.sin(a),-.35),(.18,.18,.35)),((.28*math.cos(a),.28*math.sin(a),-.63),(.10,.10,.20))],2))
  if sid=='explosion':add(arc('shock_0',.49,-.15,material=2,thickness=.05))
 elif sid=='wind_ball':
  for j in range(3):add(arc('vortex_'+str(j),.32+(j%2)*.08,-.25+j*.25,j*.7,length=TAU*.85,vertical=True,material=1+j%3,thickness=.04))
 elif sid in ('water_ball','water_cannon'):
  add(stepped('core_0',(.85,.85,1 if sid=='water_ball' else 1.15),1))
  for j in range(2):add(arc('flow_'+str(j),.45,(-.15+j*.30),j*.8,length=TAU*.75,vertical=True,material=2,thickness=.035))
  add(blocks('foam_0',[((-.21,.30,.20),(.16,.06,.12)),((.19,.19,.29),(.08,.10,.04))],3))
 else:raise ValueError('Missing explicit design: '+sid)
 assert len(parts)<=32,(sid,len(parts))
 assert sum(len(p['faces']) for p in parts)<=600,(sid,'face budget')
 return parts

def texture(s):
 colors=[tuple(bytes.fromhex(c)) for c in PALETTES[s['family']]]
 family=s['family'];sid=s['spell']
 image=Image.new('RGBA',(64,64*24));px=image.load()
 seed=int(hashlib.sha256(sid.encode()).hexdigest()[:6],16)
 def noise(x,y):
  n=((x%16)*374761393+(y%16)*668265263+seed*1442695041)&0xffffffff
  n=((n^(n>>13))*1274126177)&0xffffffff
  return ((n^(n>>16))&255)/255
 for frame in range(24):
  t=frame*math.tau/24
  for y in range(64):
   for x in range(64):
    lane=x//16;u=x%16;v=y%16
    # Small low-contrast pixel clusters, coherent over time; no random frame flicker.
    grain=(noise(u//2,v//2)-.5)*.065+(noise(u,v)-.5)*.025
    bright=0;factor=1+grain;alpha=255
    if family=='earth':
     # Minecraft stone-like mineral clusters, stable rather than scrolling rock.
     patch=noise(u//3,v//3)
     factor=.91+patch*.13+grain
     if noise(u//2,v//2)<.12:factor-=.07
     if noise((u+1)//3,(v+1)//3)>.82:factor+=.045
    elif family=='ice':
     # Sparse stepped veins, with a quiet cyan face between them.
     vein=(u+v//2+seed)%16
     factor=.94+grain+.035*math.sin(t+u*.35+v*.18)
     bright=.13 if vein in (2,3) else .045 if vein==4 else 0
    elif family=='water':
     wave=math.sin(u*math.tau/16+math.sin(v*math.tau/16+t)*.8+t)
     wave+=.45*math.sin(v*math.tau/8-t)
     factor=.95+round(wave*3)/3*.055+grain
     bright=.12 if wave>1.12 else 0
    elif family in ('fire','warm'):
     tongue=math.sin(u*math.tau/8+math.sin(v*math.tau/16-t)*.9)
     tongue+=.5*math.sin(v*math.tau/16-t*2)
     factor=.94+round(tongue*3)/3*.055+grain
     bright=.16 if tongue>1.05 else .055 if tongue>.6 else 0
    elif family=='wind':
     ribbon=(u+v//3+int(frame/3))%16
     factor=.95+grain*.5
     bright=.075 if ribbon in (3,4) else 0
    elif family in ('heal','detox'):
     factor=.96+grain*.5+.025*math.sin(t+v*math.tau/16)
     # Small sparse crosses, not all-over glitter/noise.
     bright=.09 if (u==7 and 5<=v<=9 or v==7 and 5<=u<=9) else 0
    else:
     factor=.95+grain*.4+.025*math.sin(t+v*.4)
     edge=(u+v//4)%8
     bright=.08 if edge==0 else 0
    base=colors[lane];highlight=colors[min(3,lane+1)]
    rgb=tuple(max(0,min(255,round(c*factor*(1-bright)+h*bright))) for c,h in zip(base,highlight))
    if sid in ('water_shield','physical_shield','magic_shield') and lane==0:
     alpha=58+(18 if (u+v//4)%8==0 else 0)
    px[x,y+frame*64]=rgb+(alpha,)
 p=OUT/sid/'surface.png';p.parent.mkdir(parents=True,exist_ok=True);image.save(p)
 p.with_suffix('.png.mcmeta').write_text('{"texture":{"blur":false,"clamp":true}}')
 return p


def result(r):
 if r.get('isError'):raise RuntimeError(str(r)[:2000])
 if r.get('structuredContent') is not None:return r['structuredContent']
 text=next((x['text'] for x in r.get('content',[]) if x['type']=='text'),None)
 if text:
  try:return json.loads(text)
  except ValueError:return text
 return r

def compile_model(model):
 parts=[]
 for e in model['elements']:
  if e.get('type')!='mesh':continue
  quads=[];verts=e['vertices']
  for f in e['faces'].values():
   keys=f['vertices']
   if len(keys)<3:continue
   arr=[[*[round(t/16,6) for t in verts[k]],*[round(t/64,6) for t in f['uv'][k]]] for k in keys]
   if len(arr)==3:arr.append(arr[-1])
   quads.append(arr)
  parts.append({'name':e['name'],'quads':quads})
 return {'parts':parts}

def animation_tracks(parts,sid):
 bones={}
 for part in parts:
  role,index=part['name'].rsplit('_',1);index=int(index);keys=[]
  for time in (0,.3,.6,.9,1.2):
   phase=time/1.2*TAU+index*1.91;key={'time':time}
   if role in ('flow','vortex','halo'):
    axis=1 if sid=='wind_bind' or role=='halo' else 2
    rotation=[0,0,0];rotation[axis]=time/1.2*(360 if index%2==0 else -360);key['rotation']=rotation
   elif role in ('flame','tail','jet','core','mantle'):
    scale=[1,1,1];scale[2 if role=='tail' else 1]=1+.12*math.sin(phase);key['scale']=scale
   elif role in ('ember','mote','heat','wave','foam','wake','cascade','splash'):
    key['position']=[.3*math.cos(phase),.6*math.sin(phase),.3*math.sin(phase)]
   elif role in ('stone','chips','debris'):
    key['rotation']=[time/1.2*90,0,time/1.2*360]
   elif role in ('growth','pillar','shard'):
    key['scale']=[1,1+.035*math.sin(phase),1]
   else:key['scale']=[1,1,1]
   keys.append(key)
  bones['rig_'+part['name']]=keys
 return bones

def main():
 c=Client(timeout=90);audit=[]
 # No user model is overwritten: each spell is created as a fresh editor project.
 tools=c.call('tools/list',{})
 (OUT/'mcp-tools.json').write_text(json.dumps(tools,ensure_ascii=False),encoding='utf-8')
 for s in SPECS:
  sid=s['spell'];folder=OUT/sid
  if (folder/'runtime.json').exists() and '--force' not in sys.argv:
   audit.append(json.loads((folder/'audit.json').read_text()));continue
  parts=build(s);p=texture(s)
  result(c.tool('create_project',{'name':'magic_animate_effect_'+sid,'format':'free'}))
  result(c.tool('add_group',{'name':'effect','origin':[0,0,0]}))
  data='data:image/png;base64,'+base64.b64encode(Image.open(p).crop((0,0,64,64))._repr_png_()).decode()
  result(c.tool('create_texture',{'name':sid+'_pixel_surface','width':64,'height':64,'data':data,'render_mode':'emissive','render_sides':'front'}))
  for part in parts:
   group='rig_'+part['name']
   result(c.tool('add_group',{'name':group,'origin':[0,0,0],'parent':'effect'}))
   mesh={k:v for k,v in part.items() if k!='material'}
   result(c.tool('place_mesh',{'elements':[mesh],'group':group,'texture':sid+'_pixel_surface'}))
  lanes={p['name']:p['material'] for p in parts}
  # Scoped UV projection; one material atlas, no per-part texture allocations.
  code='(()=>{const lanes='+json.dumps(lanes)+';Undo.initEdit({elements:Mesh.all,uv_only:true,textures:Texture.all,uv_mode:true});Project.texture_width=64;Project.texture_height=64;for(const t of Texture.all){t.uv_width=64;t.uv_height=64;}for(const m of Mesh.all){for(const f of Object.values(m.faces)){const vs=f.vertices.map(k=>m.vertices[k]);const n=new THREE.Vector3(...vs[1]).sub(new THREE.Vector3(...vs[0])).cross(new THREE.Vector3(...vs[2]).sub(new THREE.Vector3(...vs[0])));const axis=Math.abs(n.y)>Math.abs(n.x)&&Math.abs(n.y)>Math.abs(n.z)?1:Math.abs(n.z)>Math.abs(n.x)?2:0;for(const k of f.vertices){const p=m.vertices[k],a=axis===0?p[2]:p[0],b=axis===1?p[2]:p[1];f.uv[k]=[lanes[m.name]*16+1+((a+16)%16)/16*14,1+((b+16)%16)/16*62];}}}Undo.finishEdit("Project pixel spell UV");Canvas.updateAll();return {meshes:Mesh.all.length};})()'
  result(c.tool('risky_eval',{'code':code}))
  result(c.tool('create_animation',{'name':sid+'_material_flow','loop':True,'animation_length':1.2,'bones':animation_tracks(parts,sid)}))
  export=result(c.tool('export_model',{'codec_id':'project','max_content_length':2000000}))
  model=export['content'];model=json.loads(model) if isinstance(model,str) else model
  (folder/(sid+'.bbmodel')).write_text(json.dumps(model,ensure_ascii=False),encoding='utf-8')
  runtime=compile_model(model)
  (folder/'runtime.json').write_text(json.dumps(runtime,separators=(',',':')),encoding='utf-8')
  record={'spell':sid,'reference':s['reference'],'parts':len(parts),'quads':sum(len(p['quads']) for p in runtime['parts']),'texture':[64,1536],'frames':24,'source':'Blockbench MCP export','family':s['family']}
  (folder/'audit.json').write_text(json.dumps(record,indent=2));audit.append(record)
  print(json.dumps(record),flush=True)
 (OUT/'audit.json').write_text(json.dumps(audit,indent=2));c.close()

if __name__=='__main__':main()
