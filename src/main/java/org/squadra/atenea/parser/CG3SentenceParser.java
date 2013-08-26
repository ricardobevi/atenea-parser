package org.squadra.atenea.parser;

import org.squadra.atenea.base.word.Word;
import org.squadra.atenea.parser.model.Sentence;
import org.squadra.atenea.parser.model.SyntacticNode;

/**
 * Esta clase se encarga de parsear la salida completa del analizador CG3.
 * Crea un objeto Sentence que contiene un grafo de objetos Words.
 * @author Ricardo Bevilacqua
 * @author Leandro Morrone
 *
 */
public class CG3SentenceParser {

	static public Sentence ParseSentence(String rawPreParsedSentence){		
				
		Sentence parsedSentence = CG3SentenceParser.getGraph( rawPreParsedSentence );
		
		return parsedSentence;
	}
	
	
	static private Sentence getGraph( String rawPreParsedSentence ){
		
		Sentence parsedSentence = new Sentence();
		
		String[] rawWords = rawPreParsedSentence.split("\n");
		
		for ( String rawWord : rawWords ) {
			
			if( !rawWord.equals("") ) { 
				
				Integer rawTypeIndex = rawWord.lastIndexOf("@");
				Integer rawRelationIndex = rawWord.lastIndexOf("#");
				String rawWordTags = new String();
				
				// Obtengo la parte necesaria para cargar el objeto Word
				if (rawTypeIndex != -1) {
					rawWordTags = rawWord.substring(0, rawTypeIndex);
				}
				else if (rawRelationIndex != -1) {
					rawWordTags = rawWord.substring(0, rawRelationIndex);
				}
				else {
					rawWordTags = rawWord;
				}
				
				// Parseo los tags y obtengo el objeto Word
				Word word = new CG3WordParser().parseWord(rawWordTags);
				
				
				// Obtengo el tipo de relacion
				String type = new String();
				
				if (rawTypeIndex != -1) {
					type = rawWord.substring(rawTypeIndex + 1, rawRelationIndex - 1);
				}
				
				// Obtengo la relacion
				String rawRelationString = 
						rawWord.substring(rawRelationIndex + 1, rawWord.length());
				
				String[] nodeAndRelation = rawRelationString.split("->");	
				
				Integer node1Index = Integer.parseInt( nodeAndRelation[0] );
				Integer node2Index = Integer.parseInt( nodeAndRelation[1] );
				
				// Creo el nuevo nodo y lo relaciono en el grafo
				SyntacticNode node = new SyntacticNode(word, type);
				
				parsedSentence.relateSyntacticNodes(node, node1Index, node2Index);				

			}
			
		}
		
		return parsedSentence;
	}
	
	
}
