package com.example.examplemod.magic.application.casting;
import java.util.List;
import java.util.function.Consumer;
import com.example.examplemod.magic.application.port.*;
/** Single-use release with explicit no-effect rollback and post-commit observers. */
public final class CastLocalEffectCommand {
	public enum Result { SUCCESS, REJECTED, ALREADY_EXECUTED }
	private final String id;private final int mana,recovery;
	private final PlayerStore players;private final LocalEffect effect;
	private final List<Runnable> observers;private final Consumer<RuntimeException> errors;
	private boolean executed;
	public CastLocalEffectCommand(String id,int mana,int recovery,PlayerStore players,LocalEffect effect,List<Runnable> observers,Consumer<RuntimeException> errors) {
		this.id=id;this.mana=mana;this.recovery=recovery;this.players=players;this.effect=effect;this.observers=List.copyOf(observers);this.errors=errors;
	}
	public Result execute(float yaw,float pitch) {
		if(executed)return Result.ALREADY_EXECUTED;executed=true;
		var before=players.read();
		if(!Float.isFinite(yaw)||!Float.isFinite(pitch)||pitch < -90||pitch >90||!before.canCast(id,mana))return Result.REJECTED;
		var base=effect.locate(yaw,pitch);if(base.isEmpty())return Result.REJECTED;
		players.write(before.cast(id,mana,recovery));
		// Unknown partial world exceptions retain payment; only explicit false is reversible.
		if(!effect.place(base.get(),yaw,pitch)) { players.write(before);return Result.REJECTED; }
		for(var observer:observers)try { observer.run(); } catch(RuntimeException error) { errors.accept(error); }
		return Result.SUCCESS;
	}
}
