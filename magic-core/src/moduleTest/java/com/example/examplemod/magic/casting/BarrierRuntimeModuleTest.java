package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.barrier.*;
class BarrierRuntimeModuleTest {
	static class Projectile implements BarrierProjectile.Field {StringBuilder log=new StringBuilder();boolean trident,broken,accept=true;public boolean active(){return true;}public boolean crosses(){log.append('c');return true;}public boolean accepts(){log.append('a');return accept;}public boolean absorb(){log.append('s');return broken;}public boolean trident(){return trident;}public void reflect(){log.append('r');}public void discardProjectile(){log.append('p');}public void discardField(){log.append('f');}public void visual(){log.append('v');}}
	@Test void tridentReflectsBeforeShellDiscard(){var f=new Projectile();f.trident=true;f.broken=true;assertTrue(BarrierProjectile.intercept(f));assertEquals("casrfv",f.log.toString());}
	@Test void regularProjectileDiscardedWithoutReflection(){var f=new Projectile();assertTrue(BarrierProjectile.intercept(f));assertEquals("caspv",f.log.toString());}
	@Test void rejectedKindDoesNotSpendCapacity(){var f=new Projectile();f.accept=false;assertFalse(BarrierProjectile.intercept(f));assertEquals("ca",f.log.toString());}
	static class Movement implements BarrierMovement.Field {boolean crosses=true;StringBuilder log=new StringBuilder();public boolean active(){return true;}public boolean holdsBody(){return true;}public boolean crosses(){return crosses;}public double crossing(){log.append('c');return .5;}public void stopLiving(){log.append('s');}}
	@Test void crossingComputedBeforeVelocityReset(){var f=new Movement();assertEquals(.499,BarrierMovement.clip(f));assertEquals("cs",f.log.toString());}
	@Test void noCrossingLeavesMovementUntouched(){var f=new Movement();f.crosses=false;assertTrue(Double.isNaN(BarrierMovement.clip(f)));assertEquals("",f.log.toString());}
}
