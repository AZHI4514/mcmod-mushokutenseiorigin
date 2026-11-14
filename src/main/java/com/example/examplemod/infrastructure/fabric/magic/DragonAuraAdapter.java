package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.magic.spell.special.saint_dragon_battle_aura.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
/** Independent aura adapter; registrations preserve the original persistent identifiers. */
public record DragonAuraAdapter(ServerPlayer player) implements SelfEffect {
	private static final AttachmentType<Long> AURA_END=AttachmentRegistry.create(ExampleMod.id("aura_end"),b->b.initializer(()->0L).persistent(Codec.LONG));
	private static final AttachmentType<Long> AURA_NEXT=AttachmentRegistry.create(ExampleMod.id("aura_next"),b->b.initializer(()->0L).persistent(Codec.LONG));
	private static final AttachmentType<Long> AURA_READY=AttachmentRegistry.create(ExampleMod.id("aura_ready"),b->b.initializer(()->0L).persistent(Codec.LONG).copyOnDeath());
	private static final net.minecraft.resources.Identifier AURA_KNOCKBACK=ExampleMod.id("dragon_aura_knockback");
	public static boolean canCast(ServerPlayer owner){long now=owner.level().getGameTime();return DragonAuraRules.canCast(now,owner.getAttachedOrElse(AURA_READY,0L),owner.getAttachedOrElse(AURA_END,0L));}
	public static boolean cast(ServerPlayer owner){if(!canCast(owner)||MagicSystem.state(owner).mana()<12)return false;long now=owner.level().getGameTime();owner.setAttached(MagicAttachments.STATE,MagicSystem.state(owner).spendMana(12));owner.setAttached(AURA_END,now+400);owner.setAttached(AURA_NEXT,now+20);owner.setAttached(AURA_READY,now+1000);return true;}
	public boolean apply(float yaw,float pitch){return cast(player);}
	public static void tickOwner(ServerPlayer player){
		long now=player.level().getGameTime(),end=player.getAttachedOrElse(AURA_END,0L);
		var attribute=player.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
		DragonAuraTick.tick(new DragonAuraTick.World(){
			public boolean ended(){return end<=now;}public boolean alive(){return player.isAlive();}public boolean active(){return player.getAttachedOrElse(MagicAttachments.AURA_ACTIVE,false);}
			public void clearActive(){player.setAttached(MagicAttachments.AURA_ACTIVE,false);}public void recovery(){player.setAttached(AURA_READY,now+600);}public void endNow(){player.setAttached(AURA_END,now);}
			public void removeResistance(){if(attribute!=null)attribute.removeModifier(AURA_KNOCKBACK);}public void activate(){player.setAttached(MagicAttachments.AURA_ACTIVE,true);}
			public boolean paymentDue(){return now>=player.getAttachedOrElse(AURA_NEXT,0L);}public void schedule(){player.setAttached(AURA_NEXT,now+20);}
			public boolean pay(){var state=MagicSystem.state(player);if(state.mana()<12)return false;player.setAttached(MagicAttachments.STATE,state.spendMana(12));return true;}
			public void addResistance(){if(attribute!=null&&!attribute.hasModifier(AURA_KNOCKBACK))attribute.addTransientModifier(new AttributeModifier(AURA_KNOCKBACK,.5,AttributeModifier.Operation.ADD_VALUE));}
			public void visual(){if(now%3==0){var level=(ServerLevel)player.level();for(int i=0;i<16;i++){double a=now*.08+i*Math.PI/8;ParticlePacketAdapter.send(level,SpellParticles.accent(SpellDefinition.SAINT_DRAGON_BATTLE_AURA),player.getX()+Math.cos(a)*.55,player.getY()+level.getRandom().nextDouble()*1.8,player.getZ()+Math.sin(a)*.55,1,.02,.03,.02,.02);}}}
		});
	}

	public static float auraDamage(LivingEntity target,net.minecraft.world.damagesource.DamageSource source,float amount){
		if(!(target instanceof ServerPlayer player)||player.getAttachedOrElse(AURA_END,0L)<=player.level().getGameTime()
			||source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)||source.getDirectEntity() instanceof MagicAreaEntity area&&area.spell()==SpellDefinition.ELECTRIC)return amount;
		return DragonAuraRules.reducedDamage(amount);
	}
}
