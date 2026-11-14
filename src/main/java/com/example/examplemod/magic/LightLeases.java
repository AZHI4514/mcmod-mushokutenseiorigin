package com.example.examplemod.magic;

import java.util.*;
import com.example.examplemod.ExampleMod;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.saveddata.*;

/** Saved, reference-counted air-only light leases. Never replaces an existing light or water. */
public final class LightLeases extends SavedData {
	private record Cell(BlockPos pos,Map<String,Long> users) {
		static final Codec<Cell> CODEC=RecordCodecBuilder.create(i->i.group(BlockPos.CODEC.fieldOf("pos").forGetter(Cell::pos),Codec.unboundedMap(Codec.STRING,Codec.LONG).fieldOf("users").forGetter(Cell::users)).apply(i,(p,u)->new Cell(p,new HashMap<>(u))));
	}
	private final List<Cell> cells=new ArrayList<>();
	private static final Codec<LightLeases> CODEC=Cell.CODEC.listOf().xmap(list->{var d=new LightLeases();d.cells.addAll(list);return d;},d->d.cells);
	private static final SavedDataType<LightLeases> TYPE=new SavedDataType<>(ExampleMod.id("summon_lights"),LightLeases::new,CODEC,null);
	private static net.minecraft.world.level.block.state.BlockState light(){return Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL,12);}
	private static final ThreadLocal<Boolean> MUTATING=ThreadLocal.withInitial(()->false);
	private static boolean managedSet(ServerLevel level,BlockPos pos,net.minecraft.world.level.block.state.BlockState state){
		boolean previous=MUTATING.get();MUTATING.set(true);try{return level.setBlockAndUpdate(pos,state);}finally{MUTATING.set(previous);}
	}
	public static void externalChange(ServerLevel level,BlockPos pos,net.minecraft.world.level.block.state.BlockState before,net.minecraft.world.level.block.state.BlockState after){
		if(MUTATING.get()||before==null||before.equals(after))return;
		var data=level.getDataStorage().computeIfAbsent(TYPE);if(data.cells.removeIf(c->c.pos.equals(pos)))data.setDirty();
	}
	public static void initialize(){ServerTickEvents.END_SERVER_TICK.register(server->{
		for(var level:server.getAllLevels()){
			var data=level.getDataStorage().computeIfAbsent(TYPE);var iterator=data.cells.iterator();
			while(iterator.hasNext()){
				var cell=iterator.next();int before=cell.users.size();cell.users.values().removeIf(expiry->expiry<=level.getGameTime());if(before!=cell.users.size())data.setDirty();
				if(!cell.users.isEmpty()||!level.hasChunkAt(cell.pos))continue;
				if(level.getBlockState(cell.pos).equals(light()))managedSet(level,cell.pos,Blocks.AIR.defaultBlockState());
				iterator.remove();data.setDirty();
			}
		}
	});}
	public static void update(ServerPlayer owner){
		var level=(ServerLevel)owner.level();var data=level.getDataStorage().computeIfAbsent(TYPE);var id=owner.getUUID().toString();
		var wanted=new HashSet<BlockPos>();
		for(int i=0;i<3;i++){double a=level.getGameTime()*.04+i*Math.PI*2/3;wanted.add(BlockPos.containing(owner.position().add(Math.cos(a)*1.3,1.3,Math.sin(a)*1.3)));}
		for(var cell:data.cells)if(!wanted.contains(cell.pos)&&cell.users.remove(id)!=null)data.setDirty();
		for(var pos:wanted){
			if(!com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(owner,pos))continue;
			var found=data.cells.stream().filter(c->c.pos.equals(pos)).findFirst().orElse(null);
			if(found!=null){
				if(level.getBlockState(pos).equals(light())){found.users.put(id,level.getGameTime()+25);data.setDirty();}
			}else if(level.getBlockState(pos).isAir()&&managedSet(level,pos,light())){
				var users=new HashMap<String,Long>();users.put(id,level.getGameTime()+25);data.cells.add(new Cell(pos,users));data.setDirty();
			}
		}
	}
	public static void release(ServerPlayer owner){
		for(var level:owner.level().getServer().getAllLevels()){
			var data=level.getDataStorage().computeIfAbsent(TYPE);
			for(var cell:data.cells)if(cell.users.remove(owner.getUUID().toString())!=null)data.setDirty();
		}
	}
	private LightLeases(){}
}
