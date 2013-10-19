package org.squadra.atenea.parser.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

import org.squadra.atenea.base.word.Word;
import org.squadra.atenea.base.word.WordTypes;
import org.squadra.atenea.parser.model.Sentence.Type;

public class SimpleSentence {

	/** Palabras que componen la oracion */
	private @Getter @Setter ArrayList<Word> words; 
	
	/** Tipo de oracion */
	private @Getter @Setter Type type;
	
	
	public SimpleSentence() {
		this.words = new ArrayList<Word>();
		this.type = Type.UNKNOWN;
	}
	
	public SimpleSentence(ArrayList<Word> words) {
		this.words = new ArrayList<Word>(words);
		this.type = Type.UNKNOWN;
	}
	
	public SimpleSentence(ArrayList<Word> words, Type type) {
		this.words = new ArrayList<Word>(words);
		this.type = type;
	}

	@Override
	public String toString() {
		String sentence = "";
		
		for (Word word : words) {
			if (word.getType().equals(WordTypes.Type.PUNCTUATION)) {
				sentence += word.getName();
			} else { 
				sentence += " " + word.getName();
			}
		}
		
		return sentence.trim();
	}
	
	
}
