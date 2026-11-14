package com.example.examplemod.client.magic;

import java.util.*;
import com.example.examplemod.magic.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.*;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;

/** Local surrounding terrain is projected onto the shell. No fake block packets or client collisions. */
public final class IllusionProjection {
	public record Cell(Vec3 offset,BlockModelRenderState model,int light){}
	private record Cached(long time,List<Cell> cells){}
	private static final WeakHashMap<MagicAreaEntity,Cached> CACHE=new WeakHashMap<>();
	private record Mask(Vec3 center){boolean contains(BlockPos pos){var d=Vec3.atCenterOf(pos).subtract(center);return d.horizontalDistanceSqr()<144&&d.y>=0&&d.y<12;}}
	private static volatile List<Mask> MASKS=List.of();
	private static Object lastLevel;
	public static boolean masked(BlockPos pos){for(var mask:MASKS)if(mask.contains(pos))return true;return false;}
	public static void initialize(){net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(mc->{
		if(mc.level==null||mc.player==null){MASKS=List.of();CACHE.clear();lastLevel=null;return;}
		if(lastLevel!=mc.level){MASKS=List.of();CACHE.clear();lastLevel=mc.level;}
		var next=mc.level.getEntitiesOfClass(MagicAreaEntity.class,mc.player.getBoundingBox().inflate(160),a->a.spell()==SpellDefinition.ILLUSION_BARRIER&&exterior(a)).stream().map(a->new Mask(a.position())).toList();
		if(!next.equals(MASKS)){var dirty=new ArrayList<>(MASKS);dirty.addAll(next);MASKS=List.copyOf(next);for(var mask:dirty){var at=BlockPos.containing(mask.center);mc.levelRenderer.setBlocksDirty(at.getX()-13,at.getY()-1,at.getZ()-13,at.getX()+13,at.getY()+13,at.getZ()+13);}}
	});}
	private static boolean privileged(){var player=Minecraft.getInstance().player;return player==null||player.isSpectator()||player.permissions().hasPermission(net.minecraft.server.permissions.Permissions.COMMANDS_GAMEMASTER);}
	public static boolean exterior(MagicAreaEntity area){var camera=Minecraft.getInstance().gameRenderer.getMainCamera().position();return !privileged()&&!contains(area,camera);}
	public static boolean hidden(Entity entity){
		var mc=Minecraft.getInstance();if(mc.level==null||privileged()||entity instanceof MagicAreaEntity)return false;
		for(var field:mc.level.getEntitiesOfClass(MagicAreaEntity.class,entity.getBoundingBox().inflate(25),a->a.spell()==SpellDefinition.ILLUSION_BARRIER)){
			if(!exterior(field)||!contains(field,entity.getBoundingBox().getCenter()))continue;
			var owner=field.getOwner();if(entity==owner||owner!=null&&entity.isAlliedTo(owner))continue;return true;
		}return false;
	}
	public static List<Cell> extract(MagicAreaEntity area,BlockModelResolver resolver){
		if(!exterior(area))return List.of();long now=area.level().getGameTime();var old=CACHE.get(area);if(old!=null&&now-old.time<40)return old.cells;
		var cells=new ArrayList<Cell>();var occupied=new HashSet<BlockPos>();
		for(int a=0;a<96;a++){
			double angle=a*Math.PI/48;int x=(int)Math.round(Math.cos(angle)*12),z=(int)Math.round(Math.sin(angle)*12);
			for(int y=0;y<12;y++){
				var local=new BlockPos(x,y,z);if(!occupied.add(local))continue;
				// Sample the matching column outside the field, so hidden construction is never used as the disguise.
				var sample=BlockPos.containing(area.position().add(Math.cos(angle)*14,y,Math.sin(angle)*14));if(!area.level().hasChunkAt(sample))continue;
				var state=area.level().getBlockState(sample);if(state.isAir()||state.hasBlockEntity())continue;
				var model=new BlockModelRenderState();resolver.update(model,state,BlockDisplayContext.create());
				cells.add(new Cell(new Vec3(x,y,z),model,((area.level().getBrightness(net.minecraft.world.level.LightLayer.BLOCK,sample)<<4)|(area.level().getBrightness(net.minecraft.world.level.LightLayer.SKY,sample)<<20))));
			}
		}
		var result=List.copyOf(cells);CACHE.put(area,new Cached(now,result));return result;
	}
	public static void submit(List<Cell> cells,PoseStack poses,SubmitNodeCollector collector){for(var cell:cells){poses.pushPose();poses.translate(cell.offset.x,cell.offset.y,cell.offset.z);cell.model.submit(poses,collector,cell.light,net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,0);poses.popPose();}}
	private static boolean contains(MagicAreaEntity area,net.minecraft.world.phys.Vec3 at){return com.example.examplemod.magic.spell.barrier.illusion_barrier.IllusionRules.contains(at.x-area.getX(),at.z-area.getZ(),at.y,area.getY());}

	private IllusionProjection(){}
}
