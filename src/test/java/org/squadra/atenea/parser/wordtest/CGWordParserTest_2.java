package org.squadra.atenea.parser.wordtest;

import static org.junit.Assert.*;

import org.junit.Test;
import org.squadra.atenea.parser.CG3WordParser;

public class CGWordParserTest_2 {

	@Test
	public void punctuation() {
		System.out.println("============= PREPOSICIONES ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("contra	[contra] <'against'> PRP "));
		System.out.println(new CG3WordParser()
				.parseWord("so	[so] <'under'> PRP "));
		System.out.println(new CG3WordParser()
				.parseWord("a=pesar=de	[a=pesar=de] <'in=spite=of'> PRP "));
		System.out.println(new CG3WordParser()
				.parseWord("Desde	[desde] <*> PRP "));
		assertTrue(true);
	}
	
	@Test
	public void numericsWord() {
		System.out.println("============= NUMEROS EN PALABRAS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("veinte	[veinte] <card> <'twenty'> NUM MF P "));
		System.out.println(new CG3WordParser()
				.parseWord("veintiuna	[veintiuna] <card> NUM F P "));
		System.out.println(new CG3WordParser()
				.parseWord("cien	[cien] <card> <'hundred'> NUM MF P "));
		System.out.println(new CG3WordParser()
				.parseWord("cuatrocientos	[cuatrocientos] <card> <'four=hundred'> NUM M P "));
		assertTrue(true);
	}
	
	@Test
	public void numericsNumbers() {
		System.out.println("============= NUMEROS ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("2	[2] <card> NUM MF P "));
		System.out.println(new CG3WordParser()
				.parseWord("678	[678] <card> NUM MF P "));
		System.out.println(new CG3WordParser()
				.parseWord("1000 "));
		System.out.println(new CG3WordParser()
				.parseWord("1000,8	[1000,8] <card> NUM MF P "));
		System.out.println(new CG3WordParser()
				.parseWord("9874389,091908312	[9874389,091908312] <card> NUM MF P "));
		assertTrue(true);
	}
	
	@Test
	public void interjection() {
		System.out.println("============= INTERJECCION ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("eh	[eh] IN "));
		System.out.println(new CG3WordParser()
				.parseWord("hola	[hola] <'hi'> IN "));
		assertTrue(true);
	}
	
	@Test
	public void conjunction() {
		System.out.println("============= CONJUNCIONES ==============");
		
		System.out.println(new CG3WordParser()
				.parseWord("y	[y] <adv> KC @CO "));
		System.out.println(new CG3WordParser()
				.parseWord("mientras=que	[mientras=que] <clb> KS "));
		System.out.println(new CG3WordParser()
				.parseWord("pero	[pero] <'but'> <adv> KC "));
		System.out.println(new CG3WordParser()
				.parseWord("ya=que	[ya=que] <'since'> KS <'as'> KS "));
		System.out.println(new CG3WordParser()
				.parseWord("de=modo=que	[de=modo=que] <cjt> <'such=that'> KS "));
		assertTrue(true);
	}
}
