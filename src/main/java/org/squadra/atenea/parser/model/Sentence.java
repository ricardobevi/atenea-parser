package org.squadra.atenea.parser.model;

import lombok.Data;

import org.squadra.atenea.base.graph.Graph;

public @Data class Sentence {

	private Graph<String> parseTree;
	
	public Sentence(){
		this.parseTree = new Graph<String>();
	}
	
}
