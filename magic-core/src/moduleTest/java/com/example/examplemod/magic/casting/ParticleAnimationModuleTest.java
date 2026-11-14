package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.domain.visual.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ParticleAnimationModuleTest {
	@Test void glyphStreamKeepsFourTickCadenceIncludingWrap() {
		for(int tick=-200;tick<=200;tick++)assertEquals(Math.floorMod(tick,4)==0,ParticleAnimation.emitGlyph(tick));
		assertTrue(ParticleAnimation.emitGlyph(Integer.MIN_VALUE));assertFalse(ParticleAnimation.emitGlyph(Integer.MAX_VALUE));
	}
	@Test void allCatalogProfilesRemainBoundedAndUnknownIdsFail() {
		assertEquals(70,SpellAtmosphereProfile.ids().size());
		for(var id:SpellAtmosphereProfile.ids()){var p=SpellAtmosphereProfile.of(id);assertTrue(p.size()>0);assertTrue(p.duration()>0);assertTrue(p.accents()>0);}
		assertThrows(IllegalArgumentException.class,()->SpellAtmosphereProfile.of("missing"));
	}
	@Test void samplesMatchFrozenParticleFormulaForEveryRole() {
		for(int i=0;i<=10000;i++){
			float t=i/10000F;
			for(int role=0;role<4;role++){
				boolean haze=role==0,accent=role==1,glyph=role==2;
				float attack=Math.min(1,t*8);float envelope=attack*attack*(3-2*attack)*(1-t)*(1-t);
				assertEquals(haze||accent?envelope*(haze?.7F:1.7F):(float)Math.sin(t*Math.PI)*(glyph?.85F:.8F),ParticleAnimation.alpha(t,haze,accent,glyph));
				float smooth=t*t*(3-2*t);
				assertEquals(haze?.55F+1.35F*smooth:accent?.65F+.65F*smooth:glyph?.65F+.35F*Math.min(1,t*5):.6F+t*.6F,ParticleAnimation.size(t,haze,accent,glyph));
			}
		}
	}
	@Test void fadeFramesAndProgressRetainEndpoints() {
		assertEquals(0F,ParticleAnimation.progress(-1,0,24));assertEquals(1F,ParticleAnimation.progress(25,0,24));
		assertEquals(0,ParticleAnimation.frame(0));assertEquals(23,ParticleAnimation.frame(1));assertEquals(0F,ParticleAnimation.alpha(1,true,false,false));
	}
}
