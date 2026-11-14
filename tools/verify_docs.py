"""Validate active documentation ownership and links; never reads archive as instructions."""
from pathlib import Path
from urllib.parse import unquote
import re,json,sys
p=Path(__file__).resolve().parents[1];d=p/'docs';errors=[]
ids={r['id'] for r in json.loads((p/'src/test/resources/architecture/spell-baseline.json').read_text(encoding='utf-8'))['spells']}
cards={f.stem for f in (d/'spec/magic/spells').glob('*.md')}
if cards!=ids:errors.append(f'spell card mismatch: {cards^ids}')
rows=set(re.findall(r'^\| `([a-z_]+)` \|',(d/'spec/learning/CURRICULUM.md').read_text(encoding='utf-8'),re.M))
if rows!=ids:errors.append('learning ID relationship mismatch')
for f in d.rglob('*.md'):
 if f.is_relative_to(d/'archive') or f.is_relative_to(d/'logs'):continue
 t=f.read_text(encoding='utf-8')
 for url in re.findall(r'\]\(([^\n)]+)\)',t):
  path=url.partition('#')[0].strip('<>')
  if path and not re.match(r'^[a-zA-Z]+:',path) and not (f.parent/unquote(path)).exists():errors.append(f'{f.relative_to(d)}: broken {url}')
 if f.is_relative_to(d/'spec') and re.search(r'^#{1,4} .*?(当前进度|开发进度|最新实现|实现记录|已执行验证)',t,re.M):errors.append(f'{f.relative_to(d)}: progress heading in spec')
 if f.parent==d/'spec/magic/spells' and '等级 / 学习前置' in t:errors.append(f'{f.name}: learning owned by spell')
print(json.dumps({'spell_cards':len(cards),'curriculum_ids':len(rows),'errors':errors},ensure_ascii=False,indent=2))
sys.exit(bool(errors))
