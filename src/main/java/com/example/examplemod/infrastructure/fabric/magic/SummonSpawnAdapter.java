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
import static com.example.examplemod.infrastructure.fabric.magic.SummonUpkeepAdapter.*;
final class SummonSpawnAdapter {
	private record SpawnSpec(EntityType<? extends Mob> type,double health,boolean aerialSpawn){}
	private static final SpawnSpec SPIRIT=new SpawnSpec(EntityType.ALLAY,30,false);
	private static final Map<SpellDefinition,SpawnSpec> SPECS=Map.of(
		SpellDefinition.MONSTER_SUMMON,new SpawnSpec(EntityType.WOLF,24,false),
		SpellDefinition.NECROMANCY,new SpawnSpec(EntityType.ZOMBIE,20,false),
		SpellDefinition.SPIRIT_SUMMON,SPIRIT,
		SpellDefinition.LAMPLIGHT_SPIRITS,new SpawnSpec(EntityType.ALLAY,30,true));
	private static Vec3 spot(ServerPlayer owner,MagicAim aim){
		var from=owner.position().add(aim.direction().multiply(1,0,1).normalize().scale(3));
		var level=(ServerLevel)owner.level();
		for(int y=2;y>=-2;y--){
			var pos=BlockPos.containing(from).offset(0,y,0);if(!level.hasChunkAt(pos)||!level.getBlockState(pos.below()).isFaceSturdy(level,pos.below(),net.minecraft.core.Direction.UP))continue;
			var p=Vec3.atBottomCenterOf(pos);if(level.noCollision(new AABB(p.x-.4,p.y,p.z-.4,p.x+.4,p.y+1.9,p.z+.4)))return p;
		}
		return null;
	}
	private static String slot(ServerPlayer owner,SpellDefinition s){
		var slots=owner.getAttachedOrCreate(SLOTS);
		return s.id().equals("necromancy")?NecromancyRules.slot(slots):MonsterSummonRules.slot(slots,s.id());
	}
	public static boolean canCast(ServerPlayer owner,SpellDefinition s,MagicAim aim){
		if(!aim.valid()||slot(owner,s)==null)return false;
		return s==SpellDefinition.LAMPLIGHT_SPIRITS||spot(owner,aim)!=null&&(s!=SpellDefinition.NECROMANCY||SummonCorpseRepository.corpse(owner,aim)!=null);
	}
	public static boolean cast(ServerPlayer owner,SpellDefinition spell,MagicAim aim){
		if(!canCast(owner,spell,aim))return false;
		var level=(ServerLevel)owner.level();String slot=slot(owner,spell);
		var spec=SPECS.getOrDefault(spell,SPIRIT);
		Mob mob=spec.type().create(level,EntitySpawnReason.MOB_SUMMONED);
		if(mob==null)return false;
		mob.setPos(spec.aerialSpawn()?owner.position().add(0,1,0):spot(owner,aim));
		mob.setNoAi(true);mob.setCanPickUpLoot(false);mob.setPersistenceRequired();
		if(mob.getAttribute(Attributes.MAX_HEALTH)!=null)mob.getAttribute(Attributes.MAX_HEALTH).setBaseValue(spec.health());
		mob.setHealth(mob.getMaxHealth());
		for(var equipment:EquipmentSlot.values()){mob.setItemSlot(equipment,net.minecraft.world.item.ItemStack.EMPTY);mob.setDropChance(equipment,0);}
		mob.setAttached(MagicAttachments.VISUAL_SPELL,spell.id());mob.setAttached(OWNER,owner.getUUID().toString());mob.setAttached(KIND,slot);mob.setAttached(EXPIRY,level.getGameTime()+spell.lifetimeTicks());mob.setAttached(NEXT,level.getGameTime()+20);
		int upkeep=SummonUpkeepAdapter.initial(owner,spell,level.getGameTime());
		if(MagicSystem.state(owner).mana()<upkeep||!level.addFreshEntity(mob))return false;
		SummonUpkeepAdapter.commitInitial(owner,spell,level.getGameTime(),upkeep);
		var slots=new HashMap<>(owner.getAttachedOrCreate(SLOTS));slots.put(slot,mob.getUUID().toString());owner.setAttached(SLOTS,Map.copyOf(slots));var ends=new HashMap<>(owner.getAttachedOrCreate(SLOT_END));ends.put(slot,level.getGameTime()+spell.lifetimeTicks());owner.setAttached(SLOT_END,Map.copyOf(ends));
		if(spell==SpellDefinition.NECROMANCY){SummonCorpseRepository.consume(owner,aim);}
		return true;
	}

	private SummonSpawnAdapter(){}
}
