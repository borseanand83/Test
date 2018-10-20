package com.test;

public class FibonacciSeries {

	public static void main(String[] args) {
		System.out.println(fibRecurssive(5));
	}

	
	public static int fibonacci(int n){
		int sum=0;
		int f1 =1,f2 =2;
		sum = f1+f2;
		for(int i=sum;i<=n;i++){
			f2=i;
			f1=sum;
			sum = f1+f2;
		}
		return sum;
	}
	
	public static int fibRecurssive(int n) {
		if(n==1)
			return n;
		else
		{
			return n * fibRecurssive(n-1);

		}
	}
}
