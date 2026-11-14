package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.domain.visual.*;
import com.example.examplemod.magic.mechanic.field.ModeSelection;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class AreaVisualContractsModuleTest {
	@Test void visualThresholdsAndBodyOffsetsRetainAuthoredValues(){
		assertEquals(.12F,AreaMeshLayout.at("apocalypse_flame",59.99F).opacity());assertEquals(1,AreaMeshLayout.at("apocalypse_flame",60).opacity());
		assertEquals(2,AreaMeshLayout.at("nuclear_explosion",99.99F).width());assertEquals(0,AreaMeshLayout.at("nuclear_explosion",100).width());assertEquals(64,AreaMeshLayout.at("nuclear_explosion",140).width());
		assertEquals(new AreaMeshLayout(.9,1,1.9F,1,1),AreaMeshLayout.at("burning_place",0));assertEquals(new AreaMeshLayout(1,1.8F,2.1F,1.8F,1),AreaMeshLayout.at("magic_shield",0));assertNull(AreaMeshLayout.at("unknown",0));
	}
	@Test void allSelectableModesRemainExactlyTheFourExistingSpells(){
		assertEquals(java.util.Set.of("dual_shield","atmosphere_dominion","gravity_magic","nuclear_explosion"),SpellDefinition.ALL.stream().filter(ModeSelection::selectable).map(SpellDefinition::id).collect(java.util.stream.Collectors.toSet()));assertFalse(ModeSelection.selectable(null));
	}
	@Test void worldClockSelectionKeepsAllSharedFieldExecutionsAndExplicitSpecials(){
		for(var spell:SpellDefinition.ALL){boolean expected=java.util.Set.of("earth_hedgehog","magma_gush","absolute_zero","inferno","flash_over","sacred_nova","god_purge").contains(spell.id())||java.util.Set.of(SpellDefinition.Execution.WIND_FIELD,SpellDefinition.Execution.EXPANSION_FIELD,SpellDefinition.Execution.MODE_FIELD,SpellDefinition.Execution.SMOKE_FIELD).contains(spell.execution());assertEquals(expected,AreaAnimationClock.usesWorldStart(spell));}
	}
}
