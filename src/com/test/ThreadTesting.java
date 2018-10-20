package com.test;

import java.lang.Thread.UncaughtExceptionHandler;

public class ThreadTesting {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Thread t1 = new Thread(new TestThread(),"T1");
//		t1.start();
////		try {
////		t1.join();
//		Thread t2 = new Thread(new TestThread(),"T2");
//		t2.start();
////		t2.join();
//		Thread t3 = new Thread(new TestThread(),"T3");
//		t3.start();
//		t3.join();
		Thread t4 = new Thread(new TestThread(),"T4");
		UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread thread, Throwable throwable) {
				System.out.println("handled thread exception");
			}
		};
		t4.setUncaughtExceptionHandler(handler);
		t4.start();

		
//		t4.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
