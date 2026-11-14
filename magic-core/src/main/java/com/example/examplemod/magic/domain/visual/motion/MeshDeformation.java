package com.example.examplemod.magic.domain.visual.motion;
/** Strategy for one physical material family, sampled without a game runtime. */
interface MeshDeformation { float[] deform(String id,String role,int index,float cx,float cy,float cz,float[] v,float t,int stage); }
