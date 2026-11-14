每次开发先读 `docs/README.md`、`docs/STATUS.md` 和 `docs/MAGIC_DEVELOPMENT.md`，再按入口选读本次任务相关规格。R00—R10架构重构已完成；当前架构见 `docs/ARCHITECTURE.md`。默认禁止加载 `docs/archive/` 全集或恢复历史任务卡；仅在追溯指定证据时按需读取。结束时更新唯一进度 `docs/STATUS.md` 及受影响正式规格。不启动游戏，不自动Git操作。

# 新增物品的统一约定

开始新增或修改物品前，先阅读 `docs/ADDING_ITEMS.md`，复制其中模板。

- 注册统一放 `ModItems`，采用 `register("id", XxxItem::new, XxxItem.createProperties())` 的多行声明。
- 单个物品属性及行为放 `item/XxxItem.java`。不要为新物品另建一套注册器。
- 新物品 ID 使用 snake_case，Java 常量使用 UPPER_SNAKE_CASE，类名使用 UpperCamelCase。
- 通用代码只放 `src/main`；客户端渲染只放 `src/client`。
- 静态自定义头部装备复用 `BlockbenchArmorRenderer`，统一从 `ModArmorRenderers` 注册。
- 同步提供物品模型入口、图标模型、PNG 和中英文语言键；仅在有需求时添加配方。
- Java 用 Tab 缩进、同行大括号、UTF-8、LF；遵守 `.editorconfig`。工具生成的网格 JSON 可压缩，不手写顶点。
- Blockbench 源文件放 `model-source/<id>/`，用 `tools/export_blockbench_equipment.py` 导出，不在运行时代码里引用本机绝对路径。
- 编写后运行 `gradlew.bat build`，说明实际验证范围。未经用户要求不自动启动 Minecraft、不提交 Git。
- 保留用户已有改动，禁止为了添加一件物品重构或删除其他物品。
- 新增或修改法术前阅读 `docs/MAGIC_DEVELOPMENT.md`，复用其中服务端校验、状态持久化、输入打断与验收模板；运行 `gradlew.bat build`（包含 `verifyMagic`）。
