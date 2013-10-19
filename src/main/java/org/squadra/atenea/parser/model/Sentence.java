package org.squadra.atenea.parser.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

import org.squadra.atenea.base.graph.Graph;
import org.squadra.atenea.base.graph.Node;
import org.squadra.atenea.base.word.Word;
import org.squadra.atenea.base.word.WordTypes;

/**
 * Clase que representa la estructura de una oracion. 
 * Es cargada durante el analisis sintactico y utilizada por el semantico.
 * Contiene un grafo de palabras, un tipo y metodos para acceder a las 
 * diferentes partes de la oracion
 * @author Ricardo Bevilacqua
 * @author Leandro Morrone
 *
 */
public class Sentence {

	/** Arbol sintactico */
	private @Getter @Setter Graph<SyntacticNode> parseTree;
	
	/** Tipo de oracion */
	private @Getter @Setter Type type;
	
	/** Tipos de oracion o estructura sintactica */
	public enum Type {
		
		// Utilizados para los tipos de oracion
		ASSERTION, QUESTION, DIALOG, ORDER, UNKNOWN,
		
		// Utilizados para las sub-oraciones (estructuras sintacticaas)
		SUBJECT, DIRECT_OBJECT, INDIRECT_OBJECT, INTERROGATIVE, VERB
	}
	
	
	/**
	 * Constructor.
	 * Inicializa las variables y crea el nodo 0.
	 */
	public Sentence() {
		this.parseTree = new Graph<SyntacticNode>();
		this.parseTree.addNode(new Node<SyntacticNode>(new SyntacticNode()), 0);
		this.type = Type.UNKNOWN;
	}
	
	/**
	 * Constructor parametrizado por tipo.
	 * Inicializa las variables y crea el nodo 0.
	 * @param type Tipo de oracion.
	 */
	public Sentence(Type type) {
		this.parseTree = new Graph<SyntacticNode>();
		this.parseTree.addNode(new Node<SyntacticNode>(new SyntacticNode()), 0);
		this.type = type;
	}

	
	/**
	 * Relaciona un nuevo nodo con uno ya existente.
	 * @param node Nodo origen
	 * @param node1Index Indice del nodo origen
	 * @param node2Index Indice del nodo destino
	 */
	public void relateSyntacticNodes(SyntacticNode node, Integer node1Index, Integer node2Index) {
		
		Node<SyntacticNode> node1 = parseTree.getNode(node1Index);
		Node<SyntacticNode> node2 = parseTree.getNode(node2Index);
		
		// Si el nodo origen no existe, creo uno nuevo
		if ( node1 == null ) {
			parseTree.addNode( new Node<SyntacticNode>(node), node1Index );
		}
		// Si el nodo origen ya existe entonces solo le agrego los datos
		else {
			parseTree.getNode(node1Index).setData(node);
		}
		
		// Si el nodo destino no existe, creo uno vacio para relacionarlo
		if ( node2 == null ) {
			parseTree.addNode( new Node<SyntacticNode>(new SyntacticNode()), node2Index );
		}
		
		parseTree.relate(node1Index, node2Index);
	}
	

	/**
	 * Devuelve la oracion en forma de lista de Words
	 * @param contracted Si es false devuelve todas las palabras que devuelve
	 * la gramatica; si es true unifica las contracciones en una sola palabra
	 * (por ejemplo, "a el" -> "al", "jugar me" -> "jugarme".
	 * PARA INSERTAR NODOS EN LA BASE DE DATOS USAR CONTRACTION=TRUE.
	 * @return Lista de palabras (objetos Word) ordenados
	 */
	public ArrayList<Word> getAllWords(boolean contracted) {
		
		ArrayList<Word> words = new ArrayList<Word>();
		
		if (contracted) {
			
			int lastContraction = 0;
			ArrayList<Word> wordsToContract = new ArrayList<Word>();
			
	 		for (Node<SyntacticNode> node : parseTree.getGraph().values()) {
				if (node.getId() != 0) {
					
					int contraction = node.getData().getWord().getContraction();
					
					// Si termina una secuencia de palabras a contraer
					if (contraction <= lastContraction && lastContraction >= 2) {
						
						// Contraigo las palabras almacenadas en la lista
						String newName = "";
						for (Word word : wordsToContract) {
							newName += word.getName();
						}
						if (newName.toLowerCase().matches("ael|deel")) {
							System.out.println("MATCH");
							newName = newName.replaceFirst("e|E", "");
						}
						wordsToContract.get(0).setName(newName);
						words.add(wordsToContract.get(0));
						wordsToContract.clear();
					}
					
					// Si la palabra forma parte de una contraccion la guardo en
					// una lista temporal.
					if (contraction > 0) {
						wordsToContract.add(node.getData().getWord());
					}
					// Sino, la guardo en la lista de palabras a devolver
					else {
						words.add(node.getData().getWord());
					}
					lastContraction = contraction;
				}
			}
		}
		else {
	 		for (Node<SyntacticNode> node : parseTree.getGraph().values()) {
				if (node.getId() != 0) {
					words.add(node.getData().getWord());
				}
			}
		}
		return words;
	}
		
	/**
	 * Devuelve el sujeto de la oracion.
	 * @return Sub-oracion con el sujeto
	 */
	public ArrayList<Sentence> getSubjects() {
		ArrayList<Sentence> subSentences = getSubSentences(".*SUBJ.*", Type.SUBJECT);
		return subSentences;
	}
	
	/**
	 * Devuelve el verbo de la oracion.
	 * @return Sub-oracion con el verbo
	 */
	public ArrayList<Sentence> getVerbs() {
		ArrayList<Sentence> subSentences = new ArrayList<Sentence>();
		
		ArrayList<Node<SyntacticNode>> verbs = 
				getSyntacticNodeByType(".*FS.*|.*ICL.*(?!(AUX|SC)).*");
		
		for (Node<SyntacticNode> node : verbs) {
			
			Sentence subSentence = new Sentence(Type.VERB);
			
			// Agrego el primer verbo encontrado
			subSentence.relateSyntacticNodes(node.getData(), node.getId(), 0);
			
			// Agrego los verbos auxiliares y la negacion si existen
			addNegationAndAuxiliarVerbs(node, subSentence);
			
			subSentences.add(subSentence);
		}
		return subSentences;
	}
	
	private void addNegationAndAuxiliarVerbs(Node<SyntacticNode> verb, Sentence subSentence) {
		
		// Busco los nodos que se relacionan con el verbo
		for (Node<SyntacticNode> node : parseTree.getRelatedNodes(verb.getId())) {
			if (node.getData().getType().matches(".*ICL.*(AUX|SC).*")) {
				
				// Si hay un segundo verbo, lo relaciono al anterior
				subSentence.relateSyntacticNodes(
						node.getData(), node.getId(), verb.getId());
				
				// Llamo a la recursividad por si existen mas verbos
				addNegationAndAuxiliarVerbs(node, subSentence);
			}
			
			if (node.getData().getWord().getSubType() == 
					WordTypes.Type.AdverbSubtype.NEGATION) {
				
				// Si el verbo esta negado, agreo la negacion
				subSentence.relateSyntacticNodes(
						node.getData(), node.getId(), verb.getId());
			}
		}
	}
	
	
	/**
	 * Devuelve el objeto directo de la oracion.
	 * @return Sub-oracion con el objeto directo
	 */
	public ArrayList<Sentence> getDirectObject() {
		ArrayList<Sentence> subSentences = getSubSentences(".*ACC.*", Type.DIRECT_OBJECT);
		return subSentences;
	}
	
	/**
	 * Devuelve el objeto indirecto de la oracion.
	 * @return Sub-oracion con el objeto indirecto
	 */
	public ArrayList<Sentence> getIndirectObject() {
		ArrayList<Sentence> subSentences = getSubSentences(".*DAT.*", Type.INDIRECT_OBJECT);
		return subSentences;
	}
	
	/**
	 * Devuelve una lista de sustantivos (comunes y propios).
	 * @return Lista de Words sustantivos
	 */
	public ArrayList<Word> getNouns() {
		ArrayList<Word> nouns = new ArrayList<Word>();
		
		for (Node<SyntacticNode> node : parseTree.getGraph().values()) {
			if (node.getData().getWord().getType() == WordTypes.Type.NOUN ||
				node.getData().getWord().getType() == WordTypes.Type.PROPER_NAME) {
				
				nouns.add(node.getData().getWord());
			}
		}
		return nouns;
	}
	
	/**
	 * Devuelve una lista de adjetivos.
	 * @return Lista de Words adjetivos
	 */
	public ArrayList<Word> getAdjectives() {
		ArrayList<Word> nouns = new ArrayList<Word>();
		
		for (Node<SyntacticNode> node : parseTree.getGraph().values()) {
			if (node.getData().getWord().getType() == WordTypes.Type.ADJECTIVE) {
				
				nouns.add(node.getData().getWord());
			}
		}
		return nouns;
	}
	
	/**
	 * Devuelve una lista de los verbos principales.
	 * @return Lista de Words que son verbos principales
	 */
	public ArrayList<Word> getMainVerbs() {
		ArrayList<Word> mainVerbs = new ArrayList<Word>();
		
		for (Node<SyntacticNode> node : parseTree.getGraph().values()) {
			if (node.getData().getType().matches(".*(FS|ICL).*mv.*")) {
				mainVerbs.add(node.getData().getWord());
			}
		}
		return mainVerbs;
	}
	
	/**
	 * Indica si el verbo esta negado o es una afirmacion.
	 * Se recomienda su uso luego del getVerbs().
	 * @return true si es una negacion, false si es afirmacion.
	 */
	public boolean isNegation() {
		for (Node<SyntacticNode> node : parseTree.getGraph().values()) {
			if (node.getData().getWord().getSubType() == 
					WordTypes.Type.AdverbSubtype.NEGATION) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Devuelve una lista con las palabras interrogativas.
	 * @return Lista de Words que son interrogativas
	 */
	public ArrayList<Word> getQuestionWords() {
		ArrayList<Word> questionWords = new ArrayList<Word>();
		
		for (Node<SyntacticNode> node : parseTree.getGraph().values()) {
			if (node.getData().getWord().getType() == WordTypes.Type.INTERROGATIVE) {
				questionWords.add(node.getData().getWord());
			}
		}
		return questionWords;
	}

	
	/**
	 * Realiza una busqueda por niveles en el arbol de parsing de un tipo de nodo.
	 * Devuelve una Sentence con el subarbol.
	 * @param nodeType Expresion regular del tipo de nodo (ej: .*SUBJ.* FS.*)
	 * @return Subarbol en forma de Sentence.
	 */
	@SuppressWarnings("unused")
	private Sentence getSubSentence(String nodeType, Type subSentenceType) {
		
		Sentence subSentence = new Sentence(subSentenceType);
		
		int currentDistance = 1;
		ArrayList<Node<SyntacticNode>> nodes = parseTree.getNodesByDistanceToRoot(currentDistance);
		
		while (nodes.size() > 0) {
			
			for (Node<SyntacticNode> node : nodes) {
				if (node.getData().getType().matches(nodeType)) {
					
					subSentence.parseTree.addSubGraph(
							parseTree.subGraph(node.getId()), node.getId(), 0);
					
					return subSentence;
				}
			}
			currentDistance++;
			nodes = parseTree.getNodesByDistanceToRoot(currentDistance);
		}
		
		return subSentence;
	}
	
	/**
	 * Realiza una busqueda por niveles en el arbol de parsing de un tipo de nodo.
	 * Devuelve una lista de Sentences con cada subarbol encontrado.
	 * @param nodeType Expresion regular del tipo de nodo (ej: .*SUBJ.* FS.*)
	 * @return Listado de subarboles en forma de Sentence.
	 */
	private ArrayList<Sentence> getSubSentences(String nodeType, Type subSentenceType) {
		
		ArrayList<Sentence> subSentences = new ArrayList<Sentence>();
		
		int currentDistance = 1;
		ArrayList<Node<SyntacticNode>> nodes = parseTree.getNodesByDistanceToRoot(currentDistance);
		
		while (nodes.size() > 0) {
			
			for (Node<SyntacticNode> node : nodes) {
				if (node.getData().getType().matches(nodeType)) {
					
					Sentence subSentence = new Sentence(subSentenceType);
					
					subSentence.parseTree.addSubGraph(
							parseTree.subGraph(node.getId()), node.getId(), 0);
					
					subSentences.add(subSentence);
				}
			}
			currentDistance++;
			nodes = parseTree.getNodesByDistanceToRoot(currentDistance);
		}
		
		return subSentences;
	}
	
	
	/**
	 * Realiza una busqueda en el arbol de parsing y devuelve un conjunto de
	 * nodos con el tipo indicado.
	 * @param nodeType Expresion regular del tipo de nodo (ej: .*SUBJ.* FS.*)
	 * @return Listado de nodos sintacticos que coinciden con el tipo indicado.
	 */
	private ArrayList<Node<SyntacticNode>> getSyntacticNodeByType(String nodeType) {
		ArrayList<Node<SyntacticNode>> nodes = new ArrayList<Node<SyntacticNode>>();
		
		for (Node<SyntacticNode> node : parseTree.getGraph().values()) {
			if (node.getData().getType().matches(nodeType)) {
				nodes.add(node);
			}
		}
		return nodes;
	}
	
	
	/**
	 * Convierte el objeto Sentence es SimpleSentence (lista de Words)
	 * @param contracted Si es false devuelve todas las palabras que devuelve
	 * la gramatica; si es true unifica las contracciones en una sola palabra
	 * (por ejemplo, "a el" -> "al", "jugar me" -> "jugarme".
	 * @return SimpleSentence
	 */
	public SimpleSentence toSimpleSentence(boolean contracted) {
		return new SimpleSentence(this.getAllWords(contracted), this.type);
	}
	
	
	@Override
	public String toString() {
		String sentenceString = "";
		
		for(Node<SyntacticNode> node : parseTree.getGraph().values()) {
			if (node.getId() != 0) {
				if (node.getData().getWord().getType().equals(WordTypes.Type.PUNCTUATION)) {
					sentenceString += node.getData().getWord().getName();
				}
				else {
					sentenceString += " " + node.getData().getWord().getName();
				}
			}
		}
		return sentenceString.trim();
	}
}
