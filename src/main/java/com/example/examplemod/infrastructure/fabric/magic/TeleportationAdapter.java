package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.special.teleportation_magic.*;
import com.example.examplemod.magic.spell.special.teleportation_magic.TeleportInvitations.Invitation;

import java.util.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.*;
import net.minecraft.network.chat.Component;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.item.ModItems;

/** Owner-registered circles, short-lived invitation consent and complete preflight before any movement. */
public record TeleportationAdapter(ServerPlayer player) implements SelfEffect {
	private record Circle(String name,String dimension,BlockPos pos){
		static final Codec<Circle> CODEC=RecordCodecBuilder.create(i->i.group(Codec.STRING.fieldOf("name").forGetter(Circle::name),Codec.STRING.fieldOf("dimension").forGetter(Circle::dimension),BlockPos.CODEC.fieldOf("pos").forGetter(Circle::pos)).apply(i,Circle::new));
	}
	private static final AttachmentType<List<Circle>> CIRCLES=AttachmentRegistry.create(ExampleMod.id("teleport_circles"),b->b.initializer(()->List.<Circle>of()).persistent(Circle.CODEC.listOf()).copyOnDeath());
	private static final AttachmentType<String> DESTINATION=AttachmentRegistry.create(ExampleMod.id("teleport_destination"),b->b.initializer(()->"").persistent(Codec.STRING).copyOnDeath());
	private static final AttachmentType<TeleportInvitations> RUNTIME=AttachmentRegistry.create(ExampleMod.id("teleport_runtime"),b->b.initializer(TeleportInvitations::new));
	private static TeleportInvitations invitations(ServerPlayer p){return p.level().getServer().overworld().getAttachedOrCreate(RUNTIME);}
	private record Plan(Circle source,Circle destination,ServerLevel level,List<LivingEntity> travelers,List<Vec3> positions,int cost){}
	public static void initialize(){
		CommandRegistrationCallback.EVENT.register((d,r,e)->d.register(Commands.literal("mtteleport")
			.then(Commands.literal("register").then(Commands.argument("name",StringArgumentType.word()).executes(c->register(c.getSource().getPlayerOrException(),StringArgumentType.getString(c,"name")))))
			.then(Commands.literal("destination").then(Commands.argument("name",StringArgumentType.word()).executes(c->{var p=c.getSource().getPlayerOrException();String name=StringArgumentType.getString(c,"name");if(p.getAttachedOrCreate(CIRCLES).stream().noneMatch(x->x.name.equals(name)))return 0;p.setAttached(DESTINATION,name);return preview(p); })))
			.then(Commands.literal("list").executes(c->{var p=c.getSource().getPlayerOrException();for(var x:p.getAttachedOrCreate(CIRCLES))p.sendSystemMessage(Component.literal(x.name+" · "+x.dimension+" · "+x.pos.toShortString()));return 1;}))
			.then(Commands.literal("forget").then(Commands.argument("name",StringArgumentType.word()).executes(c->{var p=c.getSource().getPlayerOrException();var list=new ArrayList<>(p.getAttachedOrCreate(CIRCLES));boolean changed=list.removeIf(x->x.name.equals(StringArgumentType.getString(c,"name")));p.setAttached(CIRCLES,List.copyOf(list));return changed?1:0;})))
			.then(Commands.literal("invite").then(Commands.argument("player",EntityArgument.player()).executes(c->{var p=c.getSource().getPlayerOrException();var other=EntityArgument.getPlayer(c,"player");if(p==other)return 0;invitations(p).put(other.getUUID(),new Invitation(p.getUUID(),clock(p)+1200,false));other.sendSystemMessage(Component.translatable("magic.mushokutenseiorigin.teleport_invitation",p.getName().getString()));return 1;})))
			.then(Commands.literal("accept").then(Commands.argument("caster",EntityArgument.player()).executes(c->{var p=c.getSource().getPlayerOrException();var caster=EntityArgument.getPlayer(c,"caster");var invite=invitations(p).get(p.getUUID());if(invite==null||!invite.caster().equals(caster.getUUID())||invite.expiry()<=clock(p))return 0;invitations(p).put(p.getUUID(),new Invitation(invite.caster(),invite.expiry(),true));return 1;})))
			.then(Commands.literal("decline").executes(c->{invitations(c.getSource().getPlayerOrException()).remove(c.getSource().getPlayerOrException().getUUID());return 1;}))
			.then(Commands.literal("preview").executes(c->preview(c.getSource().getPlayerOrException())))));
	}
	private static long clock(ServerPlayer p){return p.level().getServer().overworld().getGameTime();}
	private static boolean structure(ServerLevel level,BlockPos center){
		for(int x=-2;x<=2;x++)for(int z=-2;z<=2;z++){
			var pos=center.offset(x,0,z);if(!level.hasChunkAt(pos))return false;
			var block=x==0&&z==0?Blocks.LODESTONE:Math.abs(x)==2&&Math.abs(z)==2?Blocks.AMETHYST_BLOCK:Blocks.POLISHED_DEEPSLATE;
			if(!level.getBlockState(pos).is(block))return false;
		}
		for(int i=1;i<=3;i++)if(!level.getBlockState(center.above(i)).isAir())return false;
		return true;
	}
	private static Circle source(ServerPlayer p){return p.getAttachedOrCreate(CIRCLES).stream().filter(c->c.dimension.equals(p.level().dimension().identifier().toString())&&p.position().distanceToSqr(Vec3.atBottomCenterOf(c.pos.above()))<=4&&structure(p.level(),c.pos)).findFirst().orElse(null);}
	private static int register(ServerPlayer p,String name){
		if(!TeleportRules.validName(name))return 0;var center=p.blockPosition().below();
		if(!structure(p.level(),center)||!com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(p,center))return 0;
		var list=new ArrayList<>(p.getAttachedOrCreate(CIRCLES));if(list.size()>=64||list.stream().anyMatch(c->c.name.equals(name)||c.dimension.equals(p.level().dimension().identifier().toString())&&c.pos.equals(center)))return 0;
		int crystals=0;for(int i=0;i<p.getInventory().getContainerSize();i++){var s=p.getInventory().getItem(i);if(s.is(ModItems.CHARGED_MANA_CRYSTAL))crystals+=s.getCount();}if(crystals<4)return 0;
		int left=4;for(int i=0;i<p.getInventory().getContainerSize()&&left>0;i++){var s=p.getInventory().getItem(i);if(s.is(ModItems.CHARGED_MANA_CRYSTAL)){int n=Math.min(left,s.getCount());s.shrink(n);left-=n;}}
		list.add(new Circle(name,p.level().dimension().identifier().toString(),center.immutable()));p.setAttached(CIRCLES,List.copyOf(list));p.getInventory().setChanged();return 1;
	}
	private static boolean accepted(ServerPlayer caster,LivingEntity entity){
		if(entity==caster)return true;
		if(entity instanceof ServerPlayer p){var invite=invitations(p).get(p.getUUID());return invite!=null&&invite.accepted()&&invite.expiry()>clock(caster)&&invite.caster().equals(caster.getUUID())&&!p.hasDisconnected();}
		return entity instanceof TamableAnimal pet&&pet.getOwner()==caster;
	}
	private static boolean safe(ServerLevel level,LivingEntity entity,Vec3 at,List<AABB> reserved){
		var box=entity.getBoundingBox().move(at.subtract(entity.position()));
		return TeleportSafety.safe(new TeleportSafety.Spot(){
			public boolean loaded(){return SpellRaycastAdapter.loaded(level,box.inflate(1));}public boolean border(){return level.getWorldBorder().isWithinBounds(box);}public boolean collisionFree(){return level.noCollision(entity,box);}public boolean liquid(){return level.containsAnyLiquid(box);}public boolean reserved(){return reserved.stream().anyMatch(box::intersects);}
			public boolean hazard(){for(var pos:BlockPos.betweenClosed(BlockPos.containing(box.minX,box.minY,box.minZ),BlockPos.containing(box.maxX,box.maxY,box.maxZ))){var b=level.getBlockState(pos);if(b.is(Blocks.FIRE)||b.is(Blocks.SOUL_FIRE)||b.is(Blocks.POWDER_SNOW)||b.is(Blocks.SWEET_BERRY_BUSH)||b.is(Blocks.WITHER_ROSE))return true;}return false;}
			public boolean safeFloor(){var floor=BlockPos.containing(at).below();var state=level.getBlockState(floor);return state.isCollisionShapeFullBlock(level,floor)&&!state.is(Blocks.MAGMA_BLOCK)&&!state.is(Blocks.CAMPFIRE)&&!state.is(Blocks.SOUL_CAMPFIRE)&&!state.is(Blocks.CACTUS);}
		});
	}
	private static Plan plan(ServerPlayer p){
		var from=source(p);if(from==null)return null;var target=p.getAttachedOrCreate(CIRCLES).stream().filter(c->c.name.equals(p.getAttachedOrCreate(DESTINATION))).findFirst().orElse(null);if(target==null||target.equals(from))return null;
		var key=ResourceKey.create(Registries.DIMENSION,Identifier.parse(target.dimension));var level=p.level().getServer().getLevel(key);
		if(level==null||!level.mayInteract(p,target.pos)||!com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(p,from.pos)||!structure(level,target.pos)||!MagicConfig.teleportDimensionAllowed(from.dimension)||!MagicConfig.teleportDimensionAllowed(target.dimension))return null;
		boolean same=p.level()==level;double distance=Vec3.atCenterOf(from.pos).distanceTo(Vec3.atCenterOf(target.pos));if(same&&distance>2048)return null;
		var travelers=new ArrayList<LivingEntity>();travelers.add(p);
		var candidates=p.level().getEntitiesOfClass(LivingEntity.class,new AABB(p.position(),p.position()).inflate(2),t->t!=p&&t.isAlive()&&!t.isSpectator()&&t.position().distanceToSqr(p.position())<=4&&accepted(p,t));
		candidates.sort(Comparator.comparing(t->t.getUUID().toString()));if(candidates.size()>3)return null;travelers.addAll(candidates);
		// Unaccepted riders or inventories attached to vehicles must never be taken implicitly.
		for(var t:travelers)if(t.isPassenger()||!t.getPassengers().isEmpty()||!t.canTeleport(p.level(),level)||t.isSleeping()||t instanceof ServerPlayer sp&&sp.containerMenu!=sp.inventoryMenu)return null;
		var positions=new ArrayList<Vec3>();var boxes=new ArrayList<AABB>();
		for(var t:travelers){Vec3 found=null;for(int radius=0;radius<=2&&found==null;radius++)for(int x=-radius;x<=radius&&found==null;x++)for(int z=-radius;z<=radius;z++){
			var at=Vec3.atBottomCenterOf(target.pos.above().offset(x,0,z));if(safe(level,t,at,boxes)){found=at;break;}
		}if(found==null)return null;positions.add(found);boxes.add(t.getBoundingBox().move(found.subtract(t.position())));}
		int extra=TeleportRules.extra(same,distance);return new Plan(from,target,level,List.copyOf(travelers),List.copyOf(positions),160+extra);
	}
	public static String token(ServerPlayer p){var plan=plan(p);return plan==null?"":plan.source.toString()+"|"+plan.destination.toString()+"|"+plan.cost+"|"+plan.travelers.stream().map(t->t.getUUID().toString()).toList();}
	public static int preview(ServerPlayer p){var plan=plan(p);if(plan==null){p.sendSystemMessage(Component.translatable("magic.mushokutenseiorigin.teleport_invalid"));return 0;}p.sendSystemMessage(Component.translatable("magic.mushokutenseiorigin.teleport_cost",plan.cost,plan.travelers.size()));return 1;}
	public static boolean canCast(ServerPlayer p){var plan=plan(p);return plan!=null&&MagicSystem.state(p).mana()>=plan.cost;}
	public static boolean cast(ServerPlayer p){
		var plan=plan(p);if(plan==null)return false;int extra=plan.cost-160;if(MagicSystem.state(p).mana()<extra)return false;
		var oldLevel=p.level();var moved=new ArrayList<Entity>();var origins=plan.travelers.stream().map(Entity::position).toList();
		return TeleportRelease.cast(new TeleportRelease.World(){
			public int travelerCount(){return plan.travelers.size();}
			public boolean move(int i){var traveler=plan.travelers.get(i);var result=traveler.teleport(new TeleportTransition(plan.level,plan.positions.get(i),Vec3.ZERO,traveler.getYRot(),traveler.getXRot(),TeleportTransition.DO_NOTHING));if(result==null)return false;result.resetFallDistance();moved.add(result);return true;}
			public void rollback(int j){var entity=moved.get(j);entity.teleport(new TeleportTransition(oldLevel,origins.get(j),Vec3.ZERO,entity.getYRot(),entity.getXRot(),TeleportTransition.DO_NOTHING));}
			public void payExtra(){p.setAttached(MagicAttachments.STATE,MagicSystem.state(p).spendMana(extra));}
			public void consumeInvitations(){for(var traveler:plan.travelers)if(traveler instanceof ServerPlayer)invitations(p).remove(traveler.getUUID());}
			public void visual(){SpellVisuals.burst(oldLevel,SpellDefinition.TELEPORTATION_MAGIC,Vec3.atBottomCenterOf(plan.source.pos.above()),new Vec3(0,1,0));SpellVisuals.burst(plan.level,SpellDefinition.TELEPORTATION_MAGIC,Vec3.atBottomCenterOf(plan.destination.pos.above()),new Vec3(0,1,0));}
		});
	}
	public boolean apply(float yaw,float pitch){return cast(player);}
}
