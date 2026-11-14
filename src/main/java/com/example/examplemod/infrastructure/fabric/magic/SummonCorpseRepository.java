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
final class SummonCorpseRepository {
	private static final AttachmentType<List<String>> CORPSES=AttachmentRegistry.create(ExampleMod.id("magic_corpses"),b->b.initializer(()->List.<String>of()).persistent(Codec.STRING.listOf()));
	static void initialize(){
		ServerLivingEntityEvents.AFTER_DEATH.register((entity,source)->{
			if(!(entity instanceof Mob)||entity instanceof net.minecraft.world.entity.player.Player||MagicControl.immune(entity)||summoned(entity))return;
			var level=(ServerLevel)entity.level();var records=new ArrayList<>(level.getAttachedOrCreate(CORPSES));
			records.removeIf(s->expiry(s)<=level.getGameTime());
			if(records.size()>=128)records.removeFirst();
			records.add(entity.getX()+";"+entity.getY()+";"+entity.getZ()+";"+(level.getGameTime()+1200)+";"+entity.getUUID());
			level.setAttached(CORPSES,List.copyOf(records));
		});
	}
	private static long expiry(String s){return NecromancyRules.corpseExpiry(s);}
	private static Vec3 corpsePosition(String s){try{var a=s.split(";");return new Vec3(Double.parseDouble(a[0]),Double.parseDouble(a[1]),Double.parseDouble(a[2]));}catch(RuntimeException e){return null;}}
	static String corpse(ServerPlayer owner,MagicAim aim){
		var level=(ServerLevel)owner.level();
		return level.getAttachedOrCreate(CORPSES).stream().filter(s->expiry(s)>level.getGameTime()).filter(s->{
			var p=corpsePosition(s);return p!=null&&p.distanceToSqr(owner.position())<=256&&aim.direction().dot(p.add(0,.5,0).subtract(owner.getEyePosition()).normalize())>.95
				&&SpellRaycastAdapter.loaded(level,new AABB(owner.getEyePosition(),p))&&level.clip(new ClipContext(owner.getEyePosition(),p.add(0,.2,0),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,owner)).getType()==HitResult.Type.MISS;
		}).min(Comparator.comparingDouble(s->corpsePosition(s).distanceToSqr(owner.position()))).orElse(null);
	}
	static void consume(ServerPlayer owner,MagicAim aim){
		var level=(ServerLevel)owner.level();var records=new ArrayList<>(level.getAttachedOrCreate(CORPSES));
		records.remove(corpse(owner,aim));level.setAttached(CORPSES,List.copyOf(records));
	}

	private SummonCorpseRepository(){}
}
