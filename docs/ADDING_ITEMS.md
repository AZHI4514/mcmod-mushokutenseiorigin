# 物品开发约定（Minecraft 26.1 / Fabric / Java 25）

此文档是本项目新增物品的统一模板。新物品沿用现有包名 `com.example.examplemod`，命名空间固定为 `mushokutenseiorigin`。

## 固定格式

- 文件：UTF-8、LF 换行、文件末尾换行；Java 使用一个 Tab 缩进（显示宽度 4），大括号不另起行。
- 顺序：`package` → Java/第三方依赖 → Minecraft → Fabric → 本项目 imports → 类说明 → 字段 → 构造/方法。
- 资源 ID：`snake_case`，例如 `roxy_pantsu`；Java 常量：`ROXY_PANTSU`；类名：`RoxyPantsuItem`。
- `.editorconfig` 负责编辑器缩进和换行；它不是完整 Java 语法格式化器。复制下方模板保持声明形式一致。
- 工具生成的网格 JSON 允许压缩排版，不手改生成的顶点数据。根目录 `AGENTS.md` 要求后续编码助手先阅读本模板。
- 服务端可用的属性、行为只放 `src/main`；渲染只能放 `src/client`，通用类不得导入客户端类。
- 新功能不清理或改名其他物品，不擅自加入配方、特殊效果或执行 Git 提交。

## 每件物品的文件对应关系

| 位置 | 职责 | 本次示例 |
|---|---|---|
| `src/main/java/com/example/examplemod/item/<Name>Item.java` | 属性与行为 | `RoxyPantsuItem.java` |
| `src/main/java/com/example/examplemod/item/ModItems.java` | 唯一通用注册入口与创造分页 | `ROXY_PANTSU` |
| `src/main/resources/assets/mushokutenseiorigin/items/<id>.json` | 26.1 物品模型入口 | `roxy_pantsu.json` |
| `src/main/resources/assets/mushokutenseiorigin/models/item/<id>.json` | 物品栏/手持/掉落物平面图标模型 | `roxy_pantsu.json` |
| `src/main/resources/assets/mushokutenseiorigin/textures/item/<id>.png` | 透明像素图标 | `roxy_pantsu.png`，32×32 |
| `src/main/resources/assets/mushokutenseiorigin/lang/{zh_cn,en_us}.json` | 显示名称 | `item.mushokutenseiorigin.roxy_pantsu` |

仅自定义头部装备需要以下文件；普通物品不要创建：

| 位置 | 职责 |
|---|---|
| `src/client/java/com/example/examplemod/client/render/ModArmorRenderers.java` | 唯一自定义装备渲染注册入口 |
| `src/client/java/com/example/examplemod/client/render/BlockbenchArmorRenderer.java` | 共用静态网格渲染器，不为每个物品复制一份 |
| `assets/mushokutenseiorigin/equipment/<id>.json` | 装备资源入口，使 26.1 正确提取头部装备渲染状态 |
| `assets/mushokutenseiorigin/models/equipment/<id>.json` | 工具导出的顶点、UV、法线与材质批次；不是 vanilla 模型 JSON |
| `assets/mushokutenseiorigin/textures/entity/equipment/<id>/material_*.png` | 工具导出的实际模型纹理 |
| `assets/mushokutenseiorigin/textures/entity/equipment/humanoid/<id>.png` | 透明原版回退层，实际模型由 Fabric 绘制 |
| `model-source/<id>/pantsu.bbmodel` | 可编辑源文件，不打包到游戏 JAR |

上表 `assets/...` 均位于 `src/main/resources/` 下。

## 新增普通物品：始终复制此模板

```java
package com.example.examplemod.item;

import net.minecraft.world.item.Item;

/** 替换成物品用途说明。 */
public final class YourItem extends Item {

	public YourItem(Properties properties) {
		super(properties);
	}

	public static Properties createProperties() {
		return new Properties()
				.stacksTo(64);
	}
}
```

在 `ModItems` 中添加且只添加一处注册声明：

```java
	public static final Item YOUR_ITEM = register(
			"your_item",
			YourItem::new,
			YourItem.createProperties()
	);
```

在 `ModItems.initialize()` 选择对应创造模式分页，沿用相同形式：

```java
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
				.register(entries -> entries.accept(YOUR_ITEM));
```

禁止在物品构造函数里重复注册，禁止绕过 `ModItems.register()` 手动处理 `setId()`。

创建 `items/your_item.json`：

```json
{
	"model": {
		"type": "minecraft:model",
		"model": "mushokutenseiorigin:item/your_item"
	}
}
```

创建 `models/item/your_item.json`：

```json
{
	"parent": "minecraft:item/generated",
	"textures": {
		"layer0": "mushokutenseiorigin:item/your_item"
	}
}
```

将 PNG 放入 `textures/item/your_item.png`；在两个语言文件中加入 `item.mushokutenseiorigin.your_item`。没有获取配方需求就不创建 recipe。

## 新增同类头部装备

复制 `RoxyPantsuItem.java`，替换类名、装备资源 ID 和属性修饰符 ID，仍通过 `createProperties()` 提供属性。装备属性只影响 `EquipmentSlotGroup.HEAD`。

本次装备不设置 `MAX_DAMAGE`/`DAMAGE`，并设置 `UNBREAKABLE` 和 `damageOnHurt=false`。防御值为 1，未添加韧性、击退抗性、药水效果或配方。

在 `ModArmorRenderers.initialize()` 追加相同结构：

```java
		ArmorRenderer.register(context -> new BlockbenchArmorRenderer(context,
				ExampleMod.id("models/equipment/your_item.json")), ModItems.YOUR_ITEM);
```

当前共用渲染器只用于静态、头部槽位的模型；其他槽位/动画另行实现，不能只替换 ID。

## 修改 roxy_pantsu 模型与佩戴位置

外部原件：`D:\BlockbenchAssets\pantsu\pantsu.bbmodel`。项目保留独立副本 `model-source/roxy_pantsu/pantsu.bbmodel`。

1. 用 Blockbench 修改并保存项目内源文件，或用更新后的外部原件替换该副本。
2. 在项目根目录运行（Python 3，无需第三方库）：

```powershell
python tools/export_blockbench_equipment.py model-source/roxy_pantsu/pantsu.bbmodel --id roxy_pantsu --scale 1.08 1.0 2.08 --offset 0 -4.1 0
```

3. 重新构建，运行中的开发客户端可按 F3+T 重载资源。若更改 Java 代码，则需重启开发客户端。

`scale` 分别为宽、高、深缩放；原模型深度小于玩家头部，因此深度初配为 2.08。`offset` 单位是模型像素；Y 越负越向头顶移动。当前将腰口初配在头部局部 Y=-4.1，面部下半部保持开放。最终贴合效果需游戏里检查，不能把离线构建当作视觉验收。

工具烘焙保存的组/元素旋转、UV 和法线，保留三角/四边形网格，按材质批次导出。当前源工程为 13 个立方体、2 个网格，共 80 个面、3 个使用中的材质批次。没有逐像素堆几何，也没有新增完整模型副本。

工具仅支持静态 cube/mesh、逐面 UV、内嵌 PNG。遇到动画、Box UV、inflate 或不支持的面会报错；不要忽略错误后继续打包。导出结果包含源文件 SHA-256，运行时不访问外部磁盘上的模型。

## 构建与手动测试

```powershell
.\gradlew.bat build
.\gradlew.bat runClient
```

使用 Java 25。开发环境为 Minecraft 26.1；项目原有 Fabric API 版本是 `0.155.3+26.1.2`，本次未变更依赖。

在有权限的世界里执行：

```text
/give @s mushokutenseiorigin:roxy_pantsu
/attribute @s minecraft:armor get
```

- 创造模式「战斗」分页可找到「洛琪希的圣遗物」。图标正常、无紫黑贴图。
- 右键装备或放入头盔槽，最大堆叠为 1。未穿其他护甲时，装备后的护甲属性为 1，取下后为 0。
- F5 检查三角布片、裆布、蝴蝶结，蝴蝶结应朝前；检查没有额外平面图标叠在头上。
- 抬头低头、转头、蹲下、游泳，检查头部跟随和偏移；分别测试普通及纤细皮肤。
- 生存模式受伤后物品不损坏，没有耐久条。没有额外增益、合成配方。
- 按 F3+T，检查资源重载后模型仍正常；存档退出重进检查装备仍在。
- 联机查看需要相关客户端均安装模组，服务器安装对应模组。游戏内视觉与联机效果由实际客户端测试确认。

代码编译和资源检查通过不等于已经完成游戏内视觉测试。此功能不添加新 Mixin，不启动游戏、不提交 Git。
