package com.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CSVFileComparison {

	
	public static void main(String[] args) {
		String searchString;
		File file = new File("/Users/aborse/Documents/Workarea/Suncorp/export.csv");
		File file2 = new File("/Users/aborse/Documents/IBM Watson NLC training set.csv");
		List<String> list1 = Readfile(file,1);
		List<String> list2 = Readfile(file2,2);
		System.out.println("*********************************************");
//		for (String sentence1 : list2) {
//			if(!list1.contains(sentence1))
//				System.out.println(sentence1);
//		}
	}

	private static List Readfile(File file,int index) {
		BufferedReader reader = null;
		List<String> exportList = new ArrayList<String>();
		try {
			reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file))));
			while(reader.readLine()!=null){
				String line = reader.readLine();
				exportList.add(ReadLines(line,index));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exportList;
	}

	private static String ReadLines(String line,int index) {
		System.out.println();
		
		if(line!=null)
		{	
			
			String ret = line.split(",")[index];
			System.out.println("Split line ------>>>"+ret);
			return ret;
			
				
		}
				return "";
	}
	
}
