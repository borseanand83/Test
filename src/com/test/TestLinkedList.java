package com.test;

import java.util.LinkedList;
import java.util.StringTokenizer;


public class TestLinkedList {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		
	}

	static void listOperation(String list, String command) {
		StringTokenizer tkn = new StringTokenizer(list, ",");
		LinkedList linkedList = new TestLinkedList.LinkedList();
		while(tkn.hasMoreElements()){
			linkedList.appendToTail(new TestLinkedList.Node(Integer.parseInt(tkn.nextElement().toString()))); 
		}

		StringTokenizer tkn2 = new StringTokenizer(command, ",");
		while(tkn2.hasMoreElements()){
			if(tkn2.nextElement().toString() == "H"){
				Node head = linkedList.head;
				linkedList.head = head.next;
			}
			if(tkn2.nextElement().toString() == "T"){
				Node head = linkedList.head;
				Node current = head.next; 
				while(current != null)
				{ 
					current = current.next; 
				} 
	
			}
			
		}
    }
	static class LinkedList{
		
		public Node node;
		public Node head;
		public LinkedList() {
			this.head  = new Node(0);
		}
		
		public void appendToTail(Node node){
			Node current = head;
			 while(current.next!=null)
				 current = current.next;
			 current.next = node;
		}
		
		public boolean isCyclic(){
			Node fast = head;
			Node slow = head;
			
			while(fast!=null && fast.next!=null){
				fast = fast.next.next;
				slow = slow.next;
				
				if(fast == slow)
					return true;
							
			}
			return false;
		}
		
		@Override 
		public String toString(){ 
			StringBuilder sb = new StringBuilder(); 
			Node current = head.next; 
			while(current != null)
			{ 
				sb.append(current).append("-->"); 
				current = current.next; 
			} 
			sb.delete(sb.length() - 3, sb.length()); // to remove --> from last node return sb.toString(); 
			return sb.toString();
		}

	}
	
	static class Node{
		private int data;
		private Node next;
		private Node prev;
		
		public Node(int data) {
			this.data = data;
		}
		
		public void setNext(Node next) {
			this.next = next;
		}
		
		public void setPrev(Node prev) {
			this.prev = prev;
		}
	}
}
