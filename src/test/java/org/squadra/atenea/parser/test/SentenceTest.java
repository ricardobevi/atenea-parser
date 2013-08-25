package org.squadra.atenea.parser.test;

import static org.junit.Assert.*;
import org.junit.Test;
import lombok.extern.log4j.Log4j;

import org.squadra.atenea.parser.Parser;
import org.squadra.atenea.parser.model.Sentence;

@Log4j
public class SentenceTest {

	@Test
	public void test() {
		
		//String sentenceToParse = "Él le pega con un palo.";
		//String sentenceToParse = "Él sabía que le pegaba con un palo.";
		//String sentenceToParse = "Quién era la esposa de San Martín.";
		//String sentenceToParse = "El hombre que estaba corriendo se murió.";
		String sentenceToParse = "Cómo se llamaba la hija de San Martín.";
		
		Sentence sentence = new Parser().parse(sentenceToParse);
		
		log.debug("GRAFO: \n" + sentence.getParseTree() );
		
		log.debug("ORACION: " + sentence );
		
		log.debug("TIPO DE ORACION: " + sentence.getType() );

		log.debug("SUJETO: " + sentence.getDirectObject() );
		
		log.debug("SUB GRAFO: " + sentence.getDirectObject().getParseTree() );
		
		assertTrue(true);
	}
	

}
