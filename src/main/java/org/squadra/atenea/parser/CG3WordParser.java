package org.squadra.atenea.parser;

import java.util.Arrays;

import org.squadra.atenea.base.word.*;

/**
 * Esta clase se encarga de parsear la salida del analizador CG3 para una palabra en
 * particular y generar un objeto Word correspondiente a dicha palabra con sus tipos.
 * @author Leandro Morrone
 *
 */
public class CG3WordParser {

	private String name 	= WordTypes.VOID;
	private String baseWord = WordTypes.VOID;
	private String type 	= WordTypes.VOID;
	private String subType 	= WordTypes.VOID;
	private String gender 	= WordTypes.VOID;
	private String number 	= WordTypes.VOID;
	private String mode 	= WordTypes.VOID;
	private String tense 	= WordTypes.VOID;
	private String person 	= WordTypes.VOID;
	private boolean heuristic = false;
	
	/**
	 * Constructor
	 */
	public CG3WordParser() { }
	
	/**
	 * Se encarga de parsear la entrada y generar un objeto Word.
	 * @param cgWord Cadena obtenida del analizador CG3
	 * @return Objeto Word correspondiente a la palabra.
	 */
	public Word parseWord(String cgWord) {
		
		if (isPunctuationSign(cgWord)) {
			setPunctuationFields(cgWord);
		}
		else if (isNumber(cgWord)) {
			setNumberFields(cgWord);
		}
		else {
			setWordFields(cgWord);
		}
		return new Word(name, baseWord, type, subType, gender, number, 
				mode, tense, person, heuristic);
	}
	

	/**
	 * Si es un signo de puntuacion setea los campos correspondientes al signo.
	 * @param cgWord Cadena obtenida del analizador CG3
	 */
	private void setPunctuationFields(String cgWord) {
		name = cgWord.substring(1, 2);
		baseWord = name;
		type = WordTypes.Type.PUNCTUATION;
	}
	
	/**
	 * Si es un numero setea los campos correspondientes al un numero.
	 * @param cgWord Cadena obtenida del analizador CG3
	 */
	private void setNumberFields(String cgWord) {
		name = cgWord.substring(0, cgWord.indexOf(" ") - 1);
		baseWord = name;
		type = WordTypes.Type.NUMERIC;
		gender = WordTypes.Gender.NEUTRAL;
		number = WordTypes.Number.PLURAL;
	}

	/**
	 * Si es una palabra setea los campos correspondientes al tipo de palabra
	 * @param cgWord Cadena obtenida del analizador CG3
	 */
	private void setWordFields(String cgWord) {
		name = getName(cgWord);
		baseWord = getBaseWord(cgWord);
		String[] lexicalTags = getLexicalTags(cgWord);
		String[] syntacticTags = getSyntacticTags(cgWord);
		setHeuristic(syntacticTags);
		
		System.out.println(
				Arrays.asList(lexicalTags) + " " + Arrays.asList(syntacticTags));
		
		switch (lexicalTags[0]) {
		
			// es sustantivo (cargo tipo, genero y numero)
			case "N":
				type = WordTypes.Type.NOUN;
				setGender(lexicalTags[1]);
				setNumber(lexicalTags[2]);
				break;
				
			// es adjetivo (cargo tipo, genero y numero)
			case "ADJ":
				type = WordTypes.Type.ADJECTIVE;
				setGender(lexicalTags[1]);
				setNumber(lexicalTags[2]);
				break;
				
			// es verbo (cargo tipo, modo, tiempo y persona)
			case "V":
				type = WordTypes.Type.VERB;
				setModeTenseAndPerson(lexicalTags);
				break;
				
			// es nombre propio (cargo tipo, genero y numero)
			case "PROP":
				type = WordTypes.Type.PROPER_NAME;
				setGender(lexicalTags[1]);
				setNumber(lexicalTags[2]);
				break;
			
			// es preposicion (solo cargo el tipo)
			case "PRP":
				type = WordTypes.Type.PREPOSITION;
				break;
			
			// es numerico (cargo tipo, genero y numero)
			case "NUM":
				type = WordTypes.Type.NUMERIC;
				setGender(lexicalTags[1]);
				setNumber(lexicalTags[2]);
				break;
			
			// es interjeccion (solo cargo el tipo)
			case "IN":
				type = WordTypes.Type.INTERJECTION;
				break;
			
			// es conjuncion coordinada (cargo tipo y subtipo)
			case "KC":
				type = WordTypes.Type.CONJUNCTION;
				subType = WordTypes.Type.ConjunctionSubtype.COORDINATING;
				break;
				
			// es conjuncion subodinada (cargo tipo y subtipo)
			case "KS":
				type = WordTypes.Type.CONJUNCTION;
				subType = WordTypes.Type.ConjunctionSubtype.SUBORDINATING;
				break;
				
			// es pronombre personal (cargo tipo y subtipo)
			case "PERS":
				type = WordTypes.Type.PERSONAL_PRONOUN;
				setPersonalPronounSubtype(lexicalTags);
				break;
								
			// es pronombre no personal o articulo (cargo tipo, subtipo, genero y numero)
			case "DET":
			case "INDP":
				setPronounAndArticlesSubtype(lexicalTags, syntacticTags);
				break;
				
			// es pronombre no personal o articulo (cargo tipo, subtipo, genero y numero)
			case "ADV":
				setAdverbSubtype(syntacticTags);
				break;
			
			// si no es nada de lo anterior, que Dios nos ampare.
			default:
				type = WordTypes.UNKNOWN;
		}
	}
	

	/**
	 * Setea el genero segun la sigla.
	 * @param genderTag Siglas del genero
	 */
	private void setGender(String genderTag) {
		switch (genderTag) {
			case "M":
				gender = WordTypes.Gender.MALE;
				break;
			case "F":
				gender = WordTypes.Gender.FAMALE;
				break;
			case "MF":
				gender = WordTypes.Gender.NEUTRAL;
				break;
			default:
				gender = WordTypes.UNKNOWN;
		}
	}
	
	/**
	 * Setea el numero segun la sigla
	 * @param numberTag Siglas del numero
	 */
	private void setNumber(String numberTag) {
		switch (numberTag) {
			case "S":
				number = WordTypes.Number.SINGULAR;
				break;
			case "P":
				number = WordTypes.Number.PLURAL;
				break;
			case "SP":
				number = WordTypes.Number.INDEFINITE;
				break;
			default:
				number = WordTypes.UNKNOWN;
		}
	}
	
	/**
	 * Setea la persona segun la sigla
	 * @param personTag Siglas de la persona
	 */
	private void setPerson(String personTag) {
		switch (personTag) {
			case "1S":
				person = WordTypes.Person._1S;
				break;
			case "2S":
				person = WordTypes.Person._2S;
				break;
			case "3S":
				person = WordTypes.Person._3S;
				break;
			case "1P":
				person = WordTypes.Person._1P;
				break;
			case "2P":
				person = WordTypes.Person._2P;
				break;
			case "3P":
				person = WordTypes.Person._3P;
				break;
			case "1/3S":
				person = WordTypes.Person._1y3S;
				break;
			case "3S/P":
				person = WordTypes.Person._3SyP;
				break;
			default:
				person = WordTypes.UNKNOWN;
		}
	}
	
	
	/**
	 * Setea el modo, el tiempo verbal y la persona (utilizada para verbos)
	 * @param lexicalTags
	 */
	private void setModeTenseAndPerson(String[] lexicalTags) {
		if (lexicalTags[1].equals("PR") ||
				lexicalTags[1].equals("IMPF") ||
				lexicalTags[1].equals("PS") ||
				lexicalTags[1].equals("FUT")) {
			
			if (lexicalTags[3].equals("IND")) {
				mode = WordTypes.Mode.INDICATIVE;
			}
			else if(lexicalTags[3].equals("SUBJ")) {
				mode = WordTypes.Mode.SUBJUNCTIVE;
			}
			
			if (lexicalTags[1].equals("PR")) {
				tense = WordTypes.Tense.PRESENT;
			}
			else if (lexicalTags[1].equals("PS")) {
				tense = WordTypes.Tense.PAST_SIMPLE;
			}
			else if (lexicalTags[1].equals("IMPF")) {
				tense = WordTypes.Tense.PAST_IMPERFECT;
			}
			else if (lexicalTags[1].equals("FUT")) {
				tense = WordTypes.Tense.FUTURE;
			}
			
			setPerson(lexicalTags[2]);
		}
		else if (lexicalTags[1].equals("IMP")) {
			mode = WordTypes.Mode.IMPERATIVE;
			setPerson(lexicalTags[2]);
		}
		else if (lexicalTags[1].equals("INF")) {
			mode = WordTypes.Mode.INFINITIVE;
		}
		else if (lexicalTags[1].equals("GER")) {
			mode = WordTypes.Mode.GERUND;
		}
		else if (lexicalTags[1].equals("PCP")) {
			mode = WordTypes.Mode.PARTICIPLE;
			setGender(lexicalTags[2]);
			setNumber(lexicalTags[3]);
		}
		else if (lexicalTags[1].equals("COND")) {
			mode = WordTypes.Mode.INDICATIVE;
			tense = WordTypes.Tense.CONDITIONAL;
			setPerson(lexicalTags[3]);
		}
	}
	

	/**
	 * Setea el subtipo de los pronombres personales
	 * @param lexicalTags
	 */
	private void setPersonalPronounSubtype(String[] lexicalTags) {
		setGender(lexicalTags[1]);
		setPerson(lexicalTags[2]);
		
		if (lexicalTags[3].equals("NOM")) {
			subType = WordTypes.Type.PersonalPronounSubtype.NOMINATIVE;
		}
		else if (lexicalTags[3].equals("PIV")) {
			subType = WordTypes.Type.PersonalPronounSubtype.PREPOSITIONAL;
		}
		else if (lexicalTags[3].equals("DAT")) {
			subType = WordTypes.Type.PersonalPronounSubtype.DATIVE;
		}
		else if (lexicalTags[3].equals("ACC")) {
			subType = WordTypes.Type.PersonalPronounSubtype.ACCUSATIVE;
		}
		else if (lexicalTags[3].equals("ACC/DAT")) {
			subType = WordTypes.Type.PersonalPronounSubtype.REFLEXIVE;
		}
		else if (lexicalTags[3].equals("NOM/PIV")) {
			subType = WordTypes.Type.PersonalPronounSubtype.NOMINATIVE;
		}
	}


	/**
	 * Setea los tipos y subtipos de articulos y pronombres
	 * @param lexicalTags
	 * @param syntacticTags
	 */
	private void setPronounAndArticlesSubtype(String[] lexicalTags, 
			String[] syntacticTags) {
		
		// Si es DET y tiene <arti> => es un articulo indefinido
		if (Arrays.asList(syntacticTags).contains("arti") &&
				lexicalTags[0].equals("DET")) {
			
			type = WordTypes.Type.ARTICLE;
			subType = WordTypes.Type.ArticleSubtype.INDEFINITIVE;
		}
		
		// Si es DET y tiene <artd> => es un articulo definido
		else if (Arrays.asList(syntacticTags).contains("artd") &&
				lexicalTags[0].equals("DET")) {
			
			if (name.toLowerCase().matches("el|la|los|las|lo")) {
				type = WordTypes.Type.ARTICLE;
				subType = WordTypes.Type.ArticleSubtype.DEFINITIVE;
			}
			// Pero si no es "el|la|los|las|lo" es un demostrativo
			else {
				type = WordTypes.Type.DEMONSTRATIVE;
			}
		}
		
		// Si tiene <dem> es un demostrativo
		else if (Arrays.asList(syntacticTags).contains("dem")) {
			
			type = WordTypes.Type.DEMONSTRATIVE;
		}
		
		else {
			
			// Hay pronombres con muchos tags, entonces busco el primero
			String firstTag = "";
			for (String tag : syntacticTags) {
				if (tag.matches("poss|rel|interr|quant.*")) {
					firstTag = tag;
					break;
				}
			}
			
			// Si tiene <poss> es un posesivo
			if (firstTag.equals("poss")) {
	
				type = WordTypes.Type.POSSESSIVE;
				// obtengo la persona del posesivo en la posicion siguiente a <poss>
				setPerson(Arrays.asList(syntacticTags).get(
						Arrays.asList(syntacticTags).indexOf("poss") + 1));
			}
			
			// Si tiene <rel> es un relativo
			else if (firstTag.equals("rel")) {
				type = WordTypes.Type.RELATIVE;
			}
			
			// Si tiene <interr> es un interrogativo/exclamativo
			else if (firstTag.equals("interr")) {
				type = WordTypes.Type.INTERROGATIVE;
			}
			
			// Si tiene <quant*> es un indefinido
			else if (firstTag.matches("quant[0-9]")) {
				type = WordTypes.Type.INDEFINITE;
			}
			else {
				type = WordTypes.UNKNOWN;
			}
		}
		
		setGender(lexicalTags[1]);
		setNumber(lexicalTags[2]);
	}
	
	
	/**
	 * Setea el subtipo de los adverbios
	 * @param syntacticTags
	 */
	private void setAdverbSubtype(String[] syntacticTags) {

		// Hay adverbios con muchos tags, entonces busco el primero
		String firstTag = "";
		for (String tag : syntacticTags) {
			if (tag.matches("aloc|adir|atemp|amode|amanner|ameta|aquant|rel|interr|kc")) {
				firstTag = tag;
				break;
			}
		}
		
		switch (firstTag) {
			case "aloc":
			case "adir":
				type = WordTypes.Type.ADVERB; 
				subType = WordTypes.Type.AdverbSubtype.LOCATION;
				break;
			case "atemp":
				type = WordTypes.Type.ADVERB; 
				subType = WordTypes.Type.AdverbSubtype.TIME;
				break;
			case "amod":
			case "amanner":
			case "ameta":
				type = WordTypes.Type.ADVERB; 
				subType = WordTypes.Type.AdverbSubtype.MODE;
				break;
			case "aquant":
				type = WordTypes.Type.ADVERB; 
				subType = WordTypes.Type.AdverbSubtype.QUANTIFIER;
				break;
			case "rel":
				type = WordTypes.Type.RELATIVE;
				break;
			case "interr":
				type = WordTypes.Type.INTERROGATIVE;
				break;
			case "kc":
				type = WordTypes.Type.CONJUNCTION;
				subType = WordTypes.Type.ConjunctionSubtype.COORDINATING;
				break;
			default:
				type = WordTypes.Type.ADVERB;
				if (baseWord.matches("no|tampoco")) {
					subType = WordTypes.Type.AdverbSubtype.NEGATION;
				}
				else if (baseWord.matches("sí|también")) {
					subType = WordTypes.Type.AdverbSubtype.AFFIRMATION;
				}
				else if (baseWord.matches("quizá|quizás|acaso|tal=vez|probablemente|posiblemente")) {
					subType = WordTypes.Type.AdverbSubtype.DOUBT;
				}
				else if (name.matches(".*mente$")) {
					subType = WordTypes.Type.AdverbSubtype.MODE;
				}
				else {
					// gran parte de los que caigan aca son de MODE
					subType = WordTypes.UNKNOWN;
				}
		}
		
	}
	
	
	/**
	 * Setea si el tipo de palabra fue calculado por heuristica (es utilizada 
	 * por las palabras pero no por los signos de puntuacion)
	 * @param syntacticTags
	 */
	private void setHeuristic(String[] syntacticTags) {
		heuristic = Arrays.asList(syntacticTags).contains("heur") ||
					Arrays.asList(syntacticTags).contains("heurtag");
	}
	
	/**
	 * @param cgWord Cadena obtenida del analizador CG3
	 * @return Devuelve true si es un signo de puntuacion y false si no lo es.
	 */
	private boolean isPunctuationSign(String cgWord) {
		return cgWord.charAt(0) == '$';
	}	
	
	/**
	 * @param cgWord Cadena obtenida del analizador CG3
	 * @return Devuelve true si es un numero y false si es palabra.
	 */
	private boolean isNumber(String cgWord) {
		try {
			Double.parseDouble(cgWord.substring(0, cgWord.indexOf(" ")));
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Parsea la entrada para obtener el nombre de la palabra
	 * @param cgWord Cadena obtenida del analizador CG3
	 * @return Palabra propiamente dicha
	 */
	private String getName(String cgWord) {
		return cgWord.substring(0, cgWord.indexOf("[") - 1);
	}
	
	/**
	 * Parsea la entrada para obtener la palabra base
	 * @param cgWord Cadena obtenida del analizador CG3
	 * @return Palabra base de la palabra
	 */
	private String getBaseWord(String cgWord) {
		return cgWord.substring(cgWord.indexOf("[") + 1, cgWord.indexOf("]"));
	}
	
	/**
	 * Parsea la entrada para obtener los tags lexicos (tipo, genero, numero, persona)
	 * @param cgWord Cadena obtenida del analizador CG3
	 * @return Array con los tags lexicos
	 */
	private String[] getLexicalTags(String cgWord) {
		if (cgWord.indexOf(">") >= 0) {
			return cgWord.substring(cgWord.lastIndexOf(">") + 2).split(" ");
		}
		return cgWord.substring(cgWord.lastIndexOf("]") + 2).split(" ");
	}
	
	/**
	 * Parsea la entrada para obtener los tags sintacticos (subtipos y otros)
	 * @param cgWord Cadena obtenida del analizador CG3
	 * @return Array con los tags sintacticos
	 */
	private String[] getSyntacticTags(String cgWord) {
		if (cgWord.indexOf(">") >= 0) {
			return cgWord.substring(cgWord.indexOf("<") + 1, cgWord.lastIndexOf(">"))
					.replaceAll("[<>]", "").split(" ");
		}
		String[] emptyArray = {};
		return emptyArray;
	}
	
}
