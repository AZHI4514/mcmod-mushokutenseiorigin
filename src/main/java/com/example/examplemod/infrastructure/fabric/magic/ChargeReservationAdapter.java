package com.example.examplemod.infrastructure.fabric.magic;
import net.minecraft.server.level.ServerPlayer;
import com.example.examplemod.magic.MagicAttachments;
import com.example.examplemod.magic.application.port.ManaReservation;
public record ChargeReservationAdapter(ServerPlayer player) implements ManaReservation {
	public int reserved() { return player.getAttachedOrElse(MagicAttachments.CHARGE_MANA,0); }
	public void write(int paid,int budget) { player.setAttached(MagicAttachments.CHARGE_MANA,paid);player.setAttached(MagicAttachments.CHARGE_BUDGET,budget); }
}
