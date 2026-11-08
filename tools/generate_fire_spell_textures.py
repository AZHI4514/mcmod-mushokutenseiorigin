"""Pixel-authored looping effect sheets; generated from math, not copied anime frames."""
from pathlib import Path
from PIL import Image, ImageDraw
import math
import sys

OUT = Path(sys.argv[1]) if len(sys.argv)>1 else Path(__file__).parent / 'fire-effect-rework'
OUT.mkdir(exist_ok=True)
TAU = math.tau
FRAMES = 12

def clamp(v):
    return max(0.0, min(1.0, v))

def noise(x,y,t):
    return (math.sin(x*9.1+y*5.3+t)*0.5+math.sin(x*17.3-y*8.7-t*2)*0.3+1)/2

def flame(u,v,t):
    # Several independently moving tongues, with hot cores below and frayed tips above.
    value=0
    for i in range(5):
        center=(i+0.5)/5+0.045*math.sin(t+i*1.9+v*6)
        top=0.42+0.42*(0.5+0.5*math.sin(t+i*2.3))
        half=(0.11+0.025*math.sin(t*2+i))*(1-v/(top+0.04))
        if half>0:
            value=max(value,(1-abs(u-center)/half)*(1-v/(top+0.04)))
    if value<=0.015:
        return (255,106,17,0)
    value+=0.04*math.sin(u*38+v*27-t*3)
    if value<0.035 or v>0.97:
        return (255,106,17,0)
    core=clamp(value*2.5)
    r,g,b=(255,int(85+170*core),int(12+193*core**3))
    alpha=int(230*clamp(value*5)*clamp((0.99-v)*6)*clamp(min(u,1-u)*18))
    return r,g,b,alpha

def sheet(name,w,h,paint):
    im=Image.new('RGBA',(w,h*FRAMES))
    for f in range(FRAMES):
        t=f/FRAMES*TAU
        for y in range(h):
            for x in range(w):
                im.putpixel((x,f*h+y),paint((x+0.5)/w,1-(y+0.5)/h,t))
    im.save(OUT/(name+'.png'))
    return im

flames=sheet('burning_tongues',48,64,flame)

def slash(u,v,t):
    x=u*2-1
    # Curved leading edge and a tapered trailing skirt; endpoints are transparent.
    edge=0.54+0.28*(1-x*x)
    distance=edge-v
    tail=0.22+0.06*math.sin(u*29+t*2)+0.03*math.sin(u*61-t)
    if distance<-0.045 or distance>tail or abs(x)>0.98:
        return 255,91,9,0
    fade=clamp((1-abs(x))*7)*clamp((tail-distance)*25)*clamp((distance+0.045)*45)
    hot=clamp(1-abs(distance-0.025)*11)
    alpha=int(245*fade*(0.75+0.25*noise(u,v,t)))
    return 255,int(95+160*hot),int(10+215*hot**3),alpha

slash_im=sheet('flame_slice',96,48,slash)

def haze(u,v,t):
    x=(u*2-1)+math.sin(v*8-t)*0.13+math.sin(v*17+t)*0.04
    a=clamp(1-abs(x)*2.8)*math.sin(v*math.pi)**2
    return 255,211,150,int(a*75)

haze_im=sheet('heat_wisp',32,64,haze)

def glow(u,v,t):
    r=math.hypot((u-.5)*2,(v-.5)*2)
    a=clamp(1-r)**2*(0.8+0.2*math.sin(t+r*9))
    return 255,171,66,int(a*100)

glow_im=sheet('heat_ground',64,64,glow)

# Contact sheet on a dark background: inspect frame shape, alpha edges and loop phases.
preview=Image.new('RGB',(960,640),(25,29,35))
d=ImageDraw.Draw(preview)
for row,(name,im,h) in enumerate([('Flame slice',slash_im,48),('Burning wall',flames,64),('Warm air',haze_im,64),('Ground warmth',glow_im,64)]):
    for col,f in enumerate([0,3,6,9]):
        tile=im.crop((0,f*h,im.width,(f+1)*h))
        tile.thumbnail((225,130)); tile=tile.resize((tile.width*2,tile.height*2),Image.Resampling.NEAREST) if tile.height<65 else tile
        preview.paste(tile,(col*240,row*160+20),tile)
        d.text((col*240+5,row*160+142),name+' '+str(f),fill='white')
preview.save(OUT/'preview.png')
print(OUT)
