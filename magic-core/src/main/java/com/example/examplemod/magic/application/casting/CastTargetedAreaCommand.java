package com.example.examplemod.magic.application.casting;
import java.util.List;
import com.example.examplemod.magic.application.port.*;
/** One instantaneous targeted-area release. World effects are not undoable after commit. */
public final class CastTargetedAreaCommand {
	public enum Result { SUCCESS, REJECTED, ALREADY_EXECUTED }
	private final String id;private final int mana,recovery;
	private final PlayerStore players;private final TargetedArea area;private final List<Runnable> observers;
	private final java.util.function.Consumer<RuntimeException> errors;private boolean executed;
	public CastTargetedAreaCommand(String id,int mana,int recovery,PlayerStore players,TargetedArea area,List<Runnable> observers,java.util.function.Consumer<RuntimeException> errors) {
		this.id=id;this.mana=mana;this.recovery=recovery;this.players=players;this.area=area;this.observers=List.copyOf(observers);this.errors=errors;
	}
	public Result execute(float yaw,float pitch) {
		if(executed)return Result.ALREADY_EXECUTED;executed=true;
		var before=players.read();
		if(!Float.isFinite(yaw)||!Float.isFinite(pitch)||pitch < -90||pitch >90||!before.canCast(id,mana))return Result.REJECTED;
		var center=area.locate(yaw,pitch);if(center.isEmpty())return Result.REJECTED;
		players.write(before.cast(id,mana,recovery));
		// Once world application starts, refunding could grant partially applied damage for free.
		area.apply(center.get(),yaw,pitch);
		for(var observer:observers)try { observer.run(); } catch(RuntimeException error) { errors.accept(error); }
		return Result.SUCCESS;
	}
}
