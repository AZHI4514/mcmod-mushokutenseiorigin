package com.example.examplemod.magic.domain.visual;
import java.util.Map;
/** Visual dimensions only. Never used as a damage or collision volume. */
public record ProjectileMeshSpec(float width,float height,float depth,float offset) {
	private static final ProjectileMeshSpec SWEPT_DEFAULT=new ProjectileMeshSpec(.45F,.45F,1.4F,0);
	private static final Map<String,ProjectileMeshSpec> SWEPT=Map.of(
		"tornado_impact",new ProjectileMeshSpec(1.5F,1.5F,3,0),"exodus_flame",new ProjectileMeshSpec(3,3,3,0),
		"ex_flame",new ProjectileMeshSpec(2,2,2,0),"sonic_boom",new ProjectileMeshSpec(3,1,.25F,0),"stone_cannon",new ProjectileMeshSpec(.38F,.38F,.55F,0));
	private static final Map<String,ProjectileMeshSpec> BASIC=Map.of(
		"water_ball",cube(.42F),"wind_ball",cube(.45F),"fireball",cube(.45F),"ice_smash",cube(.6F),"rock_bullet",cube(.4F),
		"ice_blade",new ProjectileMeshSpec(.14F,.65F,.95F,0),"water_arrow",new ProjectileMeshSpec(.16F,.16F,.65F,0));
	private static ProjectileMeshSpec cube(float size){return new ProjectileMeshSpec(size,size,size,size/2);}
	public static ProjectileMeshSpec basic(String id){return BASIC.getOrDefault(id,cube(.25F));}
	public static ProjectileMeshSpec swept(String id){return SWEPT.getOrDefault(id,SWEPT_DEFAULT);}
	public static float carrierPitch(boolean falling,boolean stationary,boolean shield,double y,double horizontal){return falling||stationary&&!shield?0:(float)Math.toDegrees(Math.atan2(y,horizontal));}
	public static float carrierOffset(boolean stationary,boolean shield,boolean earthPillar,float height){return stationary&&!shield||earthPillar?height/2:0;}
}
