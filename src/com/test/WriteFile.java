package com.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile implements Runnable {

	File file;
	String contents;
	public WriteFile(File file, String contents) {
		this.file = file;
		this.contents = contents;
	}
	@Override
	public void run() {
		System.out.println("In Write thread  :: "+Thread.currentThread().getName());
		FileWriter fileWriter =null;
		try {
			
			synchronized (file) {
				fileWriter = new FileWriter(file);
				fileWriter.write(contents);
				fileWriter.close();
//				file.notify();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//			catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
