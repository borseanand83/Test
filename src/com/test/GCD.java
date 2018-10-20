package com.test;

import java.util.Arrays;

//IMPORT LIBRARY PACKAGES NEEDED BY YOUR PROGRAM
//SOME CLASSES WITHIN A PACKAGE MAY BE RESTRICTED
//DEFINE ANY CLASS AND METHOD NEEDED
//CLASS BEGINS, THIS CLASS IS REQUIRED
class GCD
{
 // METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
 public int generalizedGCD(int num, int[] arr)
 {
     // WRITE YOUR CODE HERE
	 Arrays.sort(arr);
     int k = 1;
     boolean divisor = true;
     int gcd = k;
     while(k < arr[num-1]){
		  divisor = true;
        for (int i=0;i<arr.length ;i++ ) {
           if(arr[i]%k!=0){
             divisor = false;
             break;
           }
        }
 	   if(divisor) {
		   System.out.println("***************"+k);
		   if(k > gcd)
			   gcd = k;
	   }
        k++;
     }

        System.out.println(gcd);
     return gcd;
 }
 
 public static void main (String[] args) {
     GCD gcd = new GCD();
     int[] input = {3,5,7,11,13,17};
     gcd.generalizedGCD(5,input);
 }
 // METHOD SIGNATURE ENDS
}