package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.fire.heat_island.HeatIslandRules;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
public record HeatIslandAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return AreaRuntimeAdapter.cast(player,SpellDefinition.HEAT_ISLAND,new MagicAim(yaw,pitch));}
	public static void warm(LivingEntity target,SpellDefinition spell){HeatIslandRules.apply(new HeatIslandRules.Target(){
		public void removeWet(){target.removeEffect(MagicEffects.WET);}
		public void warm(){MagicEffects.warm(target,spell.statusTicks());}
	});}
}
