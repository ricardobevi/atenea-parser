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
	private @Setter Type type;
	
	/** Tipos de oracion o estructura sintactica */
	public enum Type {
		
		// Utilizados para los tipos de oracion
		ASSERTION, QUESTION, INTERJECTION, ORDER, UNKNOWN,
		
		// Utilizados para las sub-oraciones (estructuras sintacticaas)
		SUBJECT, DIRECT_OBJECT, INDIRECT_OBJECT, INTERROGATIVE, VERB
	}
	
	
	/**
	 * Constructor.
	 * Inicializa las variables y crea el nodo 0.
	 */
	public Sentence() {
		this.parseTree = new Graph<SyntacticNode>();
		Node<SyntacticNode> node0 = new Node<SyntacticNode>(new SyntacticNode());
		this.parseTree.addNode(node0, 0);
		this.type = Type.UNKNOWN;
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
	 * Devuelve el tipo de oracion, y si es desconocido lo calcula.
	 * @return tipo de oracion
	 */
	public Type getType() {
		if (type == Type.UNKNOWN) {
			calculateType();
		}
		return type;
	}
	
	
	/**
	 * Asigna un tipo a la oracion segun su contenido y lo devuelve.
	 * @return tipo de oracion
	 */
	private Type calculateType() {
		
		// TODO: completar para los demas tipos de oracion
		
		for(Node<SyntacticNode> node : parseTree.getGraph().values()) {
			if (node.getId() != 0) {
				if (node.getData().getWord().getType() == WordTypes.Type.INTERROGATIVE) {
					type = Type.QUESTION;
				}
			}
		}
		return type;
	}
	

	/**
	 * Devuelve la oracion en forma de lista de Words
	 * @return Lista de palabras (objetos Word) ordenados
	 */
	public ArrayList<Word> getAllWords() {
		ArrayList<Word> words = new ArrayList<Word>();
		
		for (Node<SyntacticNode> node : parseTree.getGraph().values()) {
			if (node.getId() != 0) {
				words.add(node.getData().getWord());
			}
		}
		return words;
	}
		
	/**
	 * Devuelve el sujeto de la oracion.
	 * @return Sub-oracion con el sujeto
	 */
	public Sentence getSubject() {
		Sentence subSentence = getSubSentence(".*SUBJ.*");
		subSentence.setType(Type.SUBJECT);
		return subSentence;
	}
	
	/**
	 * Devuelve el verbo de la oracion.
	 * @return Sub-oracion con el verbo
	 */
	public Sentence getVerb() {
		Sentence subSentence = new Sentence();
		
		ArrayList<Node<SyntacticNode>> verbs = 
				getSyntacticNodeByType(".*FS.*|.*ICL.*(?!(AUX|SC)).*");
		
		if (verbs.size() > 0) {
			
			// Agrego el primer verbo encontrado
			subSentence.relateSyntacticNodes(
					verbs.get(0).getData(), verbs.get(0).getId(), 0);
			
			// Busco si se relaciona con algun auxiliar
			for (Node<SyntacticNode> node1 : parseTree.getRelatedNodes(verbs.get(0).getId())) {
				if (node1.getData().getType().matches(".*ICL.*(AUX|SC).*")) {
					
					// Agrego el segundo verbo
					subSentence.relateSyntacticNodes(
							node1.getData(), node1.getId(), verbs.get(0).getId());
					
					// Busco si se relaciona con otro auxiliar
					for (Node<SyntacticNode> node2 : parseTree.getRelatedNodes(node1.getId())) {
						if (node2.getData().getType().matches(".*ICL.*(AUX|SC).*")) {
							
							// Agrego el tercer verbo
							subSentence.relateSyntacticNodes(
									node2.getData(), node2.getId(), node1.getId());
						}
					}
				}
			}
		}
		subSentence.setType(Type.VERB);
		return subSentence;
	}
	
	/**
	 * Devuelve el objeto directo de la oracion.
	 * @return Sub-oracion con el objeto directo
	 */
	public Sentence getDirectObject() {
		Sentence subSentence = getSubSentence(".*ACC.*");
		subSentence.setType(Type.DIRECT_OBJECT);
		return subSentence;
	}
	
	/**
	 * Devuelve el objeto indirecto de la oracion.
	 * @return Sub-oracion con el objeto indirecto
	 */
	public Sentence getIndirectObject() {
		Sentence subSentence = getSubSentence(".*DAT.*");
		subSentence.setType(Type.INDIRECT_OBJECT);
		return subSentence;
	}

	
	/**
	 * Realiza una busqueda por niveles en el arbol de parsing de un tipo de nodo.
	 * Devuelve una Sentence con el subarbol.
	 * @param nodeType Expresion regular del tipo de nodo (ej: .*SUBJ.* FS.*)
	 * @return Subarbol en forma de Sentence.
	 */
	private Sentence getSubSentence(String nodeType) {
		
		Sentence subSentence = new Sentence();
		
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
	private ArrayList<Sentence> getSubSentences(String nodeType) {
		
		ArrayList<Sentence> subSentences = new ArrayList<Sentence>();
		
		int currentDistance = 1;
		ArrayList<Node<SyntacticNode>> nodes = parseTree.getNodesByDistanceToRoot(currentDistance);
		
		while (nodes.size() > 0) {
			
			for (Node<SyntacticNode> node : nodes) {
				if (node.getData().getType().matches(nodeType)) {
					
					Sentence subSentence = new Sentence();
					
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
	
	
	@Override
	public String toString() {
		String sentenceString = "";
		
		for(Node<SyntacticNode> node : parseTree.getGraph().values()) {
			if (node.getId() != 0) {
				sentenceString += node.getData().getWord().getName() + " ";
			}
		}
		return sentenceString;
	}
}
