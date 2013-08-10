package org.squadra.atenea.parser.wordtest;

import static org.junit.Assert.*;

import org.junit.Test;
import org.squadra.atenea.parser.CG3WordParser;

public class CGWordParserTest_1 {

	@Test
	public void punctuation() {
		System.out.println("============= PUNTUACION ==============");
		
		System.out.println(new CG3WordParser().parseWord("$."));
		assertTrue(true);
	}
	
	@Test
	public void nouns() {
		System.out.println("============= SUSTANTIVOS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("casa	[casa] <build> <'house'> N F S "));
		System.out.println(new CG3WordParser()
				.parseWord("agua	[agua] <cm-liq> <'water'> N F S "));
		System.out.println(new CG3WordParser()
				.parseWord("avertruz	[avertruz] <heur> N M S "));
		System.out.println(new CG3WordParser()
				.parseWord("autom�viles	[autom�vil] <Vground> <'car'> N M P "));
		System.out.println(new CG3WordParser()
				.parseWord("casa	[casa] <build> <'house'> N F S "));
		assertTrue(true);
	}
	
	@Test
	public void adjectives() {
		System.out.println("============= ADJETIVOS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("fea	[feo] <'ugly'> ADJ F S "));
		System.out.println(new CG3WordParser()
				.parseWord("hermosos	[hermoso] <'handsome'> ADJ M P "));
		System.out.println(new CG3WordParser()
				.parseWord("media	[medio] <'half'> <'mid'> <'average'> ADJ F S "));
		System.out.println(new CG3WordParser()
				.parseWord("soper�tano	[soper�tano] <heur> ADJ M S "));
		assertTrue(true);
	}
	
	@Test
	public void verbsIndicatives() {
		System.out.println("============= VERBOS INDICATIVOS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("salto	[saltar] <move> <'jump'> <mv> V PR 1S IND VFIN "));
		System.out.println(new CG3WordParser()
				.parseWord("saltabas	[saltar] <move> <'jump'> <mv> V IMPF 2S IND VFIN "));
		System.out.println(new CG3WordParser()
				.parseWord("salt�	[saltar] <move> <'jump'> <mv> V PS 3S IND VFIN"));
		System.out.println(new CG3WordParser()
				.parseWord("jugar�an	[jugar] <'play'> <mv> V COND VFIN 3P "));
		System.out.println(new CG3WordParser()
				.parseWord("hidrolavarar�n	[hidrolavarar] <heur> <mv> V FUT 3P IND VFIN "));
		assertTrue(true);
	}
	
	@Test
	public void verbsSubjunctives() {
		System.out.println("============= VERBOS SUBJUNTIVOS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("saltaran	[saltar] <move> <'jump'> <mv> V IMPF 3P SUBJ VFIN "));
		System.out.println(new CG3WordParser()
				.parseWord("puteasen	[putear] <mv> V IMPF 3P SUBJ VFIN"));
		System.out.println(new CG3WordParser()
				.parseWord("cagare	[cagar] <'DA:skide'> <'defecate'> <'shit'> <mv> V FUT 1/3S SUBJ VFIN "));
		System.out.println(new CG3WordParser()
				.parseWord("tire	[tirar] <'throw'> <mv> V PR 1/3S SUBJ VFIN "));
	}
	
	@Test
	public void verbsInfinitives() {
		System.out.println("============= VERBOS INFINITIVOS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("partir	[partir] <ve> <'part'> <mv> V INF "));
		System.out.println(new CG3WordParser()
				.parseWord("permutar	[permutar] <sam1> <'permute'> <mv> V INF "));
		assertTrue(true);
	}
	
	@Test
	public void verbsGerunds() {
		System.out.println("============= VERBOS GERUNDIOS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("salteando	[saltear] <'rob'> <mv> V GER "));
		System.out.println(new CG3WordParser()
				.parseWord("yendo	[ir] <va+DIR> <'go'> <mv> V GER "));
		assertTrue(true);
	}
	
	@Test
	public void verbsParticiples() {
		System.out.println("============= VERBOS PARTICIPIOS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("comido	[comer] <'eat'> <mv> V PCP M S "));
		System.out.println(new CG3WordParser()
				.parseWord("saltadas	[saltar] <move> <'jump'> <adj> V PCP F P "));
		System.out.println(new CG3WordParser()
				.parseWord("loggeada	[loggear] <heur> <mv> V PCP F S "));
		assertTrue(true);
	}
	
	@Test
	public void verbsImperatives() {
		System.out.println("============= VERBOS IMPERATIVOS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("toma	[tomar] <sam1> <'take'> <mv> V IMP 2S VFIN "));
		System.out.println(new CG3WordParser()
				.parseWord("abre	[abrir] <sam1> <'open'> <mv> V IMP 2S VFIN "));
		assertTrue(true);
	}
	
	@Test
	public void properName() {
		System.out.println("============= NOMBRES PROPIOS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("Juan	[Juan] <*> <'Juan'> PROP M S "));
		System.out.println(new CG3WordParser()
				.parseWord("Estados=Unidos	[Estados=Unidos] <*> <Lcountry> <'United=States=of=America'> PROP M P "));
		System.out.println(new CG3WordParser()
				.parseWord("CIA	[CIA] <*> <heurtag> PROP MF SP "));
		System.out.println(new CG3WordParser()
				.parseWord("Leandro=Morrone	[Leandro=Morrone] <*> <heurtag> PROP MF SP "));
		System.out.println(new CG3WordParser()
				.parseWord("Argentina	[Argentina] <*> <'Argentina'> PROP F S "));
		System.out.println(new CG3WordParser()
				.parseWord("Brasil	[Brasil] <*> <Lcountry> <'Brazil'> PROP M S "));
		assertTrue(true);
	}


}
