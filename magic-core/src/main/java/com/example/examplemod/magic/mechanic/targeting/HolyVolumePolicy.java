package com.example.examplemod.magic.mechanic.targeting;
/** Only area policies need target geometry; single-target policies have no empty shape method. */
public interface HolyVolumePolicy extends HolyInstantPolicy {
	boolean shape(double x,double y,double z,double ax,double ay,double az,double length);
}
