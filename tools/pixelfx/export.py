"""Usage: python export.py <raw-frame-folder> <repository-root>. Requires Pillow."""
from pathlib import Path
import json, sys
from PIL import Image
raw,repo=map(Path,sys.argv[1:3])
families=json.loads(Path(__file__).with_name('families.json').read_text())
a=repo/'src/main/resources/assets/mushokutenseiorigin'
for family in set(families.values()):
 for role in ('haze','accent'):
  for i in range(24):
   target=a/'textures/particle/pixelfx'/family/f'{role}_{i:02}.png'
   target.parent.mkdir(parents=True,exist_ok=True)
   Image.frombytes('RGBA',(32,32),(raw/family/role/f'{i:02}.rgba').read_bytes()).save(target)
for spell,family in families.items():
 for role in ('haze','accent'):
  target=a/'particles'/f'{spell}_{role}.json';target.parent.mkdir(parents=True,exist_ok=True)
  target.write_text(json.dumps({'textures':[f'mushokutenseiorigin:pixelfx/{family}/{role}_{i:02}' for i in range(24)]}),encoding='utf-8')
