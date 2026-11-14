package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.summon.lamplight_spirits.LamplightUpkeep;
import com.example.examplemod.magic.spell.special.necromancy.NecromancyRules;
import com.example.examplemod.magic.spell.summon.spirit_summon.SpiritSummonRules;
import com.example.examplemod.magic.spell.summon.monster_summon.MonsterSummonRules;

import java.util.*;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import com.example.examplemod.ExampleMod;

import static com.example.examplemod.infrastructure.fabric.magic.SummonRuntimeAdapter.*;
final class SummonUpkeepAdapter {
	private static final AttachmentType<Long> NECRO_PAID_UNTIL=AttachmentRegistry.create(ExampleMod.id("necro_paid_until"),b->b.initializer(()->0L).persistent(Codec.LONG));
	private static final AttachmentType<Integer> LIGHT_DEBT=AttachmentRegistry.create(ExampleMod.id("light_debt"),b->b.initializer(()->0).persistent(Codec.intRange(0,4)).copyOnDeath());
	private interface FeePolicy {
		int initial(ServerPlayer owner,long now);
		int pulse(ServerPlayer owner,long now);
		default void beforeInitialPayment(ServerPlayer owner,long now,int fee){}
		default void afterInitialPayment(ServerPlayer owner){}
	}
	private record FixedFee(int fee) implements FeePolicy {
		public int initial(ServerPlayer owner,long now){return fee;}
		public int pulse(ServerPlayer owner,long now){return fee;}
	}
	private static final class LamplightFee implements FeePolicy {
		public int initial(ServerPlayer owner,long now){return 1;}
		public int pulse(ServerPlayer owner,long now){
			var charge=LamplightUpkeep.next(owner.getAttachedOrCreate(LIGHT_DEBT));
			owner.setAttached(LIGHT_DEBT,charge.credit());return charge.fee();
		}
		public void afterInitialPayment(ServerPlayer owner){owner.setAttached(LIGHT_DEBT,4);}
	}
	private static final class NecromancyFee implements FeePolicy {
		public int initial(ServerPlayer owner,long now){return NecromancyRules.initialFee(owner.getAttachedOrCreate(NECRO_PAID_UNTIL),now);}
		public int pulse(ServerPlayer owner,long now){
			if(owner.getAttachedOrCreate(NECRO_PAID_UNTIL)>now)return 0;
			if(MagicSystem.state(owner).mana()>=2)owner.setAttached(NECRO_PAID_UNTIL,now+20);
			return 2;
		}
		public void beforeInitialPayment(ServerPlayer owner,long now,int fee){if(fee>0)owner.setAttached(NECRO_PAID_UNTIL,now+20);}
	}
	private static final FeePolicy MONSTER=new FixedFee(1),SPIRIT=new FixedFee(2),LIGHT=new LamplightFee(),NECRO=new NecromancyFee();
	private static final Map<SpellDefinition,FeePolicy> FEES=Map.of(SpellDefinition.MONSTER_SUMMON,MONSTER,SpellDefinition.SPIRIT_SUMMON,SPIRIT,SpellDefinition.LAMPLIGHT_SPIRITS,LIGHT,SpellDefinition.NECROMANCY,NECRO);
	static int initial(ServerPlayer owner,SpellDefinition spell,long now){return FEES.getOrDefault(spell,SPIRIT).initial(owner,now);}
	static void commitInitial(ServerPlayer owner,SpellDefinition spell,long now,int fee){
		var policy=FEES.getOrDefault(spell,SPIRIT);policy.beforeInitialPayment(owner,now,fee);
		owner.setAttached(MagicAttachments.STATE,MagicSystem.state(owner).spendMana(fee));policy.afterInitialPayment(owner);
	}
	static boolean payUpkeep(ServerLevel level,Mob mob,ServerPlayer owner,String kind){
		var policy=kind.startsWith("necromancy")?NECRO:kind.equals("lamplight_spirits")?LIGHT:kind.equals("spirit_summon")?SPIRIT:MONSTER;
		int fee=policy.pulse(owner,level.getGameTime());var mana=MagicSystem.state(owner);
		if(mana.mana()<fee)return false;owner.setAttached(MagicAttachments.STATE,mana.spendMana(fee));return true;
	}
	private SummonUpkeepAdapter(){}
}
