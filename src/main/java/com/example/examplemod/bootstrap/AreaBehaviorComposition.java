package com.example.examplemod.bootstrap;
import com.example.examplemod.infrastructure.fabric.magic.HolySanctuaryAdapter;
import com.example.examplemod.infrastructure.fabric.magic.CumulonimbusAdapter;
import com.example.examplemod.infrastructure.fabric.magic.SquallAdapter;
import com.example.examplemod.infrastructure.fabric.magic.IllusionBarrierAdapter;
import com.example.examplemod.infrastructure.fabric.magic.EarthHedgehogAdapter;
import com.example.examplemod.infrastructure.fabric.magic.DivineJudgmentAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.infrastructure.fabric.magic.*;
import java.util.Map;
import java.util.function.*;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.phys.AABB;
/** Explicit lifecycle Strategies for area entities; registration and storage identifiers are compatibility data. */
public final class AreaBehaviorComposition {
	public record Behavior(Consumer<MagicAreaEntity> tick,Function<MagicAreaEntity,AABB> bounds,
		BiConsumer<MagicAreaEntity,ValueOutput> save,BiConsumer<MagicAreaEntity,ValueInput> load,String storageKey,boolean late) {}
	private static final Map<String,Behavior> BEHAVIORS=Map.ofEntries(
		Map.entry("illusion_barrier",new Behavior(IllusionBarrierAdapter::tick,a -> IllusionBarrierAdapter.bounds(a),IllusionBarrierAdapter::save,IllusionBarrierAdapter::load,"Illusion",false)),
		Map.entry("fortress_barrier",new Behavior(PoweredBarrierRuntimeAdapter::tick,a -> PoweredBarrierRuntimeAdapter.bounds(a),PoweredBarrierRuntimeAdapter::save,PoweredBarrierRuntimeAdapter::load,"CoreSpell",false)),
		Map.entry("demon_god_sealing_barrier",new Behavior(PoweredBarrierRuntimeAdapter::tick,a -> PoweredBarrierRuntimeAdapter.bounds(a),PoweredBarrierRuntimeAdapter::save,PoweredBarrierRuntimeAdapter::load,"CoreSpell",false)),
		Map.entry("saint_dragon_battle_aura",new Behavior(PoweredBarrierRuntimeAdapter::tick,a -> PoweredBarrierRuntimeAdapter.bounds(a),PoweredBarrierRuntimeAdapter::save,PoweredBarrierRuntimeAdapter::load,"CoreSpell",false)),
		Map.entry("dual_shield",new Behavior(FiniteBarrierRuntimeAdapter::tick,a -> FiniteBarrierRuntimeAdapter.bounds(a),FiniteBarrierRuntimeAdapter::save,FiniteBarrierRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("spirit_sealing_barrier",new Behavior(FiniteBarrierRuntimeAdapter::tick,a -> FiniteBarrierRuntimeAdapter.bounds(a),FiniteBarrierRuntimeAdapter::save,FiniteBarrierRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("confinement_barrier",new Behavior(FiniteBarrierRuntimeAdapter::tick,a -> FiniteBarrierRuntimeAdapter.bounds(a),FiniteBarrierRuntimeAdapter::save,FiniteBarrierRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("atmosphere_dominion",new Behavior(MaintainedFieldRuntimeAdapter::tick,a -> MaintainedFieldRuntimeAdapter.bounds(a),MaintainedFieldRuntimeAdapter::save,MaintainedFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("gravity_magic",new Behavior(MaintainedFieldRuntimeAdapter::tick,a -> MaintainedFieldRuntimeAdapter.bounds(a),MaintainedFieldRuntimeAdapter::save,MaintainedFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("nuclear_explosion",new Behavior(MaintainedFieldRuntimeAdapter::tick,a -> MaintainedFieldRuntimeAdapter.bounds(a),MaintainedFieldRuntimeAdapter::save,MaintainedFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("sand_cloud",new Behavior(SmokeFieldRuntimeAdapter::tick,a -> box(a,SmokeFieldRuntimeAdapter.radius(a.spell()),0,SmokeFieldRuntimeAdapter.height(a.spell())),SmokeFieldRuntimeAdapter::save,SmokeFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("dust_storm",new Behavior(SmokeFieldRuntimeAdapter::tick,a -> box(a,SmokeFieldRuntimeAdapter.radius(a.spell()),0,SmokeFieldRuntimeAdapter.height(a.spell())),SmokeFieldRuntimeAdapter::save,SmokeFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("sandstorm",new Behavior(SmokeFieldRuntimeAdapter::tick,a -> box(a,SmokeFieldRuntimeAdapter.radius(a.spell()),0,SmokeFieldRuntimeAdapter.height(a.spell())),SmokeFieldRuntimeAdapter::save,SmokeFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("deep_mist",new Behavior(SmokeFieldRuntimeAdapter::tick,a -> box(a,SmokeFieldRuntimeAdapter.radius(a.spell()),0,SmokeFieldRuntimeAdapter.height(a.spell())),SmokeFieldRuntimeAdapter::save,SmokeFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("mud_rain",new Behavior(SmokeFieldRuntimeAdapter::tick,a -> box(a,SmokeFieldRuntimeAdapter.radius(a.spell()),0,SmokeFieldRuntimeAdapter.height(a.spell())),SmokeFieldRuntimeAdapter::save,SmokeFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("flame_storm",new Behavior(GroundFieldRuntimeAdapter::tick,a -> box(a,GroundFieldRuntimeAdapter.radius(a.spell()),-.1,GroundFieldRuntimeAdapter.height(a.spell())),GroundFieldRuntimeAdapter::save,GroundFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("apocalypse_flame",new Behavior(GroundFieldRuntimeAdapter::tick,a -> box(a,GroundFieldRuntimeAdapter.radius(a.spell()),-.1,GroundFieldRuntimeAdapter.height(a.spell())),GroundFieldRuntimeAdapter::save,GroundFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("earthquake",new Behavior(GroundFieldRuntimeAdapter::tick,a -> box(a,GroundFieldRuntimeAdapter.radius(a.spell()),-.1,GroundFieldRuntimeAdapter.height(a.spell())),GroundFieldRuntimeAdapter::save,GroundFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("terra_fall",new Behavior(GroundFieldRuntimeAdapter::tick,a -> box(a,GroundFieldRuntimeAdapter.radius(a.spell()),-.1,GroundFieldRuntimeAdapter.height(a.spell())),GroundFieldRuntimeAdapter::save,GroundFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("continental_sink",new Behavior(GroundFieldRuntimeAdapter::tick,a -> box(a,GroundFieldRuntimeAdapter.radius(a.spell()),-.1,GroundFieldRuntimeAdapter.height(a.spell())),GroundFieldRuntimeAdapter::save,GroundFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("ocean_freeze",new Behavior(OceanFreezeRuntimeAdapter::tick,a -> new AABB(a.position().add(-64,-3,-64),a.position().add(64,3,64)),OceanFreezeRuntimeAdapter::save,OceanFreezeRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("sacred_nova",new Behavior(HolySanctuaryAdapter::tick,a -> new AABB(a.position(),a.position()).inflate(a.spell()==SpellDefinition.GOD_PURGE?48:16),HolySanctuaryAdapter::save,HolySanctuaryAdapter::load,"ExpansionSpell",false)),
		Map.entry("god_purge",new Behavior(HolySanctuaryAdapter::tick,a -> new AABB(a.position(),a.position()).inflate(a.spell()==SpellDefinition.GOD_PURGE?48:16),HolySanctuaryAdapter::save,HolySanctuaryAdapter::load,"ExpansionSpell",false)),
		Map.entry("divine_judge",new Behavior(DivineJudgmentAdapter::tick,a -> DivineJudgmentAdapter.bounds(a.position()),DivineJudgmentAdapter::save,DivineJudgmentAdapter::load,"ExpansionSpell",false)),
		Map.entry("absolute_zero",new Behavior(AbsoluteZero::tick,a -> new AABB(a.position(),a.position()).inflate(16),AbsoluteZero::save,AbsoluteZero::load,"ExpansionSpell",false)),
		Map.entry("inferno",new Behavior(Inferno::tick,a -> box(a,20,0,8),Inferno::save,Inferno::load,"ExpansionSpell",false)),
		Map.entry("flash_over",new Behavior(FlashOver::tick,a -> box(a,16,0,6),FlashOver::save,FlashOver::load,"ExpansionSpell",false)),
		Map.entry("flame_tornado",new Behavior(WindFieldRuntimeAdapter::tick,a -> box(a,WindFieldRuntimeAdapter.radius(a.spell()),0,WindFieldRuntimeAdapter.height(a.spell())),WindFieldRuntimeAdapter::save,WindFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("violent_storm",new Behavior(WindFieldRuntimeAdapter::tick,a -> box(a,WindFieldRuntimeAdapter.radius(a.spell()),0,WindFieldRuntimeAdapter.height(a.spell())),WindFieldRuntimeAdapter::save,WindFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("tornado",new Behavior(WindFieldRuntimeAdapter::tick,a -> box(a,WindFieldRuntimeAdapter.radius(a.spell()),0,WindFieldRuntimeAdapter.height(a.spell())),WindFieldRuntimeAdapter::save,WindFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("vacuum_storm",new Behavior(WindFieldRuntimeAdapter::tick,a -> box(a,WindFieldRuntimeAdapter.radius(a.spell()),0,WindFieldRuntimeAdapter.height(a.spell())),WindFieldRuntimeAdapter::save,WindFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("sky_tempest",new Behavior(WindFieldRuntimeAdapter::tick,a -> box(a,WindFieldRuntimeAdapter.radius(a.spell()),0,WindFieldRuntimeAdapter.height(a.spell())),WindFieldRuntimeAdapter::save,WindFieldRuntimeAdapter::load,"ExpansionSpell",false)),
		Map.entry("magma_gush",new Behavior(MagmaGush::tick,a -> box(a,4,0,5),MagmaGush::save,MagmaGush::load,"ExpansionSpell",false)),
		Map.entry("quagmire",new Behavior(Quagmire::tick,a -> box(a,6,-.2,1),Quagmire::save,Quagmire::load,"ExpansionSpell",false)),
		Map.entry("earth_hedgehog",new Behavior(EarthHedgehogAdapter::tick,a -> box(a,6,-3,6),EarthHedgehogAdapter::save,EarthHedgehogAdapter::load,"ExpansionSpell",false)),
		Map.entry("cumulonimbus",new Behavior(CumulonimbusAdapter::tick,a -> box(a,24,0,48),CumulonimbusAdapter::save,CumulonimbusAdapter::load,"ExpansionSpell",true)),
		Map.entry("squall",new Behavior(SquallAdapter::tick,a -> box(a,16,0,32),SquallAdapter::save,SquallAdapter::load,"ExpansionSpell",true)),
		Map.entry("blizzard_storm",new Behavior(MagicAreaEntity::tickBlizzard,a -> new AABB(a.position().add(0,1,0),a.position().add(0,1,0)).inflate(8),MagicAreaEntity::saveBlizzard,MagicAreaEntity::loadBlizzard,"ExpansionSpell",true)));
	public static Behavior find(SpellDefinition spell){return spell==null?null:BEHAVIORS.get(spell.id());}
	private static AABB box(MagicAreaEntity a,double r,double bottom,double top){return new AABB(a.getX()-r,a.getY()+bottom,a.getZ()-r,a.getX()+r,a.getY()+top,a.getZ()+r);}
	public static void save(MagicAreaEntity area,ValueOutput output){
		var behavior=find(area.spell());if(behavior==null)return;
		if(behavior.storageKey.equals("Illusion"))output.putBoolean("Illusion",true);
		else output.putString(behavior.storageKey,area.spell().id());
		behavior.save.accept(area,output);
	}
	private static void loadKey(MagicAreaEntity area,ValueInput input,String key,boolean late){
		String id=key.equals("Illusion")?(input.getBooleanOr("Illusion",false)?"illusion_barrier":""):input.getStringOr(key,"");
		var behavior=BEHAVIORS.get(id);
		if(behavior==null||!behavior.storageKey.equals(key)||behavior.late!=late)return;
		area.restoreSpellId(id);behavior.load.accept(area,input);
	}
	public static void load(MagicAreaEntity area,ValueInput input,Runnable expireBase){
		loadKey(area,input,"Illusion",false);loadKey(area,input,"CoreSpell",false);loadKey(area,input,"ExpansionSpell",false);
		expireBase.run();loadKey(area,input,"ExpansionSpell",true);
	}
	private AreaBehaviorComposition(){}
}
