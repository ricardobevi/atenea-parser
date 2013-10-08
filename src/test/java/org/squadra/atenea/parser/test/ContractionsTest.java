package org.squadra.atenea.parser.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.squadra.atenea.base.word.Word;
import org.squadra.atenea.parser.Parser;
import org.squadra.atenea.parser.model.Sentence;

public class ContractionsTest {

	@Test
	public void test() {
			
		processContractions("Tengo que encontrarlo solo.");
		processContractions("Tengo que encontrarmelo solo.");
		processContractions("Tengo que encontrarlo al primer intento.");
		processContractions("Tengo que encontrarmelo al gorro del niño.");
		
		assertTrue(true);
	}
	
	
	private void processContractions(String txt) {
		Sentence sentence = new Parser().parse(txt);
		System.out.println();
		printWords(sentence.getAllWords(false));
		printWords(sentence.getAllWords(true));
		System.out.println();
	}
	
	private void printWords(ArrayList<Word> words) {
		String str = "";
		for (Word word : words) {
			str += word.getName() + " ";
		}
		System.out.println("--> " + str);
	}

}
