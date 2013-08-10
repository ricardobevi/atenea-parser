package org.squadra.atenea.parser;

import org.squadra.atenea.base.graph.Graph;
import org.squadra.atenea.base.graph.Node;
import org.squadra.atenea.parser.model.Sentence;
import org.squadra.atenea.parser.model.SyntacticNode;

public class SentenceParser {

	static public Sentence ParseSentence(String rawPreParsedSentence){
		
		Sentence parsedSentence = new Sentence();
		
		Graph<SyntacticNode> parsingGraph = new Graph<SyntacticNode>();
		
		String[] rawWords = rawPreParsedSentence.split("\n");
		
		Node<SyntacticNode> node0 = new Node<SyntacticNode>(new SyntacticNode());
		parsingGraph.addNode( node0, 0 );
		
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
					j = rawTypeIndex;
					while( j < rawWords[i].length() && rawWords[i].charAt(j) != ' ' &&  rawWords[i].charAt(j) != '\t' ){
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
				
				Node<SyntacticNode> node1 = new Node<SyntacticNode>( new SyntacticNode(word, type) );

				Node<SyntacticNode> node2 = parsingGraph.getNode(node2Index);
				
				if( node2 == null ){
					parsingGraph.addNode( new Node<SyntacticNode>( new SyntacticNode() ), node2Index );
				}
	
				parsingGraph.addNode( node1, node1Index );
				
				parsingGraph.relate(node1Index, node2Index);
				
				
			
			}
			
		}



		
		System.out.println("##############GRAFO");
		System.out.println(parsingGraph);
		
		
		
		return parsedSentence;
	}
	
}