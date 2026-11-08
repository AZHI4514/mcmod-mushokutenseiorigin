"""Build transparent spell flipbooks from the user's animation reference footage.

The footage supplies animated surface detail and sampled colors. Designed masks remove
characters/backgrounds and adapt each effect to the game's existing collision volume.
No Minecraft particle or block textures are used for these spell surfaces.
"""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFilter
import numpy as np
import json, math, sys, hashlib

REF=Path(sys.argv[1]) if len(sys.argv)>1 else Path('D:/BlockbenchAssets/magic_animate_effect')
OUT=Path(sys.argv[2]) if len(sys.argv)>2 else Path(__file__).parent/'reference-spell-effects'
ASSETS=OUT/'assets/mushokutenseiorigin'
N=24
# id, family, silhouette, reference. Missing exact names intentionally borrow related footage.
SPECS=[
 ('water_ball','water','orb','Waterball_2'),('fireball','fire','orb','Fireball_2'),
 ('wind_ball','wind','vortex','Sonic_Boom'),('rock_bullet','earth','rock','Rock_Bullet'),
 ('water_arrow','water','spear','Waterball_3'),('ice_smash','ice','crystal','Ice_Smash'),
 ('flame_slice','fire','crescent','Flame_Slice'),('healing','heal','halo','Healing'),
 ('water_shield','water','curtain','Water_Shield_2'),('ice_blade','ice','spear','Icicle_Edge'),
 ('heat_hand','warm','haze','Heat_Hand'),('heat_island','warm','haze','Heat_Hand'),
 ('burning_place','fire','flame','Burning_Place'),('detoxification','detox','halo','Healing_2'),
 ('magic_shield','barrier','shield','Physical_Shield'),('water_cannon','water','jet','Waterball_3'),
 ('water_splash','water','splash','Water_Splash'),('icicle_field','ice','crystal','Icicle_Field'),
 ('ex_flame','fire','orb','Ex-Flame'),('sonic_boom','wind','crescent','Sonic_Boom'),
 ('stone_cannon','earth','rock','Stone_Cannon_2'),('ice_pillar','ice','crystal','Ice_Smash'),
 ('waterfall','water','curtain','Flood_Flush'),('ice_lance','ice','spear','Ice_Smash'),
 ('icicle_break','ice','crystal','Icicle_Break'),('icicle_edge','ice','crescent','Icicle_Edge'),
 ('flood_flush','water','wave','Flood_Flush'),('ice_fortress','ice','shield','Frost_Nova'),
 ('explosion','fire','splash','Exodus_Flame_2'),('flame_pillar','fire','flame','Burning_Place'),
 ('flamethrower','fire','jet','Flamethrower'),('wind_slice','wind','crescent','Sonic_Boom'),
 ('wind_bind','wind','vortex','Tornado'),('wind_blast','wind','jet','Wind_Blast'),
 ('earth_lance','earth','spear','Earth_Lance'),('earth_pillar','earth','rock','Earth_Pillar'),
 ('ex_healing','heal','halo','Healing_2'),('antidote_ii','detox','halo','X-Healing'),
 ('physical_shield','purple','shield','Physical_Shield')]
PALETTE={'water':(42,159,222),'fire':(255,124,32),'wind':(212,246,241),
 'earth':(151,118,83),'ice':(140,218,246),'heal':(106,235,150),
 'detox':(68,215,173),'barrier':(92,186,237),'purple':(181,126,244),'warm':(239,181,107)}
y,x=np.mgrid[0:64,0:64].astype(float)
x=(x+.5)/32-1;y=1-(y+.5)/32
r=np.hypot(x,y);a=np.arctan2(y,x)
def clamp(v):return np.clip(v,0,1)

def design(shape,t,seed):
 flow=np.sin(x*9+y*7-t*2)+.45*np.sin(x*21-y*13+t*3)
 rim=np.exp(-((r-.70-.025*np.sin(a*6+t*2))/.055)**2)
 if shape=='orb':
  edge=.78+.025*np.sin(a*7+t*2)+.02*np.sin(a*11-t*3)
  mask=clamp((edge-r)*22)
  light=.25+.35*clamp(1-r)+.5*clamp((flow-.25)*.7)+.5*rim
 elif shape=='rock':
  edge=.73+.05*np.sin(a*5+seed)+.025*np.sin(a*9-seed)
  mask=clamp((edge-r)*30)
  cracks=clamp((np.abs(np.sin(x*9+y*7+seed))-.93)*10)
  light=(.65+.3*x-.2*y)*(1-.6*cracks)
 elif shape=='crystal':
  edge=.5+.24*np.maximum(0,np.cos(a*7+seed))**5
  mask=clamp((edge-r)*35)
  light=.45+.4*np.abs(np.sin(a*7+seed))+.3*rim+.12*np.sin(t*2+r*14)
 elif shape=='spear':
  half=.05+.17*clamp((y+1)/1.9)
  mask=clamp((half-np.abs(x))*28)*clamp((.91-y)*25)*clamp((y+.91)*15)
  mask=np.maximum(mask,clamp((.33-np.abs(x)) * 20)*clamp((.2-np.abs(y+.35))*18)*.45)
  light=.4+.6*clamp(1-np.abs(x)*12)+.15*np.sin(t*3+y*19)
 elif shape=='crescent':
  edge=.24+.38*(1-x*x)
  thickness=.11+.035*np.sin(x*23-t*3)
  mask=clamp((thickness-np.abs(y-edge))*32)*clamp((.97-np.abs(x))*16)
  light=.4+.65*clamp(1-np.abs(y-edge)*15)+.13*np.sin(x*30+t*3)
 elif shape=='vortex':
  spiral=np.sin(a*3-r*14+t*2)
  mask=clamp((spiral-.4)*2.5)*clamp((.87-r)*8)*clamp((r-.12)*15)
  light=.75+.25*np.sin(a*3-r*14+t*2)
 elif shape=='jet':
  half=.12+.2*(x+1)/2+.035*np.sin(x*18-t*3)
  mask=clamp((half-np.abs(y))*30)*clamp((1-np.abs(x))*10)
  light=.35+.55*clamp(1-np.abs(y)*5)+.15*np.sin(x*25-t*4)
 elif shape in ('curtain','shield'):
  border=np.maximum(np.exp(-((np.abs(x)-.9)/.025)**2),np.exp(-((np.abs(y)-.9)/.025)**2))
  lines=clamp((np.sin(x*23+y*5+t*2)-.8)*4)
  if shape=='shield':lines=clamp((np.cos(x*24)*np.cos(y*21)-.7)*4)
  mask=(.15+.5*lines+.65*border)*clamp((.98-np.abs(x))*35)*clamp((.98-np.abs(y))*35)
  light=.55+.45*lines+.3*border
 elif shape=='flame':
  mask=np.zeros_like(x)
  for i in range(4):
   c=-.72+i*.48+.08*np.sin(t+i*1.8+y*5)
   height=.6+.25*np.sin(t+i*2.1)
   half=.24*clamp((height-y)/1.6)
   tongue=clamp((half-np.abs(x-c))*22)*clamp((height-y)*8)*clamp((y+.97)*25)
   mask=np.maximum(mask,tongue)
  light=.25+.7*clamp((.9-y)/1.6)+.2*np.sin(x*18+y*9-t*3)
 elif shape=='wave':
  edge=.18+.13*np.sin(x*9-t*2)+.04*np.sin(x*21+t*3)
  mask=clamp((edge-y)*20)*clamp((y+.8)*20)*clamp((.96-np.abs(x))*20)
  light=.35+.6*np.exp(-((edge-y)/.08)**2)+.15*flow
 elif shape=='haze':
  bend=x+.1*np.sin(y*7-t)
  mask=np.exp(-(bend*5)**2)*clamp(1-y*y)*.27
  light=.65+.15*np.sin(t+y*7)
 elif shape=='halo':
  mask=clamp(1-r)*.14+np.exp(-((r-.55-.04*np.sin(t))/.05)**2)*.7
  for i in range(5):
   aa=i*math.tau/5+t*.35
   mask+=np.exp(-((x-.64*math.cos(aa))**2+(y-.64*math.sin(aa))**2)/.003)*.8
  light=.65+.35*np.sin(t+r*12)**2
 else:
  edge=.62+.12*np.sin(a*7+t*2)+.05*np.sin(a*13-t*3)
  mask=clamp((edge-r)*18)*(.3+.7*np.exp(-((r-edge+.07)/.1)**2))
  light=.45+.55*np.sin(a*5-r*15+t*2)**2
 return clamp(mask),clamp(light)

records=[]
preview=Image.new('RGB',(9*144,5*166),(18,24,32));draw=ImageDraw.Draw(preview)
for index,(spell,family,shape,name) in enumerate(SPECS):
 source=REF/(name+'.webp');movie=Image.open(source)
 sheet=Image.new('RGBA',(64,64*N))
 particle_dir=ASSETS/'textures/particle/magic'/spell;particle_dir.mkdir(parents=True,exist_ok=True)
 effect_dir=ASSETS/'textures/effect/spells';effect_dir.mkdir(parents=True,exist_ok=True)
 chosen=[]
 for f in range(N):
  # A full loop traverses forward then backward, avoiding a jump at the last frame.
  u=.5-.5*math.cos(f/N*math.tau)
  source_frame=int((movie.n_frames-1)*(.28+.65*u));chosen.append(source_frame)
  movie.seek(source_frame)
  fw,fh=movie.size
  crop=movie.convert('RGB').crop((int(fw*.18),int(fh*.14),int(fw*.90),int(fh*.91))).resize((64,64),Image.Resampling.BILINEAR)
  original=np.asarray(crop).astype(float)/255
  luminance=original.mean(axis=2)
  detail=np.clip((luminance-.4)*.4,-.13,.13)
  mask,lighting=design(shape,f/N*math.tau,index*.79)
  palette=np.array(PALETTE[family])/255
  # The original scene supplies the moving detail; mask/palette strip out recognizable scenery.
  rgb=palette[None,None,:]*(.6+.4*lighting[:,:,None])+detail[:,:,None]
  highlights=clamp((lighting-.65)*2.3)[:,:,None]
  rgb=rgb*(1-highlights*.75)+highlights*.75
  if family=='fire':rgb[:,:,2]*=.45+.55*highlights[:,:,0]
  if family=='earth':rgb=np.array(PALETTE[family])[None,None,:]/255*(.45+.65*lighting[:,:,None])+detail[:,:,None]*.7
  rgba=np.concatenate((clamp(rgb)*255,(mask*245)[:,:,None]),axis=2).astype('uint8')
  im=Image.fromarray(rgba);sheet.paste(im,(0,f*64));im.save(particle_dir/f'burst_{f:02}.png')
  mote=im.resize((32,32),Image.Resampling.LANCZOS)
  ma=np.asarray(mote).copy();yy,xx=np.mgrid[:32,:32]
  ma[:,:,3]=(ma[:,:,3]*np.clip(1-np.hypot(xx-15.5,yy-15.5)/20,0,1)).astype('uint8')
  Image.fromarray(ma).save(particle_dir/f'mote_{f:02}.png')
 sheet.save(effect_dir/(spell+'.png'))
 jsondir=ASSETS/'particles';jsondir.mkdir(exist_ok=True)
 for role in ('gather','trail','burst'):
  prefix='burst' if role=='burst' else 'mote'
  (jsondir/f'{spell}_{role}.json').write_text(json.dumps({'textures':[f'mushokutenseiorigin:magic/{spell}/{prefix}_{f:02}' for f in range(N)]}),encoding='utf-8')
 thumb=sheet.crop((0,6*64,64,7*64)).resize((128,128),Image.Resampling.NEAREST)
 px=index%9*144;py=index//9*166;preview.paste(thumb,(px+8,py),thumb);draw.text((px+3,py+133),spell,fill='white')
 records.append({'spell':spell,'family':family,'shape':shape,'reference':source.name,'source_sha256':hashlib.sha256(source.read_bytes()).hexdigest(),'source_frames':chosen,'frames':N,'size':[64,64]})

# Glyph artwork: copy the SGA font glyphs, not the enchant particle or its orbital motion.
from zipfile import ZipFile
font=None
for jar in Path('C:/Users/AZHI/.gradle/caches/fabric-loom/26.1').glob('*.jar'):
 with ZipFile(jar) as z:
  key='assets/minecraft/textures/font/ascii_sga.png'
  if key in z.namelist():
   from io import BytesIO
   font=Image.open(BytesIO(z.read(key))).convert('RGBA');break
if font is None: raise RuntimeError('SGA source font not found')
glyphdir=ASSETS/'textures/particle/magic/chant';glyphdir.mkdir(parents=True,exist_ok=True)
for i in range(26):
 code=ord('a')+i
 glyph=font.crop(((code%16)*8,(code//16)*8,(code%16)*8+8,(code//16)*8+8))
 glyph.save(glyphdir/f'{i:02}.png')
(ASSETS/'particles/chant_glyph.json').write_text(json.dumps({'textures':[f'mushokutenseiorigin:magic/chant/{i:02}' for i in range(26)]}),encoding='utf-8')
(OUT/'manifest.json').write_text(json.dumps(records,ensure_ascii=False,indent=2),encoding='utf-8')
preview.save(OUT/'contact-sheet.png')
print('Generated',len(records),'spell sheets; 24 frames each, custom gather/trail/burst sprites and SGA glyphs')
