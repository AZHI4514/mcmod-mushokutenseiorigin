package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import java.util.*;
import com.example.examplemod.ExampleMod;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.*;

/** Server-owned committed work queue, independent of casting/channel state. */
public final class GrowingColumnRuntimeAdapter {
	private record Column(com.example.examplemod.magic.mechanic.terrain.ColumnGrowth<net.minecraft.core.BlockPos> growth,SpellDefinition spell,boolean permanent) { }
	private static final AttachmentType<ColumnRuntime> COLUMN_RUNTIME=AttachmentRegistry.create(ExampleMod.id("column_runtime"),b->b.initializer(ColumnRuntime::new));
	private static final class ColumnRuntime { final Map<UUID,java.util.List<Column>> columns=new HashMap<>(); }
	private static Map<UUID,java.util.List<Column>> columns(ServerPlayer owner) { return owner.level().getServer().overworld().getAttachedOrCreate(COLUMN_RUNTIME).columns; }
	public static void growingColumn(ServerPlayer owner,java.util.List<net.minecraft.core.BlockPos> positions,SpellDefinition spell,boolean permanent) {
		columns(owner).computeIfAbsent(owner.getUUID(),k->new ArrayList<>()).add(new Column(new com.example.examplemod.magic.mechanic.terrain.ColumnGrowth<>(positions,owner.level().getGameTime()+200),spell,permanent));
	}
	public static void tick(ServerPlayer owner) {
		var list=columns(owner).get(owner.getUUID());if(list==null)return;var level=(ServerLevel)owner.level();
		list.removeIf(c->c.growth.tick(level.getGameTime(),new com.example.examplemod.magic.mechanic.terrain.ColumnGrowth.Placement<net.minecraft.core.BlockPos>() {
			public boolean available(net.minecraft.core.BlockPos p) { return com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(owner,p)&&level.getBlockState(p).isAir(); }
			public boolean occupied(net.minecraft.core.BlockPos p) { return !level.getEntities(owner,new AABB(p),e->e instanceof LivingEntity).isEmpty()||owner.getBoundingBox().intersects(new AABB(p)); }
			public void place(net.minecraft.core.BlockPos p,int remaining) {
				if(c.permanent)level.setBlockAndUpdate(p,net.minecraft.world.level.block.Blocks.DIRT.defaultBlockState());
				else com.example.examplemod.infrastructure.fabric.magic.TemporaryTerrainAdapter.temporary(owner,p,c.spell==SpellDefinition.EARTH_LANCE?MagicTerrain.TEMPORARY_DIRT:MagicTerrain.TEMPORARY_ICE,remaining);
			}
		}));if(list.isEmpty())columns(owner).remove(owner.getUUID());
	}
	public static void initialize() {}
	public static void clear(ServerPlayer owner){columns(owner).remove(owner.getUUID());}
	public static void clearServer(net.minecraft.server.MinecraftServer server){server.overworld().removeAttached(COLUMN_RUNTIME);}
	private GrowingColumnRuntimeAdapter() {}
}
