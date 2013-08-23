package org.squadra.atenea.parser.model;

import java.util.ArrayList;
import java.util.HashSet;

import lombok.Data;

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
public @Data class Sentence {

	/** Arbol sintactico */
	private Graph<SyntacticNode> parseTree;
	
	/** Tipo de oracion */
	private Type type;
	
	/** Tipos de oracion (afirmacion, pregunta, orden, etc.) */
	public enum Type {
		ASSERTION, QUESTION, INTERJECTION, ORDER, UNKNOWN
	}
	
	
	HashSet<Integer> verbs;
	
	public Sentence(){
		this.verbs = new HashSet<Integer>();
		this.parseTree = new Graph<SyntacticNode>();
		Node<SyntacticNode> node0 = new Node<SyntacticNode>(new SyntacticNode());
		this.parseTree.addNode( node0, 0 );
		this.type = Type.UNKNOWN;
	}

	
	/**
	 * Relaciona un nuevo nodo con uno ya existente.
	 * @param node Nodo origen
	 * @param node1Index Indice del nodo origen
	 * @param node2Index Indice del nodo destino
	 */
	public void relateNodes(SyntacticNode node, Integer node1Index, Integer node2Index){
		
		Node<SyntacticNode> node1 = new Node<SyntacticNode>( node );
		Node<SyntacticNode> node2 = parseTree.getNode(node2Index);
		
		if( node2 == null ){
			parseTree.addNode( new Node<SyntacticNode>( new SyntacticNode() ), node2Index );
		}

		parseTree.addNode( node1, node1Index );
		
		parseTree.relate(node1Index, node2Index);
		parseTree.relate(node2Index, node1Index);
		
		if( node.getType().matches("FS.*") ) {
			this.verbs.add(node1Index);
		}
				
	}
	
	
	/**
	 * Asigna un tipo a la oracion segun su contenido y lo devuelve.
	 * @return tipo de oracion
	 */
	public Type getType() {
		
		if (type == Type.UNKNOWN) {
			
			// TODO: completar para los demas tipos de oracion
			
			for(Node<SyntacticNode> node : parseTree.getGraph().values()) {
				if (node.getId() != 0) {
					if (node.getData().getWord().getType() == WordTypes.Type.INTERROGATIVE) {
						type = Type.QUESTION;
					}
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
