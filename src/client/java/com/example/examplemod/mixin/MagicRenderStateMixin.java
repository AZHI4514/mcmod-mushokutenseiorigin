package com.example.examplemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import com.example.examplemod.client.magic.MagicPoseState;

@Mixin(HumanoidRenderState.class)
public abstract class MagicRenderStateMixin implements MagicPoseState {
	@Unique private boolean mushoku$casting;
	public boolean mushoku$isCasting() { return mushoku$casting; }
	public void mushoku$setCasting(boolean value) { mushoku$casting = value; }
}
