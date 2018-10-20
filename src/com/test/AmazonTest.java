package com.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class AmazonTest {
	
	public static void main(String args[]) {
		String input = "you got beautiful eyes";
		eliminateDuplicateChars(input);
	}
	
	public static String eliminateDuplicateChars(String input) {
		char[] chararray = input.toCharArray();
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		Queue<Character> queue = new LinkedList<Character>();
		for (int i =0; i< chararray.length;i++) {
			queue.add(chararray[i]);
//			map.put(chararray[i], i);
		}
		Iterator<Character> it = map.keySet().iterator();
		String output = "";
		while(it.hasNext()) {
			output+=it.next();
		}
		
		System.out.println(output);
		return output;
	}

}
