package org.squadra.atenea.parser;

import org.squadra.atenea.parser.connection.CG3Connection;
import org.squadra.atenea.parser.connection.HttpCG3Connection;
import org.squadra.atenea.parser.model.Sentence;

/**
 * Esta clase se encarga de ejecutar el programa Constraint Grammar 3, obtener
 * su salida y parsearla para generar un objeto Sentence que sera utilizado en
 * nuestro analizador sintactico.
 * @author Ricardo Bevilacqua
 *
 */
public class Parser {

	public Parser() {}
	
	public Sentence parse(String input) {
		String rawPreParsedSentenceString = "";
		
		// Conecto con el programa de la gramatica y obtengo la salida para el parsing
		CG3Connection conn = new HttpCG3Connection();
		rawPreParsedSentenceString = conn.getPreParsedSentence(" " + input);
		
		// Parseo la salida del programa y obtengo un objeto Sentence
		Sentence sentence = CG3SentenceParser.ParseSentence(rawPreParsedSentenceString);
		
		return sentence;
	}
	
}
