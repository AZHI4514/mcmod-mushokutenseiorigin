package com.example.examplemod.client.magic;

/** 姿态随单个渲染状态提取，避免延迟渲染时串到其他玩家。 */
public interface MagicPoseState {
	boolean mushoku$isCasting();
	void mushoku$setCasting(boolean value);
}
