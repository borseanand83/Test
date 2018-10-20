package com.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Processor implements Runnable {
private CountDownLatch latch;

public Processor(CountDownLatch latch) {
    this.latch = latch;
}

public void run() {
    System.out.println("Started.");
    latch.countDown();   
}
}

public class App {

    public static void main(String[] args) {

    CountDownLatch latch = new CountDownLatch(2); // coundown from 3 to 0

    ExecutorService executor = Executors.newFixedThreadPool(3); // 3 Threads in pool

    for(int i=0; i < 3; i++) {
        executor.submit(new Processor(latch)); // ref to latch. each time call new Processes latch will count down by 1
    }


    try {
        latch.await();  // wait untill latch counted down to 0
        System.out.println("after await..");
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    executor.shutdown();
    // Wait until all threads are finish
    try {
		executor.awaitTermination(0,TimeUnit.NANOSECONDS);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    System.out.println("Completed.");
    }

}