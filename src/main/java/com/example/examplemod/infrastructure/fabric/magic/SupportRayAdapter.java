package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import java.util.HashSet;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.example.examplemod.ExampleMod;
import com.mojang.serialization.Codec;

public final class SupportRayAdapter {
	public static LivingEntity trace(ServerPlayer player,MagicAim aim,double range,java.util.function.Predicate<LivingEntity> acceptable){
		if(!aim.valid())return null;
		var level=(ServerLevel)player.level();var from=player.getEyePosition();var to=from.add(aim.direction().scale(range));
		if(!SpellRaycastAdapter.loaded(level,new AABB(from,to).inflate(1)))return null;
		var point=level.clip(new ClipContext(from,to,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,player)).getLocation();
		double nearest=from.distanceToSqr(point);LivingEntity selected=null;
		for(var target:level.getEntitiesOfClass(LivingEntity.class,new AABB(from,point).inflate(1),t->t!=player&&t.isAlive()&&!t.isSpectator())){
			var hit=target.getBoundingBox().clip(from,point);
			if(hit.isPresent()&&from.distanceToSqr(hit.get())<nearest){nearest=from.distanceToSqr(hit.get());selected=target;}
		}
		return selected!=null&&acceptable.test(selected)?selected:null;
	}
	private SupportRayAdapter() { }
}
