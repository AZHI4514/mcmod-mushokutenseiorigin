package com.example.examplemod.magic;

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
public final class MagicAreas {
	private static final Map<UUID, Map<String, MagicAreaEntity>> ACTIVE = new HashMap<>();
	public static void initialize() {
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((target, source, amount) -> {
			if (!(source.getDirectEntity() instanceof Projectile projectile) || !isMagic(projectile)) return true;
			var spells = ACTIVE.get(target.getUUID());
			var shield = spells == null ? null : spells.get("magic_shield");
			if (shield == null || shield.isRemoved() || shield.level() != target.level()) return true;
			shield.absorb(amount); return false;
		});
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			ACTIVE.values().forEach(map -> map.values().removeIf(Entity::isRemoved));
			ACTIVE.values().removeIf(Map::isEmpty);
		});
	}
	public static boolean isMagic(Projectile projectile) {
		return projectile instanceof WaterBallEntity || projectile instanceof FlameSliceEntity || projectile instanceof MagicAreaEntity;
	}
	public static boolean cast(ServerPlayer owner, SpellDefinition spell, MagicAim aim) {
		var entries = ACTIVE.computeIfAbsent(owner.getUUID(), id -> new HashMap<>());
		var old = entries.get(spell.id());
		if (old != null && !old.isRemoved() && old.level() == owner.level() && old.canRenew(owner)) { old.renew(); return true; }
		var area = new MagicAreaEntity(MagicSystem.MAGIC_AREA, owner.level()); area.configure(owner, spell, aim);
		if (!owner.level().addFreshEntity(area)) return false;
		if (old != null) old.discard(); entries.put(spell.id(), area); return true;
	}
	public static void breakMaintenance(ServerPlayer owner) {
		var entries = ACTIVE.get(owner.getUUID()); if (entries != null) entries.values().forEach(MagicAreaEntity::breakMaintenance);
	}
	public static void clear(UUID owner) {
		var entries = ACTIVE.remove(owner); if (entries != null) entries.values().forEach(Entity::discard);
	}
	public static void clearAll() { ACTIVE.values().forEach(map -> map.values().forEach(Entity::discard)); ACTIVE.clear(); }
	/** 在ServerLevel的整个弹体tick之前检测；不凭水幕附近就吞弹。 */
	public static boolean intercept(Projectile projectile) {
		if (projectile instanceof MagicAreaEntity || projectile instanceof net.minecraft.world.entity.projectile.FishingHook
				|| projectile.isRemoved() || projectile.getDeltaMovement().lengthSqr() < 1.0E-8) return false;
		double first = Double.POSITIVE_INFINITY; MagicAreaEntity selected = null;
		var movement = projectile.getDeltaMovement(); double distance = movement.length();
		for (var entries : ACTIVE.values()) for (var area : entries.values()) {
			if (area.isRemoved() || area.level() != projectile.level() || area.spell() != SpellDefinition.WATER_SHIELD
					|| projectile.getOwner() == area.getOwner()) continue;
			var normal = Vec3.directionFromRotation(0, area.getYRot());
			if (movement.dot(normal) >= 0) continue;
			double contact = new MagicWallVolume(normal, area.getYRot()).contact(
					projectile.getBoundingBox(), area.position().add(0,1,0), movement.scale(-1 / distance), distance);
			if (contact < first) { first = contact; selected = area; }
		}
		if (selected == null) return false;
		var level = projectile.level();
		var end = projectile.position().add(movement.scale(first / distance));
		var block = level.clip(new net.minecraft.world.level.ClipContext(projectile.position(), end,
				net.minecraft.world.level.ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, projectile));
		if (block.getType() != net.minecraft.world.phys.HitResult.Type.MISS) return false;
		float damage = MagicProjectileDamage.incoming(projectile, selected);
		selected.absorb(damage);
		if (projectile instanceof ThrownTrident) {
			// 不销毁玩家三叉戟，挡回使其仍可拾取/忠诚返回。
			projectile.setPos(end.subtract(movement.normalize().scale(0.1))); projectile.setDeltaMovement(movement.scale(-0.1));
		} else projectile.discard();
		return true;
	}
	private MagicAreas() { }
}
