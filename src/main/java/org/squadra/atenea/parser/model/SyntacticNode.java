package org.squadra.atenea.parser.model;

import lombok.Data;

import org.squadra.atenea.base.word.Word;

/**
 * Clase que representa el nodo de una oracion.
 * Contiene una palabra y un tipo.
 * @author Ricardo Bevilacqua
 *
 */
public @Data class SyntacticNode {
	
	private Word word;
	private String type;
	
	public SyntacticNode(){
		this.word = new Word();
		this.type = "";
	}	
	
	public SyntacticNode(Word word, String type){
		this.word = word;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "[word=" + word.getName() + ", type=" + type + "]";
	}
}
