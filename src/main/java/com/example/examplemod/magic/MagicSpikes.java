package com.example.examplemod.magic;

import java.util.*;
import com.example.examplemod.ExampleMod;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.saveddata.*;

/** Vanilla pointed dripstone with persisted, identity-checked expiry and no terrain replacement. */
public final class MagicSpikes extends SavedData {
	private record Entry(BlockPos pos,long expiry,BlockState state) {
		static final Codec<Entry> CODEC=RecordCodecBuilder.create(i->i.group(BlockPos.CODEC.fieldOf("pos").forGetter(Entry::pos),Codec.LONG.fieldOf("expiry").forGetter(Entry::expiry),BlockState.CODEC.fieldOf("state").forGetter(Entry::state)).apply(i,Entry::new));
	}
	private final List<Entry> entries=new ArrayList<>();
	private static final Codec<MagicSpikes> CODEC=Entry.CODEC.listOf().xmap(list->{var data=new MagicSpikes();data.entries.addAll(list);return data;},data->data.entries);
	private static final SavedDataType<MagicSpikes> TYPE=new SavedDataType<>(ExampleMod.id("magic_spikes"),MagicSpikes::new,CODEC,null);
	public static void initialize() {
		ServerTickEvents.END_SERVER_TICK.register(server->{for(var level:server.getAllLevels()) {
			var data=level.getDataStorage().computeIfAbsent(TYPE);var it=data.entries.iterator();
			while(it.hasNext()){var entry=it.next();if(entry.expiry>level.getGameTime()||!level.hasChunkAt(entry.pos))continue;
				if(level.getBlockState(entry.pos).equals(entry.state))level.setBlock(entry.pos,Blocks.AIR.defaultBlockState(),2);
				it.remove();data.setDirty();
			}
		}});
	}
	public static boolean raise(ServerPlayer owner,BlockPos feet) {
		var level=(ServerLevel)owner.level();
		for(int y=0;y<3;y++)if(!MagicTerrain.mayChange(owner,feet.above(y))||!level.getBlockState(feet.above(y)).isAir())return false;
		var data=level.getDataStorage().computeIfAbsent(TYPE);
		for(int y=0;y<3;y++) {
			var pos=feet.above(y);var state=Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(PointedDripstoneBlock.TIP_DIRECTION,Direction.UP).setValue(PointedDripstoneBlock.THICKNESS,y==2?DripstoneThickness.TIP:y==1?DripstoneThickness.FRUSTUM:DripstoneThickness.BASE);
			level.setBlock(pos,state,2);data.entries.add(new Entry(pos,level.getGameTime()+200,state));
		}
		data.setDirty();return true;
	}
	private MagicSpikes() { }
}
