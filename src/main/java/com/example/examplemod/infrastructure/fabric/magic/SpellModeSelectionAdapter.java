package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.PreparationRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.field.ModeSelection;
import com.example.examplemod.magic.spell.mixed.nuclear_explosion.NuclearExplosionRules;
import com.example.examplemod.bootstrap.SpellModeComposition;
import java.util.HashMap;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import com.example.examplemod.ExampleMod;
/** Player mode selection, persistent keys and its command; no field world updates. */
public final class SpellModeSelectionAdapter {
	private static final AttachmentType<java.util.Map<String,Integer>> MODES=AttachmentRegistry.create(ExampleMod.id("spell_modes"),b->b.initializer(()->java.util.Map.<String,Integer>of()).persistent(Codec.unboundedMap(Codec.STRING,Codec.INT)).copyOnDeath());
	public static boolean selectable(SpellDefinition s){return ModeSelection.selectable(s);}
	public static int mode(ServerPlayer player,SpellDefinition s){return ModeSelection.normalized(player.getAttachedOrCreate(MODES).getOrDefault(s.id(),0),SpellModeComposition.count(s));}
	public static void cycle(ServerPlayer player,SpellDefinition s){
		if(!selectable(s)||PreparationRuntimeAdapter.active(player))return;
		int count=SpellModeComposition.count(s);
		var map=new HashMap<>(player.getAttachedOrCreate(MODES));int mode=ModeSelection.next(mode(player,s),count);map.put(s.id(),mode);if(s.id().equals("nuclear_explosion"))map.put("nuclear_extra",1000*mode);player.setAttached(MODES,java.util.Map.copyOf(map));
		player.sendSystemMessage(Component.translatable("magic.mushokutenseiorigin.mode",Component.translatable("spell.mushokutenseiorigin."+s.id()),Component.translatable("magic.mushokutenseiorigin.mode_"+s.id()+"_"+mode)),true);
	}
	public static int cost(ServerPlayer player,SpellDefinition s){return s.id().equals("nuclear_explosion")?NuclearExplosionRules.cost(player.getAttachedOrCreate(MODES).getOrDefault("nuclear_extra",1000*mode(player,s))):s.manaCost();}
	public static void initialize(){net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT.register((d,r,e)->d.register(net.minecraft.commands.Commands.literal("mtnuclear").then(net.minecraft.commands.Commands.argument("extra_mp",com.mojang.brigadier.arguments.IntegerArgumentType.integer(0,3000)).executes(c->{var player=c.getSource().getPlayerOrException();if(PreparationRuntimeAdapter.active(player))return 0;var map=new HashMap<>(player.getAttachedOrCreate(MODES));map.put("nuclear_extra",com.mojang.brigadier.arguments.IntegerArgumentType.getInteger(c,"extra_mp"));player.setAttached(MODES,java.util.Map.copyOf(map));return 1;}))));}
	private SpellModeSelectionAdapter() {}
}
