package org.squadra.atenea.parser.model;

import java.util.HashMap;
import java.util.HashSet;

import lombok.Data;

import org.squadra.atenea.base.graph.Graph;
import org.squadra.atenea.base.graph.Node;

public @Data class Sentence {

	private Graph<SyntacticNode> parseTree;
	
	private HashMap<String, String> sentencePart;
	
	HashSet<Integer> verbs;
	
	public Sentence(){
		this.verbs = new HashSet<Integer>();
		this.parseTree = new Graph<SyntacticNode>();
		Node<SyntacticNode> node0 = new Node<SyntacticNode>(new SyntacticNode());
		this.parseTree.addNode( node0, 0 );
	}

	
	public void relateNodes(SyntacticNode node, Integer node1Index, Integer node2Index){
		
		Node<SyntacticNode> node1 = new Node<SyntacticNode>( node );
		Node<SyntacticNode> node2 = parseTree.getNode(node2Index);
		
		if( node2 == null ){
			parseTree.addNode( new Node<SyntacticNode>( new SyntacticNode() ), node2Index );
		}

		parseTree.addNode( node1, node1Index );
		
		parseTree.relate(node1Index, node2Index);
		parseTree.relate(node2Index, node1Index);
		
		if( node.getType().matches("FS.*") ) {
			this.verbs.add(node1Index);
		}
				
	}
	
	
	
}
