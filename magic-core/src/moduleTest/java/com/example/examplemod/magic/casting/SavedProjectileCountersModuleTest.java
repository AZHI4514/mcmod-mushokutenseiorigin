package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.SavedProjectileCounters;
import com.example.examplemod.magic.spell.earth.stone_cannon.StoneCannonRules;
class SavedProjectileCountersModuleTest {
 @Test void ageHasNoNewUpperLimit(){assertEquals(0,SavedProjectileCounters.age(Integer.MIN_VALUE));assertEquals(0,SavedProjectileCounters.age(-1));assertEquals(Integer.MAX_VALUE,SavedProjectileCounters.age(Integer.MAX_VALUE));}
 @Test void terrainBudgetRemainsClampedTo64(){assertEquals(0,SavedProjectileCounters.brokenBlocks(-1));assertEquals(63,SavedProjectileCounters.brokenBlocks(63));assertEquals(64,SavedProjectileCounters.brokenBlocks(64));assertEquals(64,SavedProjectileCounters.brokenBlocks(Integer.MAX_VALUE));}
 @Test void defaultsAndPaidManaFloorStayIndependent(){assertEquals(0,SavedProjectileCounters.age(0));assertEquals(0,SavedProjectileCounters.brokenBlocks(0));assertEquals(10,StoneCannonRules.paidMana(10));assertEquals(10,StoneCannonRules.paidMana(Integer.MIN_VALUE));assertEquals(Integer.MAX_VALUE,StoneCannonRules.paidMana(Integer.MAX_VALUE));}
 @Test void frozenArithmeticMatchesRandomIntegers(){var random=new java.util.Random(1113);for(int i=0;i<10000;i++){int n=random.nextInt();assertEquals(Math.max(0,n),SavedProjectileCounters.age(n));assertEquals(Math.min(64,Math.max(0,n)),SavedProjectileCounters.brokenBlocks(n));}}
}
