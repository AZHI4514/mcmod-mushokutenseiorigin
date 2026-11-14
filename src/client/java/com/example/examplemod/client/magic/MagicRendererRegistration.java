package com.example.examplemod.client.magic;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import com.example.examplemod.magic.MagicSystem;
/** Renderer registration only; no input, network or player state. */
final class MagicRendererRegistration {
	static void initialize() {
		EntityRendererRegistry.register(MagicSystem.WATER_BALL, context -> new WaterBallRenderer(context, "water_ball"));
		EntityRendererRegistry.register(MagicSystem.FIREBALL, context -> new WaterBallRenderer(context, "fireball"));
		EntityRendererRegistry.register(MagicSystem.WIND_BALL, context -> new WaterBallRenderer(context, "wind_ball"));
		EntityRendererRegistry.register(MagicSystem.ROCK_BULLET, context -> new WaterBallRenderer(context, "rock_bullet"));
		EntityRendererRegistry.register(MagicSystem.WATER_ARROW, context -> new WaterBallRenderer(context, "water_arrow"));
		EntityRendererRegistry.register(MagicSystem.ICE_SMASH, context -> new WaterBallRenderer(context, "ice_smash"));
		EntityRendererRegistry.register(MagicSystem.ICE_BLADE, context -> new WaterBallRenderer(context, "ice_blade"));
		EntityRendererRegistry.register(MagicSystem.MAGIC_AREA, MagicAreaRenderer::new);
		EntityRendererRegistry.register(MagicSystem.WATER_CANNON,context->new SweptSpellRenderer(context,"water_cannon"));
		EntityRendererRegistry.register(MagicSystem.WATER_SPLASH,context->new SweptSpellRenderer(context,"water_splash"));
		EntityRendererRegistry.register(MagicSystem.ICICLE_FIELD,context->new SweptSpellRenderer(context,"icicle_field"));
		EntityRendererRegistry.register(MagicSystem.TORNADO_IMPACT,context->new SweptSpellRenderer(context,"tornado_impact"));
		EntityRendererRegistry.register(MagicSystem.EXODUS_FLAME,context->new SweptSpellRenderer(context,"exodus_flame"));
		EntityRendererRegistry.register(MagicSystem.EX_FLAME,context->new SweptSpellRenderer(context,"ex_flame"));
		EntityRendererRegistry.register(MagicSystem.SONIC_BOOM,context->new SweptSpellRenderer(context,"sonic_boom"));
		EntityRendererRegistry.register(MagicSystem.STONE_CANNON,context->new SweptSpellRenderer(context,"stone_cannon"));
		EntityRendererRegistry.register(MagicSystem.PHASE_TWO, DirectionalSpellRenderer::new);
		EntityRendererRegistry.register(MagicSystem.FLAME_SLICE, FlameSliceRenderer::new);
	}
	private MagicRendererRegistration() {}
}
