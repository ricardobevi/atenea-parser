package org.squadra.atenea.parser.wordtest;

import static org.junit.Assert.*;

import org.junit.Test;
import org.squadra.atenea.parser.CG3WordParser;

public class CGWordParserTest_3 {

	@Test
	public void personalPronouns() {
		System.out.println("============= PRONOMBRES PERSONALES ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("yo	[yo] PERS MF 1S NOM "));
		System.out.println(new CG3WordParser()
				.parseWord("ella	[él] PERS F 3S NOM/PIV "));
		System.out.println(new CG3WordParser()
				.parseWord("vosotros	[vosotros] PERS M 2P NOM/PIV "));
		System.out.println(new CG3WordParser()
				.parseWord("mí	[yo] PERS MF 1S PIV "));
		System.out.println(new CG3WordParser()
				.parseWord("sí	[sí] <refl> PERS MF SP PIV "));
		System.out.println(new CG3WordParser()
				.parseWord("los	[lo] <sam3> PERS M 3P ACC "));
		System.out.println(new CG3WordParser()
				.parseWord("nos	[nosotros] PERS MF 1P ACC/DAT "));
		System.out.println(new CG3WordParser()
				.parseWord("le	[le] <sam2> PERS MF 3S DAT "));
		assertTrue(true);
	}

	@Test
	public void articles() {
		System.out.println("============= ARTICULOS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("el	[el] <artd> DET M S "));
		System.out.println(new CG3WordParser()
				.parseWord("Los	[el] <*> <artd> DET M P "));
		System.out.println(new CG3WordParser()
				.parseWord("unas	[un] <quant2> <arti> DET F P "));
		assertTrue(true);
	}
	
	@Test
	public void demostratives() {
		System.out.println("============= PRONOMBRE DEMOSTRATIVOS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("aquellos	[aquel] <artd> DET M P "));
		System.out.println(new CG3WordParser()
				.parseWord("esa	[ese] <artd> DET F S "));
		System.out.println(new CG3WordParser()
				.parseWord("eso	[ese] <dem> INDP M S "));
		assertTrue(true);
	}
	
	@Test
	public void possesives() {
		System.out.println("============= PRONOMBRE POSESIVO ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("mío	[mío] <poss 1S> DET M S "));
		System.out.println(new CG3WordParser()
				.parseWord("tu	[tu] <cjt> <poss 2S> DET MF S "));
		System.out.println(new CG3WordParser()
				.parseWord("suyos	[tuyo] <cjt> <poss 3S/P> DET M P "));
		assertTrue(true);
	}
	
	@Test
	public void relatives() {
		System.out.println("============= RELATIVO ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("cuyo	[cuyo] <clb> <rel> <poss> DET M S "));
		System.out.println(new CG3WordParser()
				.parseWord("el=cual	[el=cual] <rel> INDP M S "));
		System.out.println(new CG3WordParser()
				.parseWord("que	[que] <rel> INDP MF SP "));
		assertTrue(true);
	}
	
	@Test
	public void interrogatives() {
		System.out.println("============= INTERROGATIVO ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("quién	[quién] <interr> INDP MF S "));
		System.out.println(new CG3WordParser()
				.parseWord("cuántos	[cuánto] <interr> <quant2> DET M P "));
		assertTrue(true);
	}
	
	@Test
	public void indefinites() {
		System.out.println("============= INDEFINIDO ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("pocas	[poco] <cjt> <quant2> <quant3> DET F P "));
		System.out.println(new CG3WordParser()
				.parseWord("nada	[nada] <cjt> <quant0> INDP M S "));
		System.out.println(new CG3WordParser()
				.parseWord("muchos	[mucho] <cjt> <quant2> <quant3> DET M P "));
		System.out.println(new CG3WordParser()
				.parseWord("cada=uno	[cada=uno] <quant2> DET M S "));
		assertTrue(true);
	}
	
	@Test
	public void unknown() {
		System.out.println("============= PRONOMBRES DESCONOCIDOS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("lo=mismo	[lo=mismo] INDP M S "));
		System.out.println(new CG3WordParser()
				.parseWord("nadie	[nadie] INDP M S "));
		System.out.println(new CG3WordParser()
				.parseWord("alguien	[alguien] INDP M S "));
		System.out.println(new CG3WordParser()
				.parseWord("misma	[mismo] <KOMP> DET F S "));
		assertTrue(true);
	}
	
	@Test
	public void adverbs() {
		System.out.println("============= ADVERBIOS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("allá	[allá] <cjt> <aloc> <atemp> <'there'> ADV "));
		System.out.println(new CG3WordParser()
				.parseWord("adelante	[adelante] <cjt> <adir> <'forward'> <'ahead'> ADV "));
		System.out.println(new CG3WordParser()
				.parseWord("siempre	[siempre] <cjt> <atemp> <'always'> ADV "));
		System.out.println(new CG3WordParser()
				.parseWord("hoy	[hoy] <cjt> <atemp> <'today'> ADV "));
		System.out.println(new CG3WordParser()
				.parseWord("así	[así] <cjt-head> <amanner> <'so'> <'thus'> <'this=way'> ADV "));
		System.out.println(new CG3WordParser()
				.parseWord("seguramente	[seguramente] <cjt> <amod> <'sure'> ADV "));
		System.out.println(new CG3WordParser()
				.parseWord("fuertemente	[fuertemente] <cjt> <'strongly'> ADV "));
		System.out.println(new CG3WordParser()
				.parseWord("casi	[casi] <cjt> <aquant> <'quasi'> ADV "));
		System.out.println(new CG3WordParser()
				.parseWord("más	[más] <aquant> <KOMP> ADV "));
		System.out.println(new CG3WordParser()
				.parseWord("tan	[tan] <dem> <aquant> <KOMP> ADV"));
		assertTrue(true);
	}
	
	@Test
	public void adverbsRare() {
		System.out.println("============= ADVERBIOS RAROS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("despacio	[despacio] <cjt> <'slowly'> ADV "));
		System.out.println(new CG3WordParser()
				.parseWord("tampoco	[tampoco] <setop> <'either'> ADV "));
		System.out.println(new CG3WordParser()
				.parseWord("dónde	[dónde] <cjt-head> <interr> <aloc> <'where'> ADV "));
		System.out.println(new CG3WordParser()
				.parseWord("cuándo	[cuándo] <cjt> <interr> ADV "));
		System.out.println(new CG3WordParser()
				.parseWord("pues	[pues] <kc> <temp> <'then'> <'so'> ADV "));
		assertTrue(true);
	}
}
