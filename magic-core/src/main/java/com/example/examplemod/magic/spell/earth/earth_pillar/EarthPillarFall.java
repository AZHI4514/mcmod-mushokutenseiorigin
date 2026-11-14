package com.example.examplemod.magic.spell.earth.earth_pillar;
/** Inclusive contacts preserve targets touching the same stop plane as terrain. */
public final class EarthPillarFall {
 public static final double SPEED=.5;
 public static boolean contactDue(double contact,double stop,boolean claimed){return contact<=Math.min(SPEED,stop)&&!claimed;}
 public static boolean stopped(double stop){return stop<=SPEED;}
 public static double movement(double stop){return stopped(stop)?Math.max(0,stop):SPEED;}
 private EarthPillarFall() { }
}
