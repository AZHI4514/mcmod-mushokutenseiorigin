package com.example.examplemod.client.magic;

import java.util.ArrayList;
import java.util.List;
import com.example.examplemod.magic.*;
import com.mojang.math.Axis;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

/** Snapshot live state during extraction; the drawing phase holds only immutable values. */
final class SpellVolumeEffects {
	private record Burst(String id, Vec3 at, Vec3 direction, long started) { }
	private record Draw(String id, Vec3 at, float yaw, float pitch, float age, float width, float height, float depth, float opacity, int stage) {
		Draw(String id, Vec3 at, float yaw, float pitch, float age, float width, float height, float depth, float opacity) {
			this(id, at, yaw, pitch, age, width, height, depth, opacity, ReferenceSpellModels.FLIGHT);
		}
		Draw stage(int value) { return new Draw(id, at, yaw, pitch, age, width, height, depth, opacity, value); }
	}
	private static final RenderStateDataKey<List<Draw>> FRAME = RenderStateDataKey.create(() -> "spell_volumes");
	private static final ArrayList<Burst> BURSTS = new ArrayList<>();
	private static Object lastLevel;
	static void receive(SpellVisualPayload packet) {
		var client = Minecraft.getInstance();
		if (client.level == null || SpellDefinition.find(packet.spell()) == null) return;
		if (lastLevel != client.level) { BURSTS.clear(); lastLevel = client.level; }
		if (BURSTS.size() >= 24) BURSTS.removeFirst();
		BURSTS.add(new Burst(packet.spell(), packet.position(), packet.direction(), client.level.getGameTime()));
	}
	static void initialize() {
		LevelRenderEvents.END_EXTRACTION.register(context -> {
			var client = Minecraft.getInstance();
			var level = context.level();
			if (lastLevel != level) { BURSTS.clear(); lastLevel = level; }
			float delta = context.deltaTracker().getGameTimeDeltaPartialTick(false);
			var draws = new ArrayList<Draw>();
			if (level == null) { context.levelState().setData(FRAME, List.of()); return; }
			var camera = context.camera().position();
			for (var player : level.players()) {
				if (player.isUsingItem() || !player.isAlive() || player.distanceToSqr(camera) > 32 * 32) continue;
				int left = player.getAttachedOrElse(MagicAttachments.PREPARE_LEFT, 0);
				int total = player.getAttachedOrElse(MagicAttachments.PREPARE_TOTAL, 0);
				boolean charge = player == client.player && player.getAttachedOrElse(MagicAttachments.CHARGE_MANA, 0) > 0;
				boolean jet = left <= 0 && player.getAttachedOrElse(MagicAttachments.CAST_TICKS, 0) > 0
						&& player.getAttachedOrElse(MagicAttachments.VISUAL_SPELL, "").equals("flamethrower");
				if (left <= 0 && !charge && !jet) continue;
				if (player == client.player && (client.screen != null || !MagicClient.physicallyDown(MagicClient.CAST))) continue;
				var spell = SpellDefinition.find(player.getAttachedOrElse(MagicAttachments.VISUAL_SPELL, ""));
				if (spell == null || !jet && !SpellVisualProfile.condenses(spell)) continue;
				float progress = charge ? 1 : total > 0 ? 1 - Math.clamp((left - delta) / total, 0, 1) : 0;
				float growth = progress * progress * (3 - 2 * progress);
				if (!jet && growth < .025F) continue;
				var look = player.getLookAngle();
				var forward = Vec3.directionFromRotation(0, player.getYRot());
				var right = new Vec3(-forward.z, 0, forward.x);
				var at = player.getPosition(delta).add(0, player.getBbHeight() * .72, 0).add(look.scale(.6)).add(right.scale(.35));
				if (player == client.player && client.options.getCameraType().isFirstPerson())
					at = camera.add(look.scale(.65)).add(right.scale(.28)).add(0, -.25, 0);
				if (jet) {
					var end = at.add(look.scale(16));
					var hit = level.clip(new net.minecraft.world.level.ClipContext(at, end,
							net.minecraft.world.level.ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, player));
					float length = (float) at.distanceTo(hit.getLocation());
					if (length > .05F) draws.add(new Draw(spell.id(), at.add(look.scale(length / 2)), yaw(look), pitch(look), player.tickCount + delta, 1.6F, 1.6F, length, .85F));
					continue;
				}
				float[] size = SpellVisualProfile.handSize(spell.id());
				draws.add(new Draw(spell.id(), at, yaw(look), pitch(look), ReferenceSpellModels.handles(spell.id()) ? progress * 16 : player.tickCount + delta, size[0] * growth, size[1] * growth, size[2] * growth, Math.min(1, growth * 2)).stage(ReferenceSpellModels.FORM));
			}
			var it = BURSTS.iterator();
			while (it.hasNext()) {
				var burst = it.next();
				float age = level.getGameTime() - burst.started + delta;
				float lifetime = switch (burst.id) {
					case "healing", "ex_healing", "detoxification", "antidote_ii" -> 32;
					case "explosion", "ex_flame" -> 18;
					case "water_splash", "icicle_field" -> 28;
					default -> 20;
				};
				if (age >= lifetime || age < 0) { it.remove(); continue; }
				if (burst.at.distanceToSqr(camera) > 48 * 48) continue;
				// These effects already have authoritative visible entities/areas.
				// Their packet adds pixel sparks only, avoiding a second miniature body.
				if (switch (burst.id) {
					case "absolute_zero", "inferno", "flash_over", "magma_gush", "earth_hedgehog", "earth_fortress", "earth_wall", "water_shield", "magic_shield", "physical_shield", "burning_place", "heat_island", "flamethrower", "wind_bind" -> true;
					default -> false;
				}) continue;
				if (ReferenceSpellModels.handles(burst.id)) {
					float w = .5F, h = .5F, d = .5F;
					var at = burst.at;
					float angle = pitch(burst.direction);
					switch (burst.id) {
						case "waterfall" -> { w = 3; h = 4; d = .25F; at = at.add(0, 2, 0); angle = 0; }
						case "flame_pillar" -> { w = d = 1.6F; h = 3; at = at.add(0, 1.5, 0); angle = 0; }
						case "ice_smash" -> { w = h = d = .65F; }
						case "healing", "ex_healing", "detoxification", "antidote_ii" -> { w = d = burst.id.equals("ex_healing") ? 1.7F : 1.3F; h = 1.8F; at = at.add(0, .9, 0); angle = 0; }
						case "water_splash" -> { w = d = 12; h = 1.8F; at = at.add(0, .27, 0); angle = 0; }
						case "icicle_field" -> { w = d = 11.5F; h = 1.4F; at = at.add(0, .7, 0); angle = 0; }
						case "blast", "bora_blast" -> {float length=burst.id.equals("blast")?10:20;w=h=length*1.1547F;d=length;at=at.add(burst.direction.scale(length/2));}
						case "sonic_blast" -> {w=h=22.4F;d=16;at=at.add(burst.direction.scale(8));}
						case "frost_nova" -> {w=h=d=16;angle=0;}
						case "lightning" -> {w=d=4;h=32;at=at.add(0,16,0);angle=0;}
						case "electric" -> {w=d=1.2F;h=1.8F;angle=0;}
						case "cumulonimbus" -> {w=d=8;h=32;at=at.add(0,16,0);angle=0;}
						case "exodus_flame" -> {w=h=d=8;angle=0;}
					case "explosion" -> { w = h = d = 5.5F; angle = 0; }
						case "ex_flame" -> { w = h = d = 3; }
						case "ice_pillar", "ice_lance", "earth_lance" -> { w = d = .95F; h = 3; at = at.add(0, 1.5, 0); angle = 0; }
						case "ice_fortress" -> { w = 5; h = 3; d = 1; at = at.add(0, 1.5, 0); angle = 0; }
						case "wind_blast" -> { w = h = 1.8F; d = 3; at = at.add(0, 1, 0).add(burst.direction.scale(.8)); }
						case "heat_hand" -> { w = d = .8F; h = 1.2F; at = at.add(0, .6, 0); angle = 0; }
						case "flame_slice", "sonic_boom", "wind_slice", "icicle_edge" -> { w = 2.4F; h = .6F; d = .6F; }
						case "earth_pillar" -> { w = h = d = 1.4F; angle = 0; }
						default -> { }
					}
					draws.add(new Draw(burst.id, at, (burst.id.equals("cumulonimbus")||burst.id.equals("lightning")||burst.id.equals("electric"))?0:yaw(burst.direction), angle, age, w, h, d, 1).stage(ReferenceSpellModels.IMPACT));
					continue;
				}
				float progress = age / lifetime, ease = 1 - (1 - progress) * (1 - progress);
				float size = .22F + ease * .65F;
				float w = size, h = size, d = size;
				var at = burst.at;
				float pitch = pitch(burst.direction);
				switch (burst.id) {
					case "exorcist_ray","exorcist_judge","holy_ray" -> {w=d=burst.id.equals("holy_ray")?4:1;h=8;at=at.add(0,2,0);pitch=0;}
					case "exorcist_burst","sacred_nova","god_purge" -> {w=d=(burst.id.equals("god_purge")?96:burst.id.equals("sacred_nova")?32:8)*ease;h=2;pitch=0;}
					case "howling" -> {w=h=4+ease*8;d=12;at=at.add(burst.direction.scale(6));}
					case "teleportation_magic" -> {w=d=5;h=3;at=at.add(0,1.5,0);pitch=0;}
					case "healing", "ex_healing", "detoxification", "antidote_ii", "antidote_iii" -> { w = d = burst.id.equals("ex_healing") ? 1.7F : 1.3F; h = 1.8F; at = at.add(0, .85 + ease * .25, 0); pitch = 0; }
					case "water_splash" -> { w = d = .6F + ease * 11.4F; h = .25F + (float) Math.sin(progress * Math.PI) * 1.5F; at = at.add(0, h * .35, 0); pitch = 0; }
					case "icicle_field" -> { w = d = 11.5F; h = .3F + Math.min(1, age / 7) * 1.2F; at = at.add(0, h * .5, 0); pitch = 0; }
					case "explosion", "ex_flame" -> { w = h = d = .5F + ease * (burst.id.equals("explosion") ? 5 : 3); }
					case "ice_pillar", "ice_lance", "earth_lance" -> { w = d = .95F; h = Math.min(3, age * .6F); at = at.add(0, h / 2, 0); pitch = 0; }
					case "ice_fortress" -> { w = 5; h = Math.min(3, age * .6F); d = 1; at = at.add(0, h / 2, 0); pitch = 0; }
					case "waterfall" -> { w = d = 1; h = .4F + ease * 1.3F; at = at.add(0, h / 2, 0); pitch = 0; }
					case "wind_blast" -> { w = h = .4F + ease * 2; d = 1 + ease * 4; at = at.add(0, 1.1, 0).add(burst.direction.scale(1 + ease * 2)); }
					case "heat_hand" -> { w = d = .8F; h = 1.2F; at = at.add(0, .7, 0).add(burst.direction.scale(.7)); pitch = 0; }
					case "flame_slice", "sonic_boom", "wind_slice", "icicle_edge" -> { w = 1 + ease * 1.5F; h = .3F; d = .5F; }
					case "rock_bullet", "stone_cannon", "earth_pillar" -> { w = h = d = .25F + ease * .5F; at = at.add(0, -.25 * progress * progress, 0); }
					default -> { }
				}
				float fade = Math.min(1, age / 2) * (1 - progress) * (1 - progress);
				draws.add(new Draw(burst.id, at, yaw(burst.direction), pitch, age, w, h, d, fade));
			}
			if (client.player != null) {
				for(var entity:level.getEntities(client.player,client.player.getBoundingBox().inflate(48),e->e.getAttachedOrElse(MagicAttachments.AURA_ACTIVE,false)||java.util.Set.of("spirit_summon","lamplight_spirits","monster_summon","necromancy","magic_core").contains(e.getAttachedOrElse(MagicAttachments.VISUAL_SPELL,"")))){if(draws.size()>160)break;var id=entity.getAttachedOrElse(MagicAttachments.AURA_ACTIVE,false)?"saint_dragon_battle_aura":entity.getAttachedOrElse(MagicAttachments.VISUAL_SPELL,"");if(id.equals("lamplight_spirits")){for(int i=0;i<3;i++){double a=(entity.tickCount+delta)*.04+i*Math.PI*2/3;draws.add(new Draw(id,entity.getPosition(delta).add(Math.cos(a)*1.3,.1+Math.sin(a*1.7)*.2,Math.sin(a)*1.3),0,0,entity.tickCount+delta,.24F,.24F,.24F,.95F));}}else draws.add(new Draw(id,entity.getPosition(delta).add(0,entity.getBbHeight()/2,0),0,0,entity.tickCount+delta,entity.getBbWidth()+.35F,entity.getBbHeight()+.15F,entity.getBbWidth()+.35F,.7F));}
				int visited = 0;
				if(client.player.getAttachedOrElse(MagicAttachments.ARC_BOUND_VISUAL,false))draws.add(new Draw("electric",client.player.getPosition(delta).add(0,client.player.getBbHeight()/2,0),0,0,client.player.tickCount+delta,client.player.getBbWidth()+.3F,client.player.getBbHeight(),client.player.getBbWidth()+.3F,.7F));
				if(client.player.getAttachedOrElse(MagicAttachments.AURA_ACTIVE,false))draws.add(new Draw("saint_dragon_battle_aura",client.player.getPosition(delta).add(0,client.player.getBbHeight()/2,0),0,0,client.player.tickCount+delta,1,1.9F,1,.7F));
				int arcVisited=0;
				for(var entity:level.getEntities(client.player,client.player.getBoundingBox().inflate(48),e->e instanceof net.minecraft.world.entity.LivingEntity&&e.getAttachedOrElse(MagicAttachments.ARC_BOUND_VISUAL,false))){if(++arcVisited>32)break;draws.add(new Draw("electric",entity.getPosition(delta).add(0,entity.getBbHeight()/2,0),0,0,entity.tickCount+delta,entity.getBbWidth()+.3F,entity.getBbHeight(),entity.getBbWidth()+.3F,.7F));}
				if (client.player.getAttachedOrElse(MagicAttachments.WIND_BOUND_VISUAL,false)) draws.add(new Draw("wind_bind", client.player.getPosition(delta).add(0, client.player.getBbHeight()/2, 0), 0, 0, client.player.tickCount + delta, client.player.getBbWidth()+.65F, client.player.getBbHeight()+.25F, client.player.getBbWidth()+.65F, 1));
				for (var entity : level.getEntities(client.player, client.player.getBoundingBox().inflate(48), e -> e instanceof net.minecraft.world.entity.LivingEntity living && living.getAttachedOrElse(MagicAttachments.WIND_BOUND_VISUAL,false))) {
					if (++visited > 64) break;
					var living = (net.minecraft.world.entity.LivingEntity) entity;
					if (living.getAttachedOrElse(MagicAttachments.WIND_BOUND_VISUAL,false)) draws.add(new Draw("wind_bind", living.getPosition(delta).add(0, living.getBbHeight() / 2, 0), 0, 0, living.tickCount + delta, living.getBbWidth() + .65F, living.getBbHeight() + .25F, living.getBbWidth() + .65F, 1F));
				}
			}
			context.levelState().setData(FRAME, List.copyOf(draws));
		});
		LevelRenderEvents.COLLECT_SUBMITS.register(context -> {
			var camera = context.levelState().cameraRenderState.pos;
			var poses = context.poseStack();
			for (var draw : context.levelState().getDataOrDefault(FRAME, List.of())) {
				poses.pushPose();
				poses.translate(draw.at.x - camera.x, draw.at.y - camera.y, draw.at.z - camera.z);
				poses.mulPose(Axis.YP.rotationDegrees(draw.yaw));
				poses.mulPose(Axis.XP.rotationDegrees(-draw.pitch));
				if(LayeredPixelMeshRenderer.handles(draw.id))LayeredPixelMeshRenderer.submit(poses,context.submitNodeCollector(),draw.id,draw.age,draw.width,draw.height,draw.depth,draw.opacity,draw.stage==ReferenceSpellModels.IMPACT);
				else if (ReferenceSpellModels.handles(draw.id))
					ReferenceSpellModels.submit(poses, context.submitNodeCollector(), draw.id, draw.age, draw.width, draw.height, draw.depth, draw.opacity, draw.stage);
				else
					SpellMeshModel.submit(poses, context.submitNodeCollector(), draw.id, draw.age, draw.width, draw.height, draw.depth, draw.opacity);
				poses.popPose();
			}
		});
	}
	private static float yaw(Vec3 direction) { return (float) Math.toDegrees(Math.atan2(direction.x, direction.z)); }
	private static float pitch(Vec3 direction) { return (float) Math.toDegrees(Math.atan2(direction.y, direction.horizontalDistance())); }
	private SpellVolumeEffects() { }
}
