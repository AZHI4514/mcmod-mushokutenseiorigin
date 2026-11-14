package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.DirectionalSpellCarrier;
import com.example.examplemod.magic.SpellVisuals;
import com.example.examplemod.magic.spell.fire.explosion.ExplosionImpact;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
/** Keeps the original carrier, explosion interaction, fire flag and current contact position. */
public record ExplosionImpactAdapter(DirectionalSpellCarrier carrier,ServerLevel level) implements ExplosionImpact.World {
	@Override public void burst() { SpellVisuals.burst(level,carrier.spell(),carrier.position(),carrier.direction()); }
	@Override public void explode(float power) { level.explode(carrier,carrier.getX(),carrier.getY(),carrier.getZ(),power,true,Level.ExplosionInteraction.TNT); }
	@Override public void discard() { carrier.discard(); }
}
