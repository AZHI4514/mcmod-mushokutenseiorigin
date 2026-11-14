package com.example.examplemod.magic.application.casting;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.domain.casting.CastingSession;
import com.example.examplemod.magic.spell.fire.flamethrower.FlamethrowerSpec;
/** Per-use channel. Runtime registers successful entry before invoking its initial emission. */
public final class FlamethrowerChannel {
	private final PlayerStore players;
	private final ChannelPayment payment;
	private final FlameStream stream;
	private final Runnable sustain;
	private CastingSession phase=CastingSession.channeling();
	private boolean active;
	public FlamethrowerChannel(PlayerStore players,ChannelDebt debt,FlameStream stream,Runnable sustain) {
		this.players=players;payment=new ChannelPayment(players,debt);this.stream=stream;this.sustain=sustain;
	}
	private static boolean valid(float yaw,float pitch) {
		return Float.isFinite(yaw)&&Float.isFinite(pitch)&&pitch>=-90&&pitch<=90;
	}
	/** Payment only: registration, first emission and release sound retain runtime order. */
	public boolean begin(float yaw,float pitch) {
		if(!valid(yaw,pitch))return false;
		if(active){heartbeat();return true;}
		if(!players.read().canCast("flamethrower",1)||!payment.pay(FlamethrowerSpec.MANA_PER_SECOND))return false;
		phase=CastingSession.channeling();active=true;return true;
	}
	public void heartbeat() { if(active)phase.heartbeat(); }
	public void emit(float yaw,float pitch) {
		if(!active||!valid(yaw,pitch))throw new IllegalStateException("Inactive or invalid flame emission");
		sustain.run();stream.emit(yaw,pitch);
	}
	/** True means the active channel ended this tick, preserving the input-blocking signal. */
	public boolean tick(boolean eligible,float yaw,float pitch) {
		if(!active)return false;
		if(phase.tick(eligible).signal()==CastingSession.Signal.CANCELLED||!valid(yaw,pitch)) { stop();return true; }
		if(!payment.pay(FlamethrowerSpec.MANA_PER_SECOND)) { stop();return true; }
		emit(yaw,pitch);return false;
	}
	public boolean stop() {
		if(!active)return false;
		active=false;phase.cancel();var state=players.read();
		players.write(state.withCooldown(Math.max(state.cooldown(),FlamethrowerSpec.RECOVERY)));return true;
	}
}
