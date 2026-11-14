package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.WindFieldRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.barrier.illusion_barrier.*;

import java.util.WeakHashMap;
import net.minecraft.server.level.*;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.phys.*;

/** Fixed, nonphysical projection field. Rendering never changes blocks, collision or entity tracking. */
public final class IllusionBarrierAdapter {
	private static final class State {long end,next;}
	private static final class Runtime {
	private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,State>> TYPE =
		net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("illusion_barrier_adapter_runtime"),
			builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,State>()));
	}

	/** The level owns this transient index; original explicit ValueInput/ValueOutput remains authoritative. */
	private static java.util.Map<MagicAreaEntity,State> states(MagicAreaEntity area) {
		return area.level().getAttachedOrCreate(Runtime.TYPE);
	}
	public static boolean cast(ServerPlayer player,MagicAim aim){
		var at=WindFieldRuntimeAdapter.target(player,SpellDefinition.ILLUSION_BARRIER,aim);if(at==null)return false;
		var area=new MagicAreaEntity(MagicSystem.MAGIC_AREA,player.level());area.configure(player,SpellDefinition.ILLUSION_BARRIER,aim);area.setPos(at);
		if(!SpellRaycastAdapter.loaded(player.level(),bounds(area))||MagicSystem.state(player).mana()<2||!AreaRuntimeAdapter.replaceExpansion(player,area))return false;
		player.setAttached(MagicAttachments.STATE,MagicSystem.state(player).spendMana(2));
		var s=new State();s.end=player.level().getGameTime()+2400;s.next=player.level().getGameTime()+20;states(area).put(area,s);return true;
	}
	public static AABB bounds(MagicAreaEntity area){return new AABB(area.getX()-12,area.getY(),area.getZ()-12,area.getX()+12,area.getY()+12,area.getZ()+12);}
	public static boolean contains(MagicAreaEntity area,Vec3 at){double dx=at.x-area.getX(),dz=at.z-area.getZ();return IllusionRules.contains(dx,dz,at.y,area.getY());}
	public static void tick(MagicAreaEntity area){
		if(!(area.level() instanceof ServerLevel level))return;var s=states(area).get(area);long now=level.getGameTime();
		com.example.examplemod.magic.mechanic.field.ExpiringFieldTick.tick(new com.example.examplemod.magic.mechanic.field.ExpiringFieldTick.World(){
			public boolean statePresent(){return s!=null;}public boolean expired(){return now>=s.end;}public boolean ownerPresent(){return area.getOwner() instanceof ServerPlayer;}private ServerPlayer owner(){return (ServerPlayer)area.getOwner();}
			public boolean alive(){return owner().isAlive();}public boolean sameLevel(){return owner().level()==level;}public boolean adopt(){return AreaRuntimeAdapter.adoptExpansion(owner(),area);}
			public void removeState(){states(area).remove(area);}public void discard(){area.discard();}
			public void update(){IllusionPulse.tick(new IllusionPulse.World(){public boolean due(){return now>=s.next;}public boolean pay(){var state=MagicSystem.state(owner());if(state.mana()<2)return false;owner().setAttached(MagicAttachments.STATE,state.spendMana(2));return true;}public void discard(){area.discard();}public void schedule(){s.next=now+20;}public void visual(){IllusionBarrierAdapter.visual(level,area,now);}});}
		});
	}
	private static void visual(ServerLevel level,MagicAreaEntity area,long now){
		if(now%10==0){var random=level.getRandom();for(int i=0;i<14;i++){double a=random.nextDouble()*Math.PI*2;ParticlePacketAdapter.send(level,SpellParticles.accent(area.spell()),area.getX()+Math.cos(a)*12,area.getY()+random.nextDouble()*12,area.getZ()+Math.sin(a)*12,1,.02,.02,.02,.002);}}
	}
	public static void save(MagicAreaEntity area,ValueOutput out){var s=states(area).get(area);if(s!=null){out.putLong("IllusionEnd",s.end);out.putLong("IllusionNext",s.next);}}
	public static void load(MagicAreaEntity area,ValueInput in){var s=new State();s.end=in.getLongOr("IllusionEnd",0);s.next=in.getLongOr("IllusionNext",Long.MAX_VALUE);states(area).put(area,s);}
	private IllusionBarrierAdapter(){}
}
