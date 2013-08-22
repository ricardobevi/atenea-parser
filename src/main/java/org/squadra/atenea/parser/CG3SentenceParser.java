package org.squadra.atenea.parser;

import org.squadra.atenea.base.graph.Graph;
import org.squadra.atenea.base.graph.Node;
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
						rawWord.substring(rawRelationIndex + 1, rawRelationIndex + 5);
				
				String[] nodeAndRelation = rawRelationString.split("->");	
				
				Integer node1Index = Integer.parseInt( nodeAndRelation[0] );
				Integer node2Index = Integer.parseInt( nodeAndRelation[1] );
				
				// Creo el nuevo nodo y lo relaciono en el grafo
				SyntacticNode node = new SyntacticNode(word, type);
				
				parsedSentence.relateNodes(node, node1Index, node2Index);				

			}
			
		}
		
		return parsedSentence;
	}
	
	
	
	
	@Deprecated
	static private Graph<SyntacticNode> getGraph2( String rawPreParsedSentence ){
		Graph<SyntacticNode> parsingGraph = new Graph<SyntacticNode>();
		
		Integer sLen = rawPreParsedSentence.length();
		
		Integer i = 0;
		
		String word = new String();
		String type = new String();
		String rawNodeId = new String();
		
		while ( i < sLen ){
			
			//Avanzo hasta encontrar un salto de linea
			while ( i < sLen && 
					rawPreParsedSentence.charAt(i) != '\n' ){
				i++;
			}
			
			i++;
			
			//Guardo la palabra.
			while( i < sLen && 
				   Character.isLetter( rawPreParsedSentence.charAt(i) )  ){
				word += rawPreParsedSentence.charAt(i);
				i++;
			}
			
			i++;
			
			//Loop hasta llegar al proximo \n
			while( i < sLen && (
					rawPreParsedSentence.charAt(i) != '\n' ||
				   rawNodeId == "") ){
				
				//Si encuentro un @ tengo el tipo de la palabra
				if( i < sLen && rawPreParsedSentence.charAt(i) == '@' ) {
					
					i++;
					while( i < sLen && 
						   Character.isLetter( rawPreParsedSentence.charAt(i) ) &&
						   rawPreParsedSentence.charAt(i) != '\n'){
						type += rawPreParsedSentence.charAt(i);
						i++;
					}
					
				//Si encuentro un # tengo la union de los nodos
				} else if(  i < sLen && rawPreParsedSentence.charAt(i) == '#'  ) {
					
					i++;
					while( i < sLen && 
						   rawPreParsedSentence.charAt(i) != ' ' &&
						   rawPreParsedSentence.charAt(i) != '\n'){
						rawNodeId += rawPreParsedSentence.charAt(i);
						i++;
					}
				}
				i++;
			}
			
			
			//Aca ya tengo la palabra y su clasificacion raw
			
			String[] nodeAndRelation = rawNodeId.split("->");
			
			Integer node1Index = Integer.parseInt( nodeAndRelation[0] );
			Integer node2Index = Integer.parseInt( nodeAndRelation[1] );

			Node<SyntacticNode> node1 = new Node<SyntacticNode>( new SyntacticNode(new Word(), type) );

			Node<SyntacticNode> node2 = parsingGraph.getNode(node2Index);
			
			if( node2 == null ){
				parsingGraph.addNode( new Node<SyntacticNode>( new SyntacticNode() ), node2Index );
			}

			parsingGraph.addNode( node1, node1Index );
			
			parsingGraph.relate(node1Index, node2Index);
			parsingGraph.relate(node2Index, node1Index);
						
			//Limpio las variables
			word = new String();
			type = new String();
			rawNodeId = new String();
			
		}
		
		return parsingGraph;
	}
	
	
	
	
}
