package com.example.examplemod.infrastructure.fabric.magic;
import java.util.function.Supplier;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.water.water_shield.WaterShieldSelection;
import com.example.examplemod.magic.mechanic.projectile.ShieldInterception;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
public final class WaterShieldCollisionAdapter {
	public static boolean intercept(Projectile projectile,Supplier<Iterable<MagicAreaEntity>> areas){
		if(projectile instanceof DirectionalSpellCarrier phase&&phase.stationary())return false;
		if(projectile instanceof MagicAreaEntity||projectile instanceof net.minecraft.world.entity.projectile.FishingHook
				||projectile.isRemoved()||projectile.getDeltaMovement().lengthSqr()<1.0E-8)return false;
		var movement=projectile.getDeltaMovement();double distance=movement.length();
		var contact=WaterShieldSelection.nearest(areas.get(),area->{
			if(area.isRemoved()||area.level()!=projectile.level()||area.spell()!=SpellDefinition.WATER_SHIELD||projectile.getOwner()==area.getOwner())return false;
			var normal=area.wallDirection();return !(movement.dot(normal)>=0);
		},area->area.wallVolume().contact(projectile.getBoundingBox(),area.position(),movement.scale(-1/distance),distance));
		if(contact==null)return false;
		var selected=contact.shield();var level=projectile.level();var end=projectile.position().add(movement.scale(contact.distance()/distance));
		return ShieldInterception.apply(new ShieldInterception.Contact(){
			public boolean terrainClear(){return level.clip(new net.minecraft.world.level.ClipContext(projectile.position(),end,net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,projectile)).getType()==net.minecraft.world.phys.HitResult.Type.MISS;}
			public float incomingDamage(){return MagicProjectileDamage.incoming(projectile,selected);}
			public void absorb(float damage){selected.absorb(damage);}
			public boolean trident(){return projectile instanceof ThrownTrident;}
			public void rebound(){projectile.setPos(end.subtract(movement.normalize().scale(0.1)));projectile.setDeltaMovement(movement.scale(-0.1));}
			public void discard(){projectile.discard();}
		});
	}
	private WaterShieldCollisionAdapter() { }
}
