package com.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntervalTest {

	
	static int complexity = 0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Interval> inputList = new ArrayList<Interval>();
		Interval obj1 = new Interval();
		obj1.start = 26;
		obj1.end = 28;
		inputList.add(obj1);
		Interval obj2 = new Interval();
		obj2.start = 3;
		obj2.end = 7;
		inputList.add(obj2);
		Interval obj3 = new Interval();
		obj3.start = 5;
		obj3.end = 11;
		inputList.add(obj3);
		Interval obj4 = new Interval();
		obj4.start = 2;
		obj4.end = 4;
		inputList.add(obj4);
		Interval obj5 = new Interval();
		obj5.start = 20;
		obj5.end = 27;
		inputList.add(obj5);
		Interval obj6 = new Interval();
		obj6.start = 31;
		obj6.end = 35;
		inputList.add(obj6);
		
		for (Interval interval : inputList) {
			interval.print();
		}

		checkOverlap(inputList);
		System.out.println("*******************************************************************");
		for (Interval interval : inputList) {
			interval.print();
		}
	}

	
	public static void checkOverlap(List<Interval> inputList){
		
		if(inputList.size() == 1)
			return;
		Collections.sort(inputList);
		for(int i=0,j=i+1;j<=i+1 && j < inputList.size();i++,j++){
			System.out.println(complexity++);
			Interval obj1 = inputList.get(i);
			Interval obj2 = inputList.get(j);
			if(obj1.end > obj2.start){
				Interval newObj = new Interval();
				newObj.start = obj1.start;
				newObj.end = obj2.end;
				inputList.remove(obj1);
				inputList.remove(obj2);
				inputList.add(0,newObj);
				checkOverlap(inputList);
			}
			else{
				return;
			}
		}
	}
}
