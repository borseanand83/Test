package com.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TestThread extends Thread implements Runnable {

	@Override
	public void run() {
		System.out.println("I m in runnable"+Thread.currentThread().getName());
		throw new RuntimeException("thread "+currentThread().getName()+" is throwing exception");
//			test();
	}

	
	public synchronized void test() throws Exception{
//		wait();
		System.out.println("in synchronized emthod test"+Thread.currentThread().getName());
		throw new RuntimeException("thread "+currentThread().getName()+" is throwing exception");
//		notify();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<String> a = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		Set<Entry<String, String>> entry = map.entrySet();	
		
		Iterator<Entry<String, String>> it = entry.iterator();
		while(it.hasNext()){
			Entry<String, String> entryObj = it.next();
			entryObj.getKey();
			entryObj.getValue();
		}
		
	}

}
