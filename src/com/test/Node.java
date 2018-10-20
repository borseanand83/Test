package com.test;

public class Node<E> {
	
	private E data;
	private Node<E> next;
	private Node<E> prev;
	
	public Node() {
		this.data = (E) "Head";
		this.next = null;
		this.prev = null;
	}

}
