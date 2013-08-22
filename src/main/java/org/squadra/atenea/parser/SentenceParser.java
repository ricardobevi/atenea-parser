package org.squadra.atenea.parser;

import lombok.extern.log4j.Log4j;

import org.squadra.atenea.base.graph.Graph;
import org.squadra.atenea.base.graph.Node;
import org.squadra.atenea.parser.model.Sentence;
import org.squadra.atenea.parser.model.SyntacticNode;

@Log4j
public class SentenceParser {

	static public Sentence ParseSentence(String rawPreParsedSentence){		
				
		Sentence parsedSentence = SentenceParser.getGraph( rawPreParsedSentence );
				
		return parsedSentence;
	}
	
	
	static private Sentence getGraph( String rawPreParsedSentence ){
		
		Sentence parsedSentence = new Sentence();
		
		String[] rawWords = rawPreParsedSentence.split("\n");
		
		for ( Integer i = 0 ; i < rawWords.length ; i++ ) {
			
			if( !rawWords[i].equals("") ) { 
				String word = new String();
				
				Integer j = 0;
				while( j < rawWords[i].length() && rawWords[i].charAt(j) != ' ' &&  rawWords[i].charAt(j) != '\t' ){
					word = word + rawWords[i].charAt(j);
					j++;
				}
		
				Integer rawRelationIndex = rawWords[i].indexOf("#");
				Integer rawTypeIndex = rawWords[i].indexOf("@");

				String type = new String();
				
				if ( rawTypeIndex != -1 ){
					j = rawTypeIndex + 1;
					while( j < rawWords[i].length() && 
						   rawWords[i].charAt(j) != ' ' &&  
						   rawWords[i].charAt(j) != '\t' ){
						
						type = type + rawWords[i].charAt(j);
						j++;
						
					}
				}
				
				type = type.replace("<", "");
				type = type.replace(">", "");
				
				String rawRelationString = 
						rawWords[i].substring(rawRelationIndex + 1, 
								              rawRelationIndex + 5);
				
				String[] nodeAndRelation = rawRelationString.split("->");	
				
				
				Integer node1Index = Integer.parseInt( nodeAndRelation[0] );
				Integer node2Index = Integer.parseInt( nodeAndRelation[1] );
				
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
			
			

			Node<SyntacticNode> node1 = new Node<SyntacticNode>( new SyntacticNode(word, type) );

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
