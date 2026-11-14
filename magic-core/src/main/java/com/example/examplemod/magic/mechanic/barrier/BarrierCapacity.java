package com.example.examplemod.magic.mechanic.barrier;
/** Separate physical/magical pools and a shared shell pool retain native float arithmetic. */
public final class BarrierCapacity {
	private float shell,physical,magical;
	public BarrierCapacity(float shell,float physical,float magical){this.shell=shell;this.physical=physical;this.magical=magical;}
	public float shell(){return shell;}public float physical(){return physical;}public float magical(){return magical;}
	public boolean absorbShell(float incoming){shell-=incoming;return shell<=0;}
	public boolean absorbPhysical(float incoming){physical-=incoming;return physical<=0;}
	public boolean absorbMagical(float incoming){magical-=incoming;return magical<=0;}
}
