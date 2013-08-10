package org.squadra.atenea.parser.model;

import lombok.Data;

public @Data class SyntacticNode {
	
	private String word;
	private String type;
	
	public SyntacticNode(){
		this.word = "";
		this.type = "";
	}	
	
	public SyntacticNode(String word, String type){
		this.word = word;
		this.type = type;
	}	
}
