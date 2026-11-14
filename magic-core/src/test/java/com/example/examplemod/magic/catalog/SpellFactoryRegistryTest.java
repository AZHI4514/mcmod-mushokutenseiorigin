package com.example.examplemod.magic.catalog;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.domain.model.*;
import com.example.examplemod.magic.mechanic.projectile.*;

class SpellFactoryRegistryTest {
	@Test void explicitBindingsRejectDuplicateAndMissingProducts() {
		var id=new SpellId("water_ball");
		var spec=new ProjectileSpec(id,1,0,1.5F,1);
		var binding=new SpellFactoryRegistry.Binding(spec,new ProjectileSpellFactory());
		assertThrows(IllegalArgumentException.class,()->new SpellFactoryRegistry(List.of(binding,binding)));
		var registry=new SpellFactoryRegistry(List.of(binding));
		assertThrows(IllegalArgumentException.class,()->registry.create(new SpellId("not_registered")));
		assertEquals(id,registry.create(id).prepare(Vec3d.ZERO,0,0).spec().id());
	}
}
