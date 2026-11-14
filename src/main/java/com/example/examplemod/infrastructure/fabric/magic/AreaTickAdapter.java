package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.field.AreaTick;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import java.util.Map;
import java.util.UUID;
/** Minecraft queries/effects; mutable lifetime and contact state remain with the entity. */
public final class AreaTickAdapter implements AreaTick.World {
	public interface State {
		int decrementRemaining();boolean stationary();double displacementSquared();void advancePulse();int pulse();
	}
	private final MagicAreaEntity area;private final ServerLevel level;private final ServerPlayer owner;private final State state;
	private final Map<UUID,Integer> fireContacts,wetContacts;
	public AreaTickAdapter(MagicAreaEntity area,ServerLevel level,State state,Map<UUID,Integer> fireContacts,Map<UUID,Integer> wetContacts){this.area=area;this.level=level;this.state=state;this.owner=area.getOwner() instanceof ServerPlayer player?player:null;this.fireContacts=fireContacts;this.wetContacts=wetContacts;}
	public boolean ownerPresent(){return owner!=null;}
	public boolean ownerRemoved(){return owner.isRemoved();}
	public boolean ownerAlive(){return owner.isAlive();}
	public boolean sameLevel(){return owner.level()==level;}
	public int decrementRemaining(){return state.decrementRemaining();}
	public boolean loaded(){return level.hasChunkAt(area.blockPosition());}
	public void discard(){area.discard();}
	public boolean stationary(){return state.stationary();}
	public double displacementSquared(){return state.displacementSquared();}
	public void breakMaintenance(){area.breakMaintenance();}
	public void followOwner(){area.setPos(owner.position());}
	public void advancePulse(){state.advancePulse();}
	public void pruneContacts(){fireContacts.values().removeIf(last->state.pulse()-last>=20);wetContacts.values().removeIf(last->state.pulse()-last>=20);}
	public void applyContacts(){
		for (var entity : level.getEntities(area, area.areaBounds(), e -> e instanceof LivingEntity && e.isAlive() && !e.isSpectator())) {
			var target = (LivingEntity) entity;
			if (area.spell() == SpellDefinition.WATER_SHIELD && area.touchesWall(target.getBoundingBox())) {
				com.example.examplemod.infrastructure.fabric.magic.WaterShieldContactAdapter.apply(owner,target,area.spell(),wetContacts,state.pulse());
			} else if (area.spell() == SpellDefinition.HEAT_ISLAND && com.example.examplemod.magic.spell.fire.heat_island.HeatIslandRules.inside(target.getX()-area.getX(),target.getZ()-area.getZ())) {
				com.example.examplemod.infrastructure.fabric.magic.HeatIslandAdapter.warm(target,area.spell());
			} else if (area.spell() == SpellDefinition.BURNING_PLACE) {
				com.example.examplemod.infrastructure.fabric.magic.BurningPlaceAdapter.contact(area,owner,level,target,fireContacts,state.pulse());
			}
		}
	}
}
