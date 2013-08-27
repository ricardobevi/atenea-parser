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
		//String sentenceToParse = "Yo le entregué un libro y le saqué un premio.";
		//String sentenceToParse = "El hombre que estaba corriendo se murió.";
		//String sentenceToParse = "Cómo se llamaba la hija de San Martín.";
		//String sentenceToParse = "Me quiero ir.";
		//String sentenceToParse = "Yo había estado jugando a la playstation.";
		//String sentenceToParse = "Abrir la cartera y cerrandola.";
		String sentenceToParse = "Yo no jugué a eso.";
		
		Sentence sentence = new Parser().parse(sentenceToParse);
		
		log.debug("GRAFO: \n" + sentence.getParseTree() );
		
		log.debug("ORACION: " + sentence );
		
		log.debug("TIPO DE ORACION: " + sentence.getType() );
		
		log.debug("SUB GRAFO OBJETO DIRECTO: \n" + sentence.getDirectObject().getParseTree() );
		log.debug("SUB GRAFO SUJETO: \n" + sentence.getSubject().getParseTree() );
		log.debug("SUB GRAFO VERBOS: \n" + sentence.getVerb().getParseTree() );
		
		assertTrue(true);
	}
	

}
