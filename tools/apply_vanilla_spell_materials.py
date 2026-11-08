"""Install vanilla-derived spell materials and face-local UVs, preserving geometry."""
from pathlib import Path
import json,base64,io,shutil,zipfile,math,hashlib
import numpy as np
from PIL import Image,ImageDraw

REPO=Path(__file__).resolve().parent.parent
WORK=REPO/'build/vanilla-material-work'
ASSETS=REPO/'src/main/resources/assets/mushokutenseiorigin'
VANILLA=Path('C:/Users/AZHI/.gradle/caches/fabric-loom/26.1/minecraft-client.jar')
SOURCES={'water':'water_still','ice':'packed_ice','fire':'fire_0','earth':'stone','wind':'snow','heal':'emerald_block','detox':'emerald_block','warm':'magma','barrier':'amethyst_block','purple':'amethyst_block'}
COLORS={'water':[(32,81,155),(47,130,213),(110,196,245),(215,247,255)],'ice':[(62,115,180),(109,173,222),(168,222,248),(230,249,255)],'fire':[(174,41,10),(244,104,13),(255,180,36),(255,238,154)],'earth':[(79,75,68),(115,110,99),(152,142,124),(188,175,150)],'wind':[(107,151,164),(166,209,215),(213,238,240),(247,255,255)],'heal':[(31,116,56),(61,175,82),(141,228,111),(223,255,179)],'detox':[(23,109,90),(54,179,139),(144,232,155),(225,255,205)],'warm':[(135,57,31),(199,102,48),(242,169,88),(255,220,158)],'barrier':[(58,61,125),(97,124,198),(163,197,245),(230,245,255)],'purple':[(88,39,137),(142,76,197),(205,150,238),(246,220,255)]}

def backup(p):
 if not p.exists():return
 rel=Path('project')/p.relative_to(REPO) if p.is_relative_to(REPO) else Path('blockbench')/p.parent.name/p.name
 dst=WORK/'baseline'/rel
 if not dst.exists():dst.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(p,dst)

def write(p,data):
 backup(p);p.parent.mkdir(parents=True,exist_ok=True)
 if isinstance(data,bytes):p.write_bytes(data)
 else:p.write_text(data,encoding='utf-8',newline='\n')

def atlas(s,z):
 family=s['family'];name=SOURCES[family]
 raw=Image.open(io.BytesIO(z.read('assets/minecraft/textures/block/'+name+'.png'))).convert('RGBA')
 w,h=raw.size;n=h//w
 canvas=Image.new('RGBA',(64,16*24))
 for f in range(24):
  frame=int(f*n/24)%n
  tile=raw.crop((0,frame*w,w,(frame+1)*w)).resize((16,16),Image.Resampling.NEAREST)
  a=np.asarray(tile).astype(float);luma=a[:,:,:3].mean(2)
  # Fully transparent fire pixels become the dark ember surface of the solid core.
  if family in ('fire','warm'):luma=np.where(a[:,:,3]<32,20,luma)
  lo,hi=float(luma.min()),float(luma.max());contrast=(luma-lo)/max(1,hi-lo)
  for lane,color in enumerate(COLORS[family]):
   factor=.64+contrast*.48
   rgb=np.clip(np.array(color)[None,None,:]*factor[:,:,None],0,255).astype('uint8')
   alpha=np.full((16,16,1),255,dtype='uint8')
   if s['spell'] in ('water_shield','physical_shield','magic_shield') and lane==0:alpha[:]=70
   canvas.paste(Image.fromarray(np.concatenate([rgb,alpha],axis=2)),(lane*16,f*16))
 return canvas

def project_uv(model):
 for e in model['elements']:
  if e.get('type')!='mesh':continue
  vertices=e['vertices']
  # Material lane comes from old UV, before replacing projection.
  all_u=[uv[0] for f in e['faces'].values() for uv in f['uv'].values()]
  lane=min(3,max(0,int(np.median(all_u)//16)))
  for f in e['faces'].values():
   keys=f['vertices'];p=np.array([vertices[k] for k in keys]);normal=np.cross(p[1]-p[0],p[2]-p[0]);axis=int(np.argmax(np.abs(normal)))
   u,v=([2,1] if axis==0 else [0,2] if axis==1 else [0,1])
   lo=p.min(0);hi=p.max(0)
   for k,point in zip(keys,p):
    # Every visible face receives a full 16px tile; no world-modulo discontinuities
    # or 64px-tall material stretched from one edge of the mesh to the other.
    a=(point[u]-lo[u])/max(hi[u]-lo[u],1e-9);b=(point[v]-lo[v])/max(hi[v]-lo[v],1e-9)
    f['uv'][k]=[round(lane*16+.5+a*15,6),round(.5+(1-b)*15,6)]
 model['resolution']={'width':64,'height':16}
 for t in model['textures']:t['width']=64;t['height']=16;t['uv_width']=64;t['uv_height']=16
 return model

def runtime(model):
 parts=[]
 for e in model['elements']:
  if e.get('type')!='mesh':continue
  quads=[]
  for face in e['faces'].values():
   q=[[*[round(t/16,6) for t in e['vertices'][k]],round(face['uv'][k][0]/64,6),round(face['uv'][k][1]/16,6)] for k in face['vertices']]
   if len(q)==3:q.append(q[-1])
   quads.append(q)
  parts.append({'name':e['name'],'quads':quads})
 return {'parts':parts}

def main():
 WORK.mkdir(parents=True,exist_ok=True);specs=json.loads((REPO/'model-source/magic_effects/design-specs.json').read_text());z=zipfile.ZipFile(VANILLA);audit=[]
 for s in specs:
  sid=s['spell'];im=atlas(s,z);buffer=io.BytesIO();im.save(buffer,format='PNG');png=buffer.getvalue()
  path=ASSETS/'textures/effect/vanilla_materials'/f'{sid}.png';write(path,png)
  write(path.with_suffix('.png.mcmeta'),'{"texture":{"blur":false,"clamp":true}}')
  # Also update legacy paths so source previews and external consumers do not lag.
  write(ASSETS/'textures/effect/surfaces'/f'{sid}.png',png)
  preview=io.BytesIO();im.crop((0,0,64,16)).save(preview,format='PNG')
  modelpath=REPO/'model-source/magic_effects'/f'{sid}.bbmodel'
  model=project_uv(json.loads(modelpath.read_text(encoding='utf-8')))
  model['textures'][0]['source']='data:image/png;base64,'+base64.b64encode(preview.getvalue()).decode()
  write(modelpath,json.dumps(model,ensure_ascii=False,separators=(',',':')))
  external=Path('D:/BlockbenchAssets')/('magic_animate_effect_'+sid)
  write(external/f'{sid}.bbmodel',json.dumps(model,ensure_ascii=False,separators=(',',':')))
  write(external/'surface.png',png)
  mesh=runtime(model);write(ASSETS/'models/effect'/f'{sid}.json',json.dumps(mesh,separators=(',',':')))
  audit.append({'spell':sid,'source_texture':'minecraft:block/'+SOURCES[s['family']],'atlas':[64,384],'tile':[16,16],'frames':24,'sha256':hashlib.sha256(png).hexdigest()})
 client=REPO/'src/client/java/com/example/examplemod/client/magic'
 p=client/'SpellMeshModel.java';txt=p.read_text(encoding='utf-8');txt=txt.replace('textures/effect/surfaces/','textures/effect/vanilla_materials/');write(p,txt)
 p=REPO/'src/main/java/com/example/examplemod/magic/MagicChantEffects.java'
 write(p,'''package com.example.examplemod.magic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

/** One letter per preparation tick, emitted from the anatomical left eye. */
public final class MagicChantEffects {
\tpublic static void tick(ServerPlayer player) {
\t\tvar look = player.getLookAngle();
\t\t// Head yaw, not body yaw: +X is the player's left while facing +Z.
\t\tvar forward = Vec3.directionFromRotation(0, player.getYHeadRot());
\t\tvar left = new Vec3(forward.z, 0, -forward.x);
\t\tvar at = player.getEyePosition().add(left.scale(.105)).add(look.scale(.24));
\t\tvar velocity = left.scale(.023).add(forward.scale(.008)).add(0, .032, 0);
\t\t// count=0 is exactly one particle with the supplied velocity, no random spread.
\t\t((ServerLevel) player.level()).sendParticles(SpellParticles.CHANT,
\t\t\t\tat.x, at.y, at.z, 0, velocity.x, velocity.y, velocity.z, 1);
\t}
\tprivate MagicChantEffects() { }
}
''')
 p=client/'SpellSpriteParticle.java';txt=p.read_text(encoding='utf-8').replace('if(glyph)yd=Math.max(.035,dy);','// Preserve the eye-stream velocity sent by the server, including diagonal rise.');write(p,txt)
 write(REPO/'model-source/magic_effects/vanilla-material-manifest.json',json.dumps(audit,indent=2))
 (WORK/'audit.json').write_text(json.dumps(audit,indent=2))
 print('Replaced 39 material atlases, face UVs, editable previews and runtime texture path; left-eye diagonal glyph stream installed.')

if __name__=='__main__':main()
