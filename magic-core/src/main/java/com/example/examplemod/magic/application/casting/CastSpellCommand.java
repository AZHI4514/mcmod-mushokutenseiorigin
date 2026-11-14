package com.example.examplemod.magic.application.casting;

import java.util.List;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.mechanic.projectile.*;
import com.example.examplemod.magic.domain.model.Vec3d;

/** One validated release request, once only; synchronous server-thread commit. */
public final class CastSpellCommand {
	public enum Result { SUCCESS, REJECTED, SPAWN_FAILED, ALREADY_EXECUTED }
	private final ProjectileSpec spec;
	private final ProjectileBehavior behavior;
	private final PlayerStore players;
	private final ProjectileSpawner spawner;
	private final List<VfxObserver> observers;
	private final java.util.function.Consumer<RuntimeException> observerError;
	private boolean executed;
	public CastSpellCommand(ProjectileSpec spec, ProjectileBehavior behavior, PlayerStore players, ProjectileSpawner spawner,
			List<VfxObserver> observers, java.util.function.Consumer<RuntimeException> observerError) {
		this.spec = spec; this.behavior = behavior; this.players = players; this.spawner = spawner;
		this.observers = List.copyOf(observers); this.observerError = observerError;
	}
	public Result execute(Vec3d origin, float yaw, float pitch) {
		if (executed) return Result.ALREADY_EXECUTED;
		executed = true;
		var before = players.read();
		if (!before.canCast(spec.id().value(), spec.mana())) return Result.REJECTED;
		final ProjectileSpawnPlan plan;
		try { plan = behavior.prepare(origin, yaw, pitch); }
		catch (IllegalArgumentException e) { return Result.REJECTED; }
		players.write(before.cast(spec.id().value(), spec.mana(), spec.recovery()));
		boolean spawned;
		try { spawned = spawner.spawn(plan); }
		catch (RuntimeException error) { players.write(before); throw error; }
		if (!spawned) { players.write(before); return Result.SPAWN_FAILED; }
		for (var observer : observers) {
			try { observer.released(plan); }
			catch (RuntimeException error) { observerError.accept(error); }
		}
		return Result.SUCCESS;
	}
}
