package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.summon.lamplight_spirits.LamplightUpkeep;
import com.example.examplemod.magic.spell.special.necromancy.NecromancyRules;
import com.example.examplemod.magic.spell.summon.spirit_summon.SpiritSummonRules;
import com.example.examplemod.magic.spell.summon.monster_summon.MonsterSummonRules;
import com.example.examplemod.magic.spell.barrier.illusion_barrier.IllusionRules;
class SummonRulesModuleTest {
	@Test void paidLightCreditCoversFirstFourPulsesThenCharges(){int credit=4,total=0;for(int i=0;i<10;i++){var c=LamplightUpkeep.next(credit);if(i<4)assertEquals(0,c.fee());total+=c.fee();credit=c.credit();}assertEquals(2,total);assertEquals(4,credit);}
	@Test void unloadedNecromancySlotsAreNotReplaced(){var s=new java.util.HashMap<String,String>();assertEquals("necromancy_0",NecromancyRules.slot(s));s.put("necromancy_0","unloaded-uuid");assertEquals("necromancy_1",NecromancyRules.slot(s));s.put("necromancy_1","another");assertNull(NecromancyRules.slot(s));}
	@Test void otherSummonsKeepOneSlot(){assertNull(MonsterSummonRules.slot(java.util.Map.of("monster_summon","unloaded"),"monster_summon"));assertEquals("spirit_summon",MonsterSummonRules.slot(java.util.Map.of(),"spirit_summon"));}
	@Test void corpseExpiryRetainsMalformedFallback(){assertEquals(0,NecromancyRules.corpseExpiry("bad"));assertEquals(0,NecromancyRules.corpseExpiry("1;2;3;bad;uuid"));assertEquals(1200,NecromancyRules.corpseExpiry("1;2;3;1200;uuid"));}
	@Test void sharedNecromancyFeeBoundaryIsStrict(){assertEquals(0,NecromancyRules.initialFee(101,100));assertEquals(2,NecromancyRules.initialFee(100,100));}
	@Test void spiritAttackAndProtectionRemainBounded(){assertTrue(SpiritSummonRules.attacksThisPulse(19));assertFalse(SpiritSummonRules.attacksThisPulse(20));assertTrue(SpiritSummonRules.attacksThisPulse(40));assertEquals(0,SpiritSummonRules.protectedDamage(3));assertEquals(6,SpiritSummonRules.protectedDamage(10));}
	@Test void illusionRetainsOpenHorizontalClosedVerticalBoundary(){assertFalse(IllusionRules.contains(12,0,0,0));assertTrue(IllusionRules.contains(0,0,12,0));assertFalse(IllusionRules.contains(0,0,12.01,0));}
}
