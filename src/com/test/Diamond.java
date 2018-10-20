package com.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.test.TestLinkedList.Node;

public class Diamond {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int max = 3;
		for(int i =1 ;i< max;i++)
		{
			int temp =0;
			while(temp < (max-i)+3){
				System.out.print(" ");
				temp++;
			}
			temp=0;
			while(temp< i){
				System.out.print("*");
				temp++;
			}
			System.out.print("\n");
		}

		for(int i =max ;i>=0;i--)
		{
			int temp =i;
			while(temp < (max+i)){
				System.out.print(" ");
				temp++;
			}
			temp=i;
			while(temp > 0){
				System.out.print("*");
				temp--;
			}
			System.out.print("\n");
		}

		findChar("aanaajjtnnvdndghsshshja");
		Integer[] arr = {1,0,0,0,1,0,1,0,1};
		sort(arr);
		System.out.println(arr.toString());
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(1);
		list.add(8);
		
		list.remove(3);
		
		System.out.println(list.toArray().toString());
	}
	
	
	public static void findChar(String str){
//		String[] arr = str.toCharArray();
		int cnt=0;
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(int i =0;i< str.length();i++){
			if(map.containsKey(str.substring(i, i+1)))
			{
				cnt = ((Integer)map.get(str.substring(i, i+1)));
				cnt++;
				map.put(str.substring(i, i+1), cnt);
			}
			else{
				map.put(str.substring(i, i+1), 1);
			}
			
		}
		
		Iterator<Entry<String,Integer>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String,Integer> entry = it.next();
			System.out.println("Key :"+entry.getKey()+"  value ::"+entry.getValue());
		}
	}

	
	public static void sort(Integer[] arr){
		Integer[] zeroesArr =new Integer[10] ;
		Integer[] onesArry =new Integer[10];
		int temp =0;
		int temp2 =0;
		for(int i=0;i<arr.length;i++){
			if(arr[i]==0){
				zeroesArr[temp]= arr[i];
				temp++;
				arr[i]=null;
			}
			else{
				onesArry[temp2]=arr[i];
				temp2++;
			}
			
		}

	}
	
static class LinkedList{
		
		Node node;
		Node head;
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
