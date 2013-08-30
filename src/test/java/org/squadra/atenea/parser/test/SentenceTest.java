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
		String sentenceToParse = "El niño malo no podría haber estado jugando a la play y a las cartas de Jhon.";
		//String sentenceToParse = "Abrir la cartera y cerrandola.";
		//String sentenceToParse = "Yo no jugué a eso.";
		
		Sentence sentence = new Parser().parse(sentenceToParse);
		
		log.debug("GRAFO: \n" + sentence.getParseTree() );
		
		log.debug("ORACION: " + sentence );
		
		log.debug("TIPO DE ORACION: " + sentence.getType() );
		
		log.debug("SUB GRAFO OBJETO DIRECTO: \n" + sentence.getDirectObject().get(0).getParseTree() );
		log.debug("SUB GRAFO SUJETO: \n" + sentence.getSubjects().get(0).getParseTree() );
		log.debug("SUB GRAFO VERBOS: \n" + sentence.getVerbs().get(0).getParseTree() );
		
		log.debug("SUSTANTIVOS: \n" + sentence.getNouns() );
		log.debug("VERBOS PRINCIPALES: \n" + sentence.getVerbs().get(0).getMainVerbs() );
		log.debug("ADJETIVOS: \n" + sentence.getSubjects().get(0).getAdjectives() );
		
		log.debug("NEGACION: " + sentence.getVerbs().get(0).isNegation() );
		
		assertTrue(true);
	}
	

}
