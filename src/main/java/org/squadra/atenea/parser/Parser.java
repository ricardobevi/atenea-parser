package org.squadra.atenea.parser;

import org.squadra.atenea.parser.connection.CG3Connection;
import org.squadra.atenea.parser.connection.HttpCG3Connection;

public class Parser {

	public String parse(String input) {
		String rawPreParsedSentenceString = "";
		
		//obtengo la salida del programa para el parsing
		CG3Connection conn = new HttpCG3Connection();
		rawPreParsedSentenceString = conn.getPreParsedSentence(input);

		SentenceParser.ParseSentence(rawPreParsedSentenceString);


		return rawPreParsedSentenceString;
	}
	



}
