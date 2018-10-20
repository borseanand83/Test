package com.test;

import java.util.Vector;

public class Producer implements Runnable {

	private final Vector<Integer> shq;
	private final int size;
	
	public Producer(Vector<Integer> shq, int size) {
		this.shq = shq;
		this.size = size;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < 7; i++) {
			try {
				produce(i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public synchronized void produce(int i) throws InterruptedException{
		
		if(shq.size() == size){
			shq.wait();
		}
		
		shq.add(i);
		shq.notify();
	}

}
