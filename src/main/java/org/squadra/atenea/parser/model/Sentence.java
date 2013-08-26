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
		SUBJECT, DIRECT_OBJECT, INDIRECT_OBJECT, INTERROGATIVE, MAIN_VERB
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
		Sentence subSentence = getSubSentence("SUBJ");
		subSentence.setType(Type.SUBJECT);
		return subSentence;
	}
	
	/**
	 * Devuelve el objeto directo de la oracion.
	 * @return Sub-oracion con el objeto directo
	 */
	public Sentence getDirectObject() {
		Sentence subSentence = getSubSentence("ACC");
		subSentence.setType(Type.DIRECT_OBJECT);
		return subSentence;
	}
	
	/**
	 * Devuelve el objeto indirecto de la oracion.
	 * @return Sub-oracion con el objeto indirecto
	 */
	public Sentence getIndirectObject() {
		Sentence subSentence = getSubSentence("DAT");
		subSentence.setType(Type.INDIRECT_OBJECT);
		return subSentence;
	}

	/**
	 * Realiza una busqueda por niveles en el arbol de parsing de un tipo de nodo.
	 * Por cada match del tipo 
	 * @param nodeType Tipo de nodo (SUBJ, ACC, FS*, DAT, etc).
	 * @return Subarbol en forma de Sentence.
	 */
	private Sentence getSubSentence(String nodeType) {
		
		Sentence subSentence = new Sentence();
		
		int currentDistance = 1;
		ArrayList<Node<SyntacticNode>> nodes = parseTree.getNodesByDistance(currentDistance);
		
		while (nodes.size() > 0) {
			
			for (Node<SyntacticNode> node : nodes) {
				if (node.getData().getType().contains(nodeType)) {
					
					subSentence.parseTree.addSubGraph(
							parseTree.subGraph(node.getId()), node.getId(), 0);
					
					// Comentar este return para devolver todos los subgrafos.
					return subSentence;
				}
			}
			currentDistance++;
			nodes = parseTree.getNodesByDistance(currentDistance);
		}
		
		return subSentence;
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
