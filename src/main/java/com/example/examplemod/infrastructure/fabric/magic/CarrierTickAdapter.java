package com.example.examplemod.infrastructure.fabric.magic;

import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.projectile.CarrierServerTick;
import com.example.examplemod.magic.mechanic.projectile.PiercingFlightRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

/** Server dispatch adapter; private entity state is changed only through narrow callbacks. */
public final class CarrierTickAdapter implements CarrierServerTick.World {
	private final DirectionalSpellCarrier carrier;
	private final ServerLevel level;
	private final int age, remaining;
	private final Vec3 anchor;
	private final DoubleSupplier speed;
	private final Runnable advanceAge, breakMaintenance;
	private final Consumer<ServerPlayer> stationaryTick, fall, fly;
	private ServerPlayer owner;

	public CarrierTickAdapter(DirectionalSpellCarrier carrier, ServerLevel level, int age, int remaining, Vec3 anchor,
			DoubleSupplier speed, Runnable advanceAge, Runnable breakMaintenance,
			Consumer<ServerPlayer> stationaryTick, Consumer<ServerPlayer> fall, Consumer<ServerPlayer> fly) {
		this.carrier=carrier; this.level=level; this.age=age; this.remaining=remaining; this.anchor=anchor;
		this.speed=speed; this.advanceAge=advanceAge; this.breakMaintenance=breakMaintenance;
		this.stationaryTick=stationaryTick; this.fall=fall; this.fly=fly;
	}
	public boolean belowMinimum() { return carrier.getY()<level.getMinY(); }
	public void discard() { carrier.discard(); }
	public boolean loadedCurrent() { return SpellRaycastAdapter.loaded(level,carrier.getBoundingBox()); }
	public void resolveOwner() { owner=carrier.getOwner() instanceof ServerPlayer player?player:null; }
	public boolean stationary() { return carrier.stationary(); }
	public boolean hasOwner() { return owner!=null; }
	public boolean loadedAhead() { return SpellRaycastAdapter.loaded(level,carrier.getBoundingBox().expandTowards(carrier.direction().scale(speed.getAsDouble()))); }
	public boolean expired() {
		return !carrier.fallingRock()&&((carrier.spell()==SpellDefinition.ICICLE_BREAK||carrier.spell()==SpellDefinition.ICICLE_EDGE||carrier.spell()==SpellDefinition.FLOOD_FLUSH||carrier.spell()==SpellDefinition.EXPLOSION||carrier.spell()==SpellDefinition.WIND_SLICE)?PiercingFlightRules.expired(age,remaining):age>=remaining);
	}
	public void advanceAge() { advanceAge.run(); }
	public boolean shield() { return carrier.shield(); }
	public void tickShield() { if(owner.position().distanceToSqr(anchor)>1.0E-6) breakMaintenance.run(); }
	public void tickStationary() { stationaryTick.accept(owner); }
	public boolean fallingRock() { return carrier.fallingRock(); }
	public void fall() { fall.accept(owner); }
	public void fly() { fly.accept(owner); }
}
