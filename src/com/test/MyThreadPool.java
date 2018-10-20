package com.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MyThreadPool {

	private BlockingQueue<Runnable> queue = null;
	private int nThreads;
	private WorkerThread[] threads;
	private boolean isStopped = false;
	
	public MyThreadPool(int nThreads) {
		this.nThreads = nThreads;
		threads = new WorkerThread[nThreads];
		queue = new LinkedBlockingQueue<Runnable>();
		for (int i = 0; i < threads.length; i++) {
			Thread t = threads[i];
			t = new WorkerThread();
			t.start();
		}
	}
	
	public void execute(Runnable task){
		if(this.isStopped)
			throw new IllegalStateException("Thread stopped");
		queue.add(task);
	}
	
	public void shutDown(){
		isStopped = true;
		for (WorkerThread t : threads) {
			t.doStop();
		}
	}
	
	private class WorkerThread extends Thread{

		private boolean       isStopped = false;
		@Override
		public void run() {
			while(!isStopped){
				try {
					Runnable task = queue.take();
					task.run();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
		public synchronized void doStop(){
			isStopped = true;
			this.interrupt();
		}
		
		public synchronized boolean isStopped(){
			return isStopped;
		}
	}
}
