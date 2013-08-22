package org.squadra.atenea.parser.test;

import static org.junit.Assert.*;
import lombok.extern.log4j.Log4j;

import org.squadra.atenea.parser.Parser;
import org.squadra.atenea.parser.model.Sentence;

@Log4j
public class Test {

	@org.junit.Test
	public void test() {

		Parser parser = new Parser();

		//String sentenceToParse = "Él le pega con un palo.";

		String sentenceToParse = "Él sabía que le pegaba con un palo.";
		
		Sentence sentence = parser.parse(sentenceToParse);
		
		//log.debug("Parsed sentence: \n" + sentence);
		
		log.debug("Generated Graph: \n" + sentence.getParseTree() );
		
		log.debug("VERB: " + sentence.getVerbs() );


		assertTrue(true);
	}
	

}
