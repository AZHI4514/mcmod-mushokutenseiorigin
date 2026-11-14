package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.*;
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

/** UUID-bound finite summons. Unloaded companions are never replaced with a fresh copy. */
public final class SummonRuntimeAdapter {
	static final AttachmentType<String> OWNER=AttachmentRegistry.create(ExampleMod.id("summon_owner"),b->b.initializer(()->"").persistent(Codec.STRING));
	static final AttachmentType<String> KIND=AttachmentRegistry.create(ExampleMod.id("summon_kind"),b->b.initializer(()->"").persistent(Codec.STRING));
	static final AttachmentType<Long> EXPIRY=AttachmentRegistry.create(ExampleMod.id("summon_expiry"),b->b.initializer(()->0L).persistent(Codec.LONG));
	static final AttachmentType<Long> NEXT=AttachmentRegistry.create(ExampleMod.id("summon_next"),b->b.initializer(()->0L).persistent(Codec.LONG));
	static final AttachmentType<Integer> COMMAND=AttachmentRegistry.create(ExampleMod.id("summon_command"),b->b.initializer(()->0).persistent(Codec.INT));
	static final AttachmentType<String> TARGET=AttachmentRegistry.create(ExampleMod.id("summon_target"),b->b.initializer(()->"").persistent(Codec.STRING));
	static final AttachmentType<Map<String,String>> SLOTS=AttachmentRegistry.create(ExampleMod.id("summon_slots"),b->b.initializer(()->Map.<String,String>of()).persistent(Codec.unboundedMap(Codec.STRING,Codec.STRING)).copyOnDeath());
	static final AttachmentType<Map<String,Long>> SLOT_END=AttachmentRegistry.create(ExampleMod.id("summon_slot_expiry"),b->b.initializer(()->Map.<String,Long>of()).persistent(Codec.unboundedMap(Codec.STRING,Codec.LONG)).copyOnDeath());



	public static boolean light(Entity entity){return entity.getAttachedOrElse(KIND,"").equals("lamplight_spirits");}
	public static boolean summoned(Entity entity){return !entity.getAttachedOrElse(OWNER,"").isEmpty();}
	public static void initialize(){
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity,source,amount)->!light(entity));
		net.fabricmc.fabric.api.event.player.UseEntityCallback.EVENT.register((player,world,hand,entity,hit)->summoned(entity)||MagicCores.core(entity)?net.minecraft.world.InteractionResult.FAIL:net.minecraft.world.InteractionResult.PASS);
		SummonCorpseRepository.initialize();
		CommandRegistrationCallback.EVENT.register((dispatcher,registry,environment)->{
			var root=Commands.literal("mtsummon");
			for(int i=0;i<5;i++){int mode=i;root.then(Commands.literal(new String[]{"follow","stay","attack","protect","stop"}[i]).executes(c->command(c.getSource().getPlayerOrException(),mode)));}
			dispatcher.register(root);
		});
	}
	private static int command(ServerPlayer owner,int mode){
		var level=(ServerLevel)owner.level();LivingEntity target=mode==2?SupportRayAdapter.trace(owner,new MagicAim(owner.getYRot(),owner.getXRot()),32,t->MagicEffects.allowed(owner,t)&&!SupportConsentAdapter.consented(owner,t)):null;
		if(mode==2&&target==null)return 0;
		var slots=new HashMap<>(owner.getAttachedOrCreate(SLOTS));int changed=0;
		for(var id:slots.values()){
			Entity e;try{e=level.getEntity(UUID.fromString(id));}catch(IllegalArgumentException ex){continue;}
			if(e==null)continue;if(mode==4){e.discard();changed++;continue;}
			e.setAttached(COMMAND,mode);e.setAttached(TARGET,target==null?"":target.getUUID().toString());changed++;
		}
		if(mode==4){owner.setAttached(SLOTS,Map.of());LightLeases.release(owner);}
		return changed;
	}
	public static void tickOwner(ServerPlayer owner){
		var map=new HashMap<>(owner.getAttachedOrCreate(SLOTS));var level=(ServerLevel)owner.level();boolean changed=false;var ends=new HashMap<>(owner.getAttachedOrCreate(SLOT_END));
		for(var entry:new ArrayList<>(map.entrySet())){
			if(ends.getOrDefault(entry.getKey(),0L)<=level.getGameTime()||!owner.isAlive()){map.remove(entry.getKey());ends.remove(entry.getKey());changed=true;continue;}
			Entity entity;try{entity=level.getEntity(UUID.fromString(entry.getValue()));}catch(IllegalArgumentException e){map.remove(entry.getKey());changed=true;continue;}
			if(entity!=null&&(!entity.isAlive()||entity.isRemoved()||!owner.isAlive())){entity.discard();map.remove(entry.getKey());changed=true;}
		}
		if(changed){owner.setAttached(SLOTS,Map.copyOf(map));owner.setAttached(SLOT_END,Map.copyOf(ends));}
	}
	public static void tick(Mob mob){
		if(!(mob.level() instanceof ServerLevel level)||!summoned(mob))return;
		ServerPlayer owner;try{owner=level.getServer().getPlayerList().getPlayer(UUID.fromString(mob.getAttachedOrCreate(OWNER)));}catch(IllegalArgumentException ex){mob.discard();return;}
		String kind=mob.getAttachedOrCreate(KIND);
		String visual=kind.startsWith("necromancy")?"necromancy":kind;if(!mob.getAttachedOrElse(MagicAttachments.VISUAL_SPELL,"").equals(visual))mob.setAttached(MagicAttachments.VISUAL_SPELL,visual);
		com.example.examplemod.magic.mechanic.summon.SummonTick.tick(new com.example.examplemod.magic.mechanic.summon.SummonTick.World(){
			public boolean ownerPresent(){return owner!=null;}public boolean sameLevel(){return owner.level()==level;}public boolean ownerAlive(){return owner.isAlive();}public boolean summonAlive(){return mob.isAlive();}public boolean expired(){return level.getGameTime()>=mob.getAttachedOrCreate(EXPIRY);}public boolean slotMatches(){return mob.getUUID().toString().equals(owner.getAttachedOrCreate(SLOTS).get(kind));}
			public void discard(){mob.discard();}public boolean pulseDue(){return level.getGameTime()>=mob.getAttachedOrCreate(NEXT);}public void schedule(){mob.setAttached(NEXT,level.getGameTime()+20);}
			public boolean pay(){return SummonUpkeepAdapter.payUpkeep(level,mob,owner,kind);}public void act(boolean pulse){SummonRuntimeAdapter.act(level,mob,owner,kind,pulse);}
		});
	}
	private static final SummonBehavior MONSTER_BEHAVIOR=new GroundSummonBehavior(SpellDefinition.MONSTER_SUMMON);
	private static final SummonBehavior NECROMANCY_BEHAVIOR=new GroundSummonBehavior(SpellDefinition.NECROMANCY);
	private static final Map<String,SummonBehavior> BEHAVIORS=Map.of(
		"monster_summon",MONSTER_BEHAVIOR,"spirit_summon",new SpiritSummonBehavior(),"lamplight_spirits",new LamplightSummonBehavior());
	private static void act(ServerLevel level,Mob mob,ServerPlayer owner,String kind,boolean pulse){
		var behavior=kind.startsWith("necromancy")?NECROMANCY_BEHAVIOR:BEHAVIORS.getOrDefault(kind,MONSTER_BEHAVIOR);
		behavior.act(level,mob,owner,pulse);
	}
	public static boolean canCast(ServerPlayer owner,SpellDefinition spell,MagicAim aim){return SummonSpawnAdapter.canCast(owner,spell,aim);}
	public static boolean cast(ServerPlayer owner,SpellDefinition spell,MagicAim aim){return SummonSpawnAdapter.cast(owner,spell,aim);}
	public static float protect(ServerPlayer player,net.minecraft.world.damagesource.DamageSource source,float amount){return SpiritProtectionAdapter.protect(player,source,amount);}
	private SummonRuntimeAdapter(){}
}
