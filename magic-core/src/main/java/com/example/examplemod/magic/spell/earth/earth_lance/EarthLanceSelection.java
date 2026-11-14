package com.example.examplemod.magic.spell.earth.earth_lance;
import java.util.Optional;
import java.util.function.Supplier;
/** Lazy priority selection; the target-only compatibility entry never queries self support. */
public final class EarthLanceSelection {
 public static <T> Optional<T> select(boolean targetOnly,Supplier<Optional<T>> self,Supplier<Optional<T>> target) {
  if(!targetOnly){var support=self.get();if(support.isPresent())return support;}
  return target.get();
 }
 private EarthLanceSelection() { }
}
