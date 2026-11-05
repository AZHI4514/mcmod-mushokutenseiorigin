package com.example.examplemod.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.equipment.EquipmentAssets;

import com.example.examplemod.ExampleMod;

/** 洛琪希的圣遗物：头部装备，护甲值 1，永久不损坏。 */
public final class RoxyPantsuItem extends Item {

	public RoxyPantsuItem(Properties properties) {
		super(properties);
	}

	/** 物品属性集中在所属物品类中；不在注册入口编写行为。 */
	public static Properties createProperties() {
		return new Properties()
				.stacksTo(1)
				.component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
				.component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD)
						.setAsset(ResourceKey.create(EquipmentAssets.ROOT_ID, ExampleMod.id("roxy_pantsu")))
						.setEquipSound(SoundEvents.ARMOR_EQUIP_LEATHER)
						.setDamageOnHurt(false)
						.build())
				.attributes(ItemAttributeModifiers.builder()
						.add(Attributes.ARMOR, new AttributeModifier(
								ExampleMod.id("roxy_pantsu_armor"), 1.0,
								AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD)
						.build());
	}
}
