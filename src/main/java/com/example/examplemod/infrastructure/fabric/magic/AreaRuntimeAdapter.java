package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.PoweredBarrierRuntimeAdapter;

import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.field.AreaRegistry;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.phys.Vec3;

/** 同一玩家同一法术仅一个实例；切换不清除，松开只终止连续续期资格。 */
public final class AreaRuntimeAdapter {
	private static final AttachmentType<AreaRegistry<MagicAreaEntity>> RUNTIME=AttachmentRegistry.create(
			com.example.examplemod.ExampleMod.id("area_runtime"),builder->builder.initializer(AreaRegistry<MagicAreaEntity>::new));
	private static AreaRegistry<MagicAreaEntity> active(MinecraftServer server){return server.overworld().getAttachedOrCreate(RUNTIME);}
	public static void initialize() {
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((target, source, amount) -> {
			return MagicShieldAdapter.allowDamage(target,source,amount,()->{
				var spells=active(target.level().getServer()).get(target.getUUID());return spells==null?null:spells.get("magic_shield");
			});
		});
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			active(server).prune(Entity::isRemoved);
		});
	}
	public static boolean isMagic(Projectile projectile) {
		return projectile instanceof WaterBallEntity || projectile instanceof FlameSliceEntity || projectile instanceof MagicAreaEntity || projectile instanceof SweptSpellProjectile || projectile instanceof DirectionalSpellCarrier;
	}
	public static boolean cast(ServerPlayer owner, SpellDefinition spell, MagicAim aim) {
		var entries = active(owner.level().getServer()).entries(owner.getUUID());
		var old = entries.get(spell.id());
		if (old != null && !old.isRemoved() && old.level() == owner.level() && old.canRenew(owner) && spell != SpellDefinition.BLIZZARD_STORM) { old.renew(); return true; }
		var area = new MagicAreaEntity(MagicSystem.MAGIC_AREA, owner.level()); area.configure(owner, spell, aim);
		if (!owner.level().addFreshEntity(area)) return false;
		if (old != null) old.discard(); entries.put(spell.id(), area); area.startExpansion(); return true;
	}
	public static boolean replaceExpansion(ServerPlayer owner, MagicAreaEntity area) {
		if(!owner.level().addFreshEntity(area))return false;
		var entries=active(owner.level().getServer()).entries(owner.getUUID());
		var old=entries.put(area.spell().id(),area);if(old!=null)old.discard();return true;
	}
	public static boolean adoptExpansion(ServerPlayer owner, MagicAreaEntity area) {
		var entries = active(owner.level().getServer()).entries(owner.getUUID());
		var old = entries.get(area.spell().id());
		if (old != null && old != area && !old.isRemoved()) return false;
		entries.put(area.spell().id(),area);
		return true;
	}
	public static void breakMaintenance(ServerPlayer owner) {
		var entries = active(owner.level().getServer()).get(owner.getUUID()); if (entries != null) entries.values().forEach(MagicAreaEntity::breakMaintenance);
	}
	public static void clear(ServerPlayer owner){active(owner.level().getServer()).clear(owner.getUUID(),Entity::discard);}
	public static void clearAll(MinecraftServer server){active(server).clearAll(Entity::discard);server.overworld().removeAttached(RUNTIME);}
	/** 在ServerLevel的整个弹体tick之前检测；不凭水幕附近就吞弹。 */
	public static boolean intercept(Projectile projectile) {
		if(PoweredBarrierRuntimeAdapter.intercept(projectile))return true;
		if (com.example.examplemod.infrastructure.fabric.magic.FiniteBarrierRuntimeAdapter.intercept(projectile)) return true;
		if (PlayerSpellRuntime.intercept(projectile)) return true;
		return WaterShieldCollisionAdapter.intercept(projectile,()->()->active(projectile.level().getServer()).values().stream().flatMap(entries->entries.values().stream()).iterator());
	}

	private AreaRuntimeAdapter() { }
}
