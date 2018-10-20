package com.test;

import java.util.ArrayList;
//IMPORT LIBRARY PACKAGES NEEDED BY YOUR PROGRAM
import java.util.List;
//SOME CLASSES WITHIN A PACKAGE MAY BE RESTRICTED
//DEFINE ANY CLASS AND METHOD NEEDED
//CLASS BEGINS, THIS CLASS IS REQUIRED
public class Solution
{        
// METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
	 public List<Integer> cellCompete(int[] states, int days)
	 {
	     // WRITE YOUR CODE HERE
		 List<Integer> list = new ArrayList<Integer>();
	     int[] outputArr = new int[states.length];
	     int i = 0;
	     while(i< days){
	    	 outputArr = new int[states.length];
	         for(int j=0;j<states.length;j++){
	             if(j==0){
	                 if(states[j+1]==0){
	                     outputArr[j]=0;
	                 }
	                 else {
	                	 outputArr[j]=1;
	                 }
	             }
	             else if(j == states.length - 1){
	                if(states[j-1]==0){
	                    
	                 outputArr[j]=0;
	                }
	                else {
	               	 outputArr[j]=1;
	                }
	             }
	             else{
	                if(states[j-1]==states[j+1]){
	                     outputArr[j]=0;
	                }
	                else {
	               	 outputArr[j]=1;
	                }
	             }
	         }
	         states = outputArr;
	         String arr= "";
	         for (int val : states) {
	           arr+=val+" ";   
	         	 
		    }
		    System.out.println(arr);
	         i++;
	     }
	     for (int k : outputArr) {
	    // 	 System.out.println(k);
	    	 list.add(k);
		}
	     
	     return list;
	 }
 
// METHOD SIGNATURE ENDS

 public static void main (String[] args) {
     Solution sol = new Solution();
//      int[] input = {1,0,0,0,0,1,0,0};
     int[] input = {1,1,1,0,1,1,1,1};
//      System.out.println(1^1);
      sol.cellCompete(input, 2);
  }
}
