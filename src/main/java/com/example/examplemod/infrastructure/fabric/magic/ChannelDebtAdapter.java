package com.example.examplemod.infrastructure.fabric.magic;
import net.minecraft.server.level.ServerPlayer;
import com.example.examplemod.magic.PlayerSpellRuntime;
import com.example.examplemod.magic.application.port.ChannelDebt;
public record ChannelDebtAdapter(ServerPlayer player) implements ChannelDebt {
	@Override public int read() { return player.getAttachedOrElse(PlayerSpellRuntime.CHANNEL_DEBT,0); }
	@Override public void write(int debt) { player.setAttached(PlayerSpellRuntime.CHANNEL_DEBT,debt); }
}
