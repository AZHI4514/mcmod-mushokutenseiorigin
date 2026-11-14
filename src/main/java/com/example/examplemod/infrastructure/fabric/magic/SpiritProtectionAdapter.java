package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.summon.lamplight_spirits.LamplightUpkeep;
import com.example.examplemod.magic.spell.special.necromancy.NecromancyRules;
import com.example.examplemod.magic.spell.summon.spirit_summon.SpiritSummonRules;
import com.example.examplemod.magic.spell.summon.monster_summon.MonsterSummonRules;

import java.util.*;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import com.example.examplemod.ExampleMod;

import static com.example.examplemod.infrastructure.fabric.magic.SummonRuntimeAdapter.*;
final class SpiritProtectionAdapter {
	public static float protect(ServerPlayer player,net.minecraft.world.damagesource.DamageSource source,float amount){
		var id=player.getAttachedOrCreate(SLOTS).get("spirit_summon");if(id==null)return amount;
		try{var e=((ServerLevel)player.level()).getEntity(UUID.fromString(id));if(e!=null&&e.isAlive()&&!e.isRemoved()&&e.distanceToSqr(player)<=256&&e.getAttachedOrCreate(EXPIRY)>player.level().getGameTime()&&e.getAttachedOrCreate(COMMAND)==3){
			boolean magic=source.getDirectEntity() instanceof net.minecraft.world.entity.projectile.Projectile p&&AreaRuntimeAdapter.isMagic(p)||source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_ARMOR)&&!source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY);
			long now=player.level().getGameTime(),ready=e.getAttachedOrElse(MagicAttachments.COUNTER_UNTIL,0L);
			if(magic&&now>=ready){e.setAttached(MagicAttachments.COUNTER_UNTIL,now+40);return SpiritSummonRules.protectedDamage(amount);}
		}}catch(IllegalArgumentException ignored){}
		return amount;
	}

	private SpiritProtectionAdapter(){}
}
