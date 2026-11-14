package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.SolidContacts;
class SolidContactsModuleTest {
 @Test void mergesClosestShapePerBlockAndKeepsFirstEqualWall(){var s=new SolidContacts<String,String>();s.accept("a",2,3,()->"first");s.accept("b",2,3,()->{fail();return null;});s.accept("a",1,3,()->"near");assertEquals(1,s.contacts().get("a"));assertEquals(2,s.contacts().get("b"));assertEquals("a",s.block());assertEquals("near",s.impact());}
 @Test void beyondStepMaySelectWallWithoutEnteringTerrainContacts(){var s=new SolidContacts<String,String>();s.accept("far",4,3,()->"point");assertTrue(s.contacts().isEmpty());assertEquals(4,s.wall());assertEquals("far",s.block());}
 @Test void infinityAndNaNDoNotSelectWall(){var s=new SolidContacts<String,String>();s.accept("nan",Double.NaN,3,()->{fail();return null;});s.accept("inf",Double.POSITIVE_INFINITY,3,()->{fail();return null;});assertTrue(s.contacts().isEmpty());assertNull(s.block());assertEquals(Double.POSITIVE_INFINITY,s.wall());}
 @Test void exactStepContactIsIncludedAndFartherShapesDoNotComputeImpact(){var s=new SolidContacts<String,String>();s.accept("a",3,3,()->"edge");s.accept("a",4,3,()->{fail();return null;});assertEquals(3,s.contacts().get("a"));assertEquals("edge",s.impact());}
 @Test void impactExceptionRetainsEarlierAssignments(){var s=new SolidContacts<String,String>();s.accept("old",2,3,()->"oldPoint");assertThrows(IllegalStateException.class,()->s.accept("new",1,3,()->{throw new IllegalStateException();}));assertEquals(1,s.wall());assertEquals("new",s.block());assertEquals("oldPoint",s.impact());assertEquals(1,s.contacts().get("new"));}
 @Test void frozenMergeAndWallSelectionMatchRandomContacts(){var r=new java.util.Random(1113);for(int n=0;n<1000;n++){var s=new SolidContacts<Integer,Integer>();var expected=new java.util.HashMap<Integer,Double>();double wall=Double.POSITIVE_INFINITY;Integer block=null;for(int i=0;i<20;i++){int b=r.nextInt(5);double c=r.nextDouble()*8;final int point=i;if(c<=3)expected.merge(b,c,Math::min);if(c<wall){wall=c;block=b;}s.accept(b,c,3,()->point);}assertEquals(expected,s.contacts());assertEquals(wall,s.wall());assertEquals(block,s.block());}}
}
