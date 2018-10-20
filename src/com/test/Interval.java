package com.test;

public class Interval implements Comparable<Interval> {

	public int start;
	public int end;
	
	@Override
	public int compareTo(Interval o) {
		if(this.start < o.start){
			return -1;
		}
		else if(this.start > o.start){
			return 1;
		}
		return 0;
	}

	public void print(){
		System.out.println("["+start+","+end+"]");
	}
}
