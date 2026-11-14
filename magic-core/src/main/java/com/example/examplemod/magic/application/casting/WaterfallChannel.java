package com.example.examplemod.magic.application.casting;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.mechanic.channel.ChannelSession;
import com.example.examplemod.magic.domain.casting.CastingSession;
import com.example.examplemod.magic.spell.water.waterfall.WaterfallSpec;
/** One player's ongoing waterfall use case; world queries and water identity belong to WaterSource. */
public final class WaterfallChannel {
	private final PlayerStore players;
	private final ChannelPayment payment;
	private final WaterSource source;
	private final Runnable sustain;
	private ChannelSession cadence=new ChannelSession();
	private CastingSession phase=CastingSession.channeling();
	private boolean active;
	public WaterfallChannel(PlayerStore players,ChannelDebt debt,WaterSource source,Runnable sustain) {
		this.players=players;this.payment=new ChannelPayment(players,debt);this.source=source;this.sustain=sustain;
	}
	private static boolean valid(float yaw,float pitch) {
		return Float.isFinite(yaw)&&Float.isFinite(pitch)&&pitch>=-90&&pitch<=90;
	}
	public boolean begin(float yaw,float pitch) {
		if(!valid(yaw,pitch))return false;
		if(active) { heartbeat();return true; }
		if(!players.read().canCast("waterfall",1))return false;
		if(!payment.begin(WaterfallSpec.MANA_PER_SECOND,()->source.begin(yaw,pitch)))return false;
		phase=CastingSession.channeling();cadence=new ChannelSession();active=true;return true;
	}
	public void heartbeat() { if(active)phase.heartbeat(); }
	/** Returns true only when this tick ends an active channel, matching the input-blocking signal. */
	public boolean tick(boolean eligible,float yaw,float pitch) {
		if(!active)return false;
		if(phase.tick(eligible).signal()==CastingSession.Signal.CANCELLED||!valid(yaw,pitch)||!source.targetAvailable(yaw,pitch)) { stop();return true; }
		if(!payment.pay(WaterfallSpec.MANA_PER_SECOND)) { stop();return true; }
		sustain.run();source.renew();
		if(cadence.generationDue())source.generate(yaw,pitch);
		return false;
	}
	public boolean stop() {
		if(!active)return false;
		active=false;phase.cancel();var state=players.read();
		players.write(state.withCooldown(Math.max(state.cooldown(),WaterfallSpec.RECOVERY)));
		return true;
	}
}
