package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.WindFieldRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.mixed.nuclear_explosion.NuclearExplosionRules;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.*;
import net.minecraft.world.phys.Vec3;
import static com.example.examplemod.infrastructure.fabric.magic.MaintainedFieldRuntimeAdapter.bounds;
import static com.example.examplemod.infrastructure.fabric.magic.MaintainedFieldRuntimeAdapter.visual;
/** Strategy for one spell's world update; upkeep/lifecycle is shared separately. */
interface MaintainedFieldEffect {
	com.example.examplemod.magic.mechanic.field.MaintainedFieldSpec spec();
	default Vec3 target(ServerPlayer owner,SpellDefinition spell,MagicAim aim){return WindFieldRuntimeAdapter.target(owner,spell,aim);}
	default boolean ready(ServerPlayer owner){return true;}
	default void released(ServerPlayer owner,long now){}
	void update(ServerLevel level,MagicAreaEntity area,MaintainedFieldRuntimeAdapter.State s,ServerPlayer owner,long now,boolean pulse);
}
