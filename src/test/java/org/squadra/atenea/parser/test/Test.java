package org.squadra.atenea.parser.test;

import static org.junit.Assert.*;

import org.squadra.atenea.parser.Parser;

public class Test {

	@org.junit.Test
	public void test() {

		Parser parser = new Parser();

		String sentenceToParse = "Él le pega con un palo.";

		System.out.println(parser.parse(sentenceToParse));


		assertTrue(true);
	}
	
	@org.junit.Test
	public void test2() {

		Parser parser = new Parser();


		String sentenceToParse = "ella le pega con un palo.";

		System.out.println(parser.parse(sentenceToParse));


		assertTrue(true);
	}
	
	@org.junit.Test
	public void test3() {

		Parser parser = new Parser();


		String sentenceToParse = "todos se pegan con un palo.";

		System.out.println(parser.parse(sentenceToParse));


		assertTrue(true);
	}
	
	

}
