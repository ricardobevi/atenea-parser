package org.squadra.atenea.parser.model;

import java.util.HashMap;

import lombok.Data;

import org.squadra.atenea.base.graph.Graph;

public @Data class Sentence {

	private Graph<SyntacticNode> parseTree;
	
	private HashMap<String, String> sentencePart;
	
	public Sentence(){
		this.parseTree = new Graph<SyntacticNode>();
	}
	
	public Sentence(Graph<SyntacticNode> parseTree){
		this.parseTree = parseTree;
	}
	
	
	
}
