# Rebuild shared pixel frames

Original source: https://github.com/patch-gg/pixelfx at `044e12a45dc9cc312cec50ee606dfc98afe5adc2` (MIT).
Godot bake/cache design reference: https://github.com/winkidney/pixelfx-godot at `babe913bc8ee95536320668d2c69996dbe260926` (MIT).

Requires Node.js and Python with Pillow. Obtain the original source separately, then run from repository root:

```shell
node tools/pixelfx/bake.mjs <pixelfx-source-checkout> <temporary-raw-output>
python tools/pixelfx/export.py <temporary-raw-output> .
```

Keep the temporary raw frames and source checkouts outside the repository. Packed license texts are in src/main/resources/META-INF/licenses. Particle references are shared by family; geometry and gameplay definitions are not regenerated.
