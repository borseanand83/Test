package com.test;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class GraphDS {
	
	private PriorityQueue<Vertex> graphQueue = new PriorityQueue<Vertex>();
	
	public void addVertex(Vertex node) {
		graphQueue.add(node);
	}
	

	public static void main(String[] args) {
		GraphDS graph = new GraphDS();
		Vertex node = new Vertex("Anand");
		Vertex node2 = new Vertex("Atharva");
		Vertex node3 = new Vertex("Priyanka");
		Vertex node4 = new Vertex("IBM");
		Vertex node5 = new Vertex("Mel");
		node.setEdge(node2);
		node.setEdge(node3);
		node.setEdge(node4);
		node.setEdge(node5);
		node3.setEdge(node5);
		node2.setEdge(node5);
		graph.addVertex(node);
		graph.addVertex(node2);
		graph.addVertex(node3);
		graph.addVertex(node4);
		graph.addVertex(node5);
		graph.traverseBFS(node);
	}
	
	public void traverseBFS(Vertex rootNode) {
		Queue<Vertex> queue = new LinkedList<Vertex>();
		if(rootNode==null)
			return;
		queue.add(rootNode);
		while(!queue.isEmpty()) {
			Vertex node = queue.poll();
			System.out.println(node.getData());
			for (Vertex vertex : node.getEdges()) {
				queue.add(vertex);
			}
		}
		
		
		
	}
}

class Vertex implements Comparable<Vertex>{
	private HashSet<Vertex> edges;
	private String data;
	Vertex(String data){
		edges = new HashSet<Vertex>();
		this.data = data;
	}
	
	public void setEdge(Vertex node) {
		edges.add(node);
	}
	
	public HashSet<Vertex> getEdges() {
		return edges;
	}
	
	public String getData() {
		return data;
	}

	@Override
	public int compareTo(Vertex v) {
		if(this.getEdges().size()==v.getEdges().size())
			return 0;
		else
			return -1;
	}

	

	
	
}