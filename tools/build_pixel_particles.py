"""Small hard-edged accent particles; bodies remain true 3D meshes."""
from build_pixel_magic import SPECS,PALETTES,ROOT
from PIL import Image,ImageDraw
import math,json

out=ROOT/'particles';out.mkdir(exist_ok=True)
sheet=Image.new('RGBA',(24*16,len(SPECS)*16))
for row,s in enumerate(SPECS):
 colors=[tuple(bytes.fromhex(c))+(255,) for c in PALETTES[s['family']]]
 folder=out/s['spell'];folder.mkdir(exist_ok=True)
 for role in ('gather','trail','burst'):
  for frame in range(24):
   p=frame/23;im=Image.new('RGBA',(16,16));d=ImageDraw.Draw(im)
   r=max(1,round((1-p)*4 if role=='trail' else 1+math.sin(p*math.pi)*4))
   x=8;y=round(9-p*4)
   family=s['family']
   if family in ('ice','earth'):
    d.polygon([(x-r,y-r),(x+r,y-r+1),(x+r-1,y+r),(x-r,y+r-1)],fill=colors[1])
    d.line([(x-r,y-r),(x+r,y-r+1),(x+r,y)],fill=colors[3],width=1)
   elif family in ('fire','warm'):
    d.rectangle((x-r,y-r,x+r,y+r),fill=colors[1]);d.rectangle((x-r+1,y,x+r-1,y+r),fill=colors[2]);d.rectangle((x-1,y+1,x+1,y+r),fill=colors[3])
    if r>2:d.rectangle((x-1,y-r-2,x,y-r),fill=colors[2])
   elif family in ('heal','detox','barrier','purple'):
    d.rectangle((x-1,y-r,x,y+r),fill=colors[2]);d.rectangle((x-r,y-1,x+r,y),fill=colors[2]);d.point((x,y),fill=colors[3])
   else:
    d.rectangle((x-r,y-r+1,x+r,y+r-1),fill=colors[1]);d.rectangle((x-r+1,y-r,x+r-1,y+r),fill=colors[1]);d.line((x-r+1,y-r+1,x+1,y-r+1),fill=colors[3],width=1)
   if role=='burst' and frame>4:
    a=frame*.6;ox=round(math.cos(a)*5);oy=round(math.sin(a)*5)
    d.rectangle((max(0,7+ox),max(0,7+oy),min(15,8+ox),min(15,8+oy)),fill=colors[2])
   im.save(folder/(role+'_'+str(frame).zfill(2)+'.png'))
   if role=='burst':sheet.paste(im,(frame*16,row*16))
sheet.resize((768,len(SPECS)*32),Image.Resampling.NEAREST).save(out/'particle-sheet.png')
print('Generated',len(SPECS)*3*24,'16x16 hard-edge accent frames')
