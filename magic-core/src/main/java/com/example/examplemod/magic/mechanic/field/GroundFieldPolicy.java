package com.example.examplemod.magic.mechanic.field;
/** Strategy for the independently specified fire/ground-field timing and volume. */
public interface GroundFieldPolicy {
	double radius();double height();int damagePulse(long age);boolean warning(long age);
	default double visualLift(){return 0;}
	default boolean continuousContacts(){return false;}
	default boolean wall(){return false;}
	default boolean perTickAttack(){return false;}
	default boolean mayClaim(Integer previous,int pulse){return (previous==null?-1:previous)!=pulse;}
	default boolean cadenceRequired(int pulse){return false;}
	default int claimedValue(Integer previous,int pulse){return pulse;}
	default float damage(int pulse,float base){return base;}
}

