package com.example.examplemod.magic.application.casting;
import java.util.List;
import java.util.function.Consumer;
import com.example.examplemod.magic.application.port.*;
/** Single-use release with explicit no-effect rollback and post-commit observers. */
public final class CastSelfEffectCommand {
	public enum Result { SUCCESS, REJECTED, ALREADY_EXECUTED }
	private final String id;private final int mana,recovery;
	private final PlayerStore players;private final SelfEffect effect;
	private final List<Runnable> observers;private final Consumer<RuntimeException> errors;
	private boolean executed;
	public CastSelfEffectCommand(String id,int mana,int recovery,PlayerStore players,SelfEffect effect,List<Runnable> observers,Consumer<RuntimeException> errors) {
		this.id=id;this.mana=mana;this.recovery=recovery;this.players=players;this.effect=effect;this.observers=List.copyOf(observers);this.errors=errors;
	}
	public Result execute(float yaw,float pitch) { return execute(yaw,pitch,List.of()); }
	/** Required committed work runs before release observers; failed/no-effect releases never consume it. */
	public Result execute(float yaw,float pitch,List<Runnable> committedWork) {
		if(executed)return Result.ALREADY_EXECUTED;executed=true;
		var before=players.read();
		if(!Float.isFinite(yaw)||!Float.isFinite(pitch)||pitch < -90||pitch >90||!before.canCast(id,mana))return Result.REJECTED;
		players.write(before.cast(id,mana,recovery));
		// Unknown partial world exceptions retain payment; only explicit false is reversible.
		if(!effect.apply(yaw,pitch)) { players.write(before);return Result.REJECTED; }
		for(var work:committedWork)work.run();
		for(var observer:observers)try { observer.run(); } catch(RuntimeException error) { errors.accept(error); }
		return Result.SUCCESS;
	}
}
