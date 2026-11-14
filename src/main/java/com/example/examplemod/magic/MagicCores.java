package com.example.examplemod.magic;

import com.example.examplemod.infrastructure.fabric.magic.SpellRaycastAdapter;

import com.example.examplemod.ExampleMod;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;

/** Destructible owned physical cores. Deposits spend real player mana; no refunds or duplication. */
public final class MagicCores {
	private static final AttachmentType<String> OWNER=AttachmentRegistry.create(ExampleMod.id("core_owner"),b->b.initializer(()->"").persistent(Codec.STRING));
	private static final AttachmentType<Integer> ENERGY=AttachmentRegistry.create(ExampleMod.id("core_energy"),b->b.initializer(()->0).persistent(Codec.intRange(0,6000)));
	public static boolean core(Entity e){return e!=null&&!e.getAttachedOrElse(OWNER,"").isEmpty();}
	public static boolean owned(ServerPlayer owner,Entity core){return core(core)&&core.getAttachedOrElse(OWNER,"").equals(owner.getUUID().toString());}
	public static int energy(Entity core){return core==null?0:core.getAttachedOrElse(ENERGY,0);}
	public static boolean take(Entity core,int amount){int before=energy(core);if(core==null||amount<0||before<amount||!core.isAlive()||core.isRemoved())return false;core.setAttached(ENERGY,before-amount);return true;}
	public static void initialize(){
		net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents.ENTITY_LOAD.register((entity,level)->{if(core(entity)){entity.setInvisible(true);entity.setAttached(MagicAttachments.VISUAL_SPELL,"magic_core");}});
		CommandRegistrationCallback.EVENT.register((dispatcher,registry,environment)->{
			dispatcher.register(Commands.literal("mtcore")
				.then(Commands.literal("create").executes(c->create(c.getSource().getPlayerOrException())))
				.then(Commands.literal("charge").then(Commands.argument("mp",IntegerArgumentType.integer(1,6000)).executes(c->charge(c.getSource().getPlayerOrException(),IntegerArgumentType.getInteger(c,"mp")))))
				.then(Commands.literal("status").executes(c->{var p=c.getSource().getPlayerOrException();var core=nearest(p,p.position(),6);if(core==null)return 0;p.sendSystemMessage(net.minecraft.network.chat.Component.translatable("magic.mushokutenseiorigin.core_energy",energy(core)));return 1;})));
		});
	}
	private static int create(ServerPlayer owner){
		var level=(ServerLevel)owner.level();var from=owner.getEyePosition();var to=from.add(owner.getLookAngle().scale(6));
		if(!SpellRaycastAdapter.loaded(level,new AABB(from,to)))return 0;
		var hit=level.clip(new ClipContext(from,to,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,owner));
		if(hit.getType()!=HitResult.Type.BLOCK||!level.getBlockState(hit.getBlockPos()).is(Blocks.LODESTONE)||!com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(owner,hit.getBlockPos()))return 0;
		var at=Vec3.atBottomCenterOf(hit.getBlockPos().above());
		if(level.getEntitiesOfClass(ArmorStand.class,new AABB(at,at).inflate(.8),MagicCores::core).size()>0)return 0;
		int crystals=0;
		for(int i=0;i<owner.getInventory().getContainerSize();i++){var stack=owner.getInventory().getItem(i);if(stack.is(com.example.examplemod.item.ModItems.CHARGED_MANA_CRYSTAL))crystals+=stack.getCount();}
		if(crystals<4)return 0;
		var stand=EntityType.ARMOR_STAND.create(level,EntitySpawnReason.MOB_SUMMONED);if(stand==null)return 0;
		stand.setPos(at);stand.setNoGravity(true);stand.setNoBasePlate(true);stand.setShowArms(true);
		stand.setInvisible(true);stand.setAttached(MagicAttachments.VISUAL_SPELL,"magic_core");
		stand.setAttached(OWNER,owner.getUUID().toString());
		if(!level.addFreshEntity(stand))return 0;
		int left=4;
		for(int i=0;i<owner.getInventory().getContainerSize()&&left>0;i++){var stack=owner.getInventory().getItem(i);if(stack.is(com.example.examplemod.item.ModItems.CHARGED_MANA_CRYSTAL)){int take=Math.min(left,stack.getCount());stack.shrink(take);left-=take;}}
		return 1;
	}
	private static int charge(ServerPlayer owner,int requested){
		var core=nearest(owner,owner.position(),6);if(core==null)return 0;
		var state=MagicSystem.state(owner);int amount=Math.min(requested,Math.min(state.mana(),6000-energy(core)));if(amount<=0)return 0;
		owner.setAttached(MagicAttachments.STATE,state.spendMana(amount));core.setAttached(ENERGY,energy(core)+amount);return amount;
	}
	public static ArmorStand nearest(ServerPlayer owner,Vec3 at,double radius){
		return owner.level().getEntitiesOfClass(ArmorStand.class,new AABB(at,at).inflate(radius),e->owned(owner,e)&&e.isAlive()&&e.position().distanceToSqr(at)<=radius*radius)
				.stream().min(java.util.Comparator.comparingDouble(e->e.position().distanceToSqr(at))).orElse(null);
	}
	public static boolean valid(Entity core){
		return core!=null&&core.isAlive()&&!core.isRemoved()&&core.level().hasChunkAt(core.blockPosition().below())&&core.level().getBlockState(core.blockPosition().below()).is(Blocks.LODESTONE);
	}
	private MagicCores(){}
}
