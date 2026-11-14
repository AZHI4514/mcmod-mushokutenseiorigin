package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;

import java.util.*;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.phys.*;
import com.example.examplemod.ExampleMod;

final class FortressBarrierEffect implements PoweredBarrierEffect {
	public int coreCount(){return 1;}public double radius(){return 24;}public boolean hemisphere(){return true;}
	public boolean prepare(ServerPlayer owner,MagicAim aim,MagicAreaEntity area,PoweredBarrierRuntimeAdapter.State s){
		var core=MagicCores.nearest(owner,owner.position(),6);s.cores=List.of(core.getUUID());s.capacity=600;area.setPos(core.position());return true;
	}
	public boolean maintain(ServerLevel level,MagicAreaEntity area,PoweredBarrierRuntimeAdapter.State s,ServerPlayer owner,long now){
		return com.example.examplemod.magic.spell.barrier.fortress_barrier.FortressUpkeep.maintain(new com.example.examplemod.magic.spell.barrier.fortress_barrier.FortressUpkeep.World(){
			public boolean due(){return now>=s.next;}public void schedule(){s.next=now+20;}public boolean takeEnergy(){return MagicCores.take(level.getEntity(s.cores.getFirst()),10);}
			public void clearDry(){s.dry=0;}public void repair(){s.capacity=com.example.examplemod.magic.spell.barrier.fortress_barrier.FortressUpkeep.repair(s.capacity);}
			public long drySince(){return s.dry;}public long now(){return now;}public void beginDry(){s.dry=now;}
		});
	}
}
