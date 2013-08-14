package org.squadra.atenea.parser.test;

import static org.junit.Assert.*;
import lombok.extern.log4j.Log4j;

import org.squadra.atenea.parser.Parser;

@Log4j
public class Test {

	@org.junit.Test
	public void test() {

		Parser parser = new Parser();

		String sentenceToParse = "Él le pega con un palo.";

		log.debug("Parsed sentence: \n" + parser.parse(sentenceToParse));



		assertTrue(true);
	}
	

}
