import {pathToFileURL} from 'node:url';
import {resolve,join} from 'node:path';
import {mkdirSync,writeFileSync} from 'node:fs';
// Bake with the upstream pixel painter; no Godot runtime or browser is required.
const upstream=resolve(process.argv[2]), output=resolve(process.argv[3]);
const core=await import(pathToFileURL(join(upstream,'src/core/index.js')));
const fire=await import(pathToFileURL(join(upstream,'src/fx/fire/_lib.js')));
const families={fire:['#60444b','#ffc23d'],water:['#90bfd1','#d5faff'],ice:['#b0d9ed','#e6fcff'],earth:['#9e8870','#d2b598'],wind:['#bacfd2','#e3ffff'],life:['#7dbb8d','#fff1ad'],detox:['#81b6a4','#c6ffd2'],warm:['#bda58c','#ffe3b5'],ward:['#a28bd1','#e8cfff']};
const H=['#fff3a6','#ffe26b','#ffc23d','#ff8a2a','#f2561d','#c9281f','#8a1f17','#4a1712'].map(core.hex);
for(const [family,colors] of Object.entries(families)) for(const role of ['haze','accent']) {
 const dir=join(output,family,role);mkdirSync(dir,{recursive:true});
 for(let i=0;i<24;i++) {
  const t=(i+.4)/24,k=1-t, f=core.createFrame(32,32),p=core.painterFor(f);p.place(16,16);p.lightK=0;
  if(role==='haze') {
   fire.smokePuff(p,0,0,k,7,core.hex(colors[0]),true,family==='fire'||family==='earth'?.75:.38);
   fire.smokePuff(p,-3,-2,k,4,core.hex(colors[0]),false,.30);
  } else if(family==='fire') fire.firePuff(p,0,0,k,9,.15,H,0,0,-1);
  else if(family==='earth'||family==='ice') {
   const a=t*2.8,c=core.hex(colors[1]),rim=core.hex(colors[0]),r=4*(1-.5*t);
   const pts=[[-r,0],[0,-r*1.6],[r,0],[0,r]].map(([x,y])=>[x*Math.cos(a)-y*Math.sin(a),x*Math.sin(a)+y*Math.cos(a)]);
   p.poly(pts,rim,k);p.line(0,-r,0,r*.4,c,k);
  } else if(family==='water') {
   p.ellipse(0,0,3*(1-.5*t),5*(1-.5*t),core.hex(colors[0]),k*.75);p.line(-1,-2,0,-3,core.hex(colors[1]),k);
  } else if(family==='wind'||family==='warm') {
   const pts=[];for(let j=0;j<18;j++){let a=-1.5+j/17*2.7;pts.push([Math.cos(a)*8-4,Math.sin(a)*7]);}p.polyline(pts,core.hex(colors[1]),k*.75);
  } else fire.ember(p,0,0,k,2,[core.hex(colors[1]),core.hex(colors[0]),core.hex(colors[0]),core.hex(colors[0])],t*35);
  const rgba=new Uint8Array(32*32*4);core.composite(f,rgba,null,0);writeFileSync(join(dir,`${String(i).padStart(2,'0')}.rgba`),rgba);
 }
}
writeFileSync(join(output,'bake.json'),JSON.stringify({source:upstream,frames:24,size:32,families:Object.keys(families),colorOnly:true},null,2));
console.log('Baked 432 color-only frames through actual PixelFX painter and puff/ember primitives.');
