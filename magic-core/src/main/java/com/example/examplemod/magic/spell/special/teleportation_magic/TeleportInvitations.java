package com.example.examplemod.magic.spell.special.teleportation_magic;
import java.util.*;
public final class TeleportInvitations {
	public record Invitation(UUID caster,long expiry,boolean accepted) { }
	private final Map<UUID,Invitation> invitations=new HashMap<>();
	public Invitation get(UUID recipient){return invitations.get(recipient);}public void put(UUID recipient,Invitation invitation){invitations.put(recipient,invitation);}public void remove(UUID recipient){invitations.remove(recipient);}
}
