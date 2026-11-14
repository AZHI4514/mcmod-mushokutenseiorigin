package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.mechanic.projectile.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class EffectShapeModuleTest {
	@Test void liveShapeCatalogRetainsCarrierCategoriesAndDefaults() {
		assertEquals(18,EffectShapeCatalog.ids().size());
		for(var id:EffectShapeCatalog.ids()){
			var shape=EffectShapeCatalog.of(id);
			boolean stationary=java.util.Set.of("ice_lance","flame_pillar","waterfall","physical_shield").contains(id);
			assertEquals(stationary,shape.stationary());assertTrue(shape.width()>0&&shape.height()>0&&shape.depth()>0);
		}
		assertEquals(new EffectShape(false,.25,.25,.5),EffectShapeCatalog.of("unknown"));
	}
	@Test void thinWallAndTallColumnsCannotCollapseIntoGenericProjectileBounds() {
		assertEquals(new EffectShape(true,3,3,.2),EffectShapeCatalog.of("physical_shield"));
		assertEquals(new EffectShape(true,3,4,.2),EffectShapeCatalog.of("waterfall"));
		assertEquals(new EffectShape(true,2,4,2),EffectShapeCatalog.of("flame_pillar"));
		assertEquals(new EffectShape(false,3,3,3),EffectShapeCatalog.of("earth_pillar"));
	}
}
