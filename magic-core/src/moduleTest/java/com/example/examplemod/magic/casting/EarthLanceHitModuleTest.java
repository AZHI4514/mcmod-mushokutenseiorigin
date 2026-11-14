package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.earth.earth_lance.EarthLanceHit;
class EarthLanceHitModuleTest {
 static class Target implements EarthLanceHit.Target {
  final List<String> events=new ArrayList<>(); boolean allowed=true,throwsDamage;
  public boolean raise(){events.add("raise");return allowed;}
  public void damage(){events.add("damage");if(throwsDamage)throw new IllegalStateException();}
  public void show(){events.add("show");}
 }
 @Test void placementThenDamageThenVisual(){var t=new Target();assertTrue(EarthLanceHit.apply(t));assertEquals(List.of("raise","damage","show"),t.events);}
 @Test void rejectedPlacementNeverHitsOrShows(){var t=new Target();t.allowed=false;assertFalse(EarthLanceHit.apply(t));assertEquals(List.of("raise"),t.events);}
 @Test void damageExceptionCannotUndoPlacedSpikes(){var t=new Target();t.throwsDamage=true;assertThrows(IllegalStateException.class,()->EarthLanceHit.apply(t));assertEquals(List.of("raise","damage"),t.events);}
}
