package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
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

public record AntidoteIIIAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return cure(player,new MagicAim(yaw,pitch));}
	private static boolean needsCure(LivingEntity target){
		return target.hasEffect(MobEffects.POISON)||target.hasEffect(MobEffects.NAUSEA)||target.hasEffect(MobEffects.HUNGER)||target.hasEffect(MobEffects.WEAKNESS);
	}
	private static LivingEntity cureTarget(ServerPlayer caster,MagicAim aim){
		var target=caster.isShiftKeyDown()?caster:SupportRayAdapter.trace(caster,aim,12,t->SupportConsentAdapter.consented(caster,t));
		return target!=null&&needsCure(target)?target:null;
	}
	public static boolean canCure(ServerPlayer caster,MagicAim aim){return cureTarget(caster,aim)!=null;}
	public static boolean cure(ServerPlayer caster,MagicAim aim){
		var target=cureTarget(caster,aim);if(target==null)return false;
		return com.example.examplemod.magic.spell.detox.antidote_iii.AntidoteIIIEffect.apply(new com.example.examplemod.magic.spell.detox.antidote_iii.AntidoteIIIEffect.Patient(){
			public boolean removePoison(){return target.removeEffect(MobEffects.POISON);}public boolean removeNausea(){return target.removeEffect(MobEffects.NAUSEA);}public boolean removeHunger(){return target.removeEffect(MobEffects.HUNGER);}public boolean removeWeakness(){return target.removeEffect(MobEffects.WEAKNESS);}
			public void visual(){AntidoteIIIAdapter.visual(caster,target);}
		});
	}
	private static void visual(ServerPlayer caster,LivingEntity target){
		var level=(ServerLevel)caster.level();
		for(int i=0;i<48;i++){
			double angle=i*.65,y=(i%24)/24.0*target.getBbHeight();
			level.sendParticles(SpellParticles.accent(SpellDefinition.ANTIDOTE_III),target.getX()+Math.cos(angle)*.6,target.getY()+y,target.getZ()+Math.sin(angle)*.6,2,.06,.06,.06,.025);
		}
		SpellVisuals.burst(level,SpellDefinition.ANTIDOTE_III,target.position().add(0,target.getBbHeight()*.5,0),new Vec3(0,1,0));
	}
}
