package com.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class TestProgram {
	 static int i,j,k;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		System.out.println(calculateSeries(0, 1));
//		calculateSeries(0, 1);
//		System.out.println("fact:"+calculateFactorial(4));
//		checkWord("restate");
//		System.out.println("*******************"+(500 % 420));
		String a = "abc";
//		String b = "HYTBCABADEFGHABCDEDCBAGHTFYW12345678987654321ZWETYGDE";
		String b = "abcdef";
		shiftString(b, 2);
		atoi("1234");
//		b.toUpperCase();
//		System.out.println(b.toUpperCase());
//		System.out.println(a==b);
//		System.out.println(a.equals(b));
////		checkContainsPalindrome(b);
////		printStringPermutations("", b);
//		int[] arr = {1,4,3,2,5,10,11,6,6,7,8};
//		Hashtable<String, String> table = new Hashtable<String, String>();
//		findDuplicates(arr);
//		Sqaure s1 = new Sqaure();
//		Sqaure s2 = new Sqaure();
//		Sqaure s3 = new Sqaure();
//		Sqaure s4 = new Sqaure();
//		List<Sqaure> squares = new ArrayList<Sqaure>();
//		squares.add(s1);
//		squares.add(s2);
//		squares.add(s3);
//		squares.add(s4);
//		testGenerics(squares);
//		printFactors(15);
		printPattern3(3);
	}
	
	  static void printFactors(int a) {
	        int divisor=2;
	        while(a > 1){
	            if(a%divisor == 0){
	                System.out.print(divisor);
	                a = a / divisor;
	                if(a>1)
	                	System.out.print(",");
	            }
	            else
	                divisor++;
	        }

	    }
	
	  
	  static void printPattern(int N) {
		  	int matrixSize = N * N;
		  	int i = 1;
		  	while(i <= matrixSize){
		  		int rowCnt = 1;
		  		while(rowCnt <= N){
		  			System.out.print("0"+i);
		  			rowCnt++;
		  			i++;
		  			if(rowCnt <= N)
		  				System.out.print("-");
		  		}
		  		System.out.println();
		  	}

	    }

	  
	  static void printPattern3(int N) {
		  	int matrixSize = N * N;
		  	int[][] dArr = new int[N][N];
		  	int i = 1;
		  	int rowCnt = 0;
		  	int colCnt = N;
		  	while(i <= matrixSize){
		  		for(int j=rowCnt;j<colCnt;j++){
		  			dArr[rowCnt][j] = i;
		  			i++;
		  		}
		  		for(int j=rowCnt+1;j<colCnt;j++){
		  			dArr[j][colCnt-1] = i;
		  			i++;
		  		}
		  		for(int j=colCnt-1;j>rowCnt;j--){
		  			dArr[colCnt-1][j-1]=i;
		  			i++;
		  		}
		  		for(int j=colCnt-1;j>rowCnt+1;j--){
		  			dArr[j-1][rowCnt]=i;
		  			i++;
		  		}
		  		rowCnt++;
		  		colCnt--;
		  	}
		  	
             for(int k=0;k<N;k++)
             {
                 for(int j=0;j<N;j++)
                 {
                     System.out.print(+dArr[k][j]+" ");
                 }
                 System.out.println();
             }

	    }

	  
	  static void printPattern2(int a) {
	        int arr[][]=new int[a][a];

	                int h=a;//Horizontal

	                int v=0;//Vertical
	                int n=1;
	                while(n!=a)//h!=0 && v!=a
	                {
	                    for(j=0,k=1;j<a;j++,k++)
	                    {
	                        arr[0][j]=k;
	                        //System.out.println(k);
	                        //upto a-1
	                    }          
	                    for(i=1,k=k;i<=a-1;i++,k++)
	                    {
	                        arr[i][a-1]=k;//k=a+1 upto 2a-1
	                    }
	                    n=1;
	                    for(j=a-2,k=k;j>=0;j--,k++)
	                    {
	                        arr[a-1][j]=k;//k=2*a upto 3a-2
	                    }
	                    n=2;
	                    for(i=a-2,k=k;i>=1;i--,k++)
	                    {
	                        arr[i][0]=k;//k=3*a-1 upto 4a-4
	                    }
	                    for(j=1,k=k;j<a-2;j++,k++)
	                    {
	                    }
	                    for(i=1,k=k;i<a-2;i++,k++)
	                    {
	                        arr[i][a-2]=k;//k=5*a-5 upto 6a-8
	                    }
	                    for(j=a-2,k=k;j>1;j--,k++)
	                    {
	                        arr[a-2][j]=k;//k=6*a-7 upto 7a-3
	                    }
	                    n=3;
	                    for(i=a-2,k=k;i>1;i--,k++)
	                    {
	                        arr[i][1]=k;
	                    }
	                    for(j=2,k=k;j<a-2;j++,k++)
	                    {
	                        arr[2][j]=k;
	                    }
	                    for(i=2,k=k;i<=a-2;i++,k++)
	                    {
	                        arr[i][a-3]=k;
	                    }
	                    for(j=a-2,k=k;j>2;j--,k++)
	                    {
	                        arr[a-2][j]=k;
	                    }
	                    for(i=a-3,k=k;i>3;i--,k++)
	                    {
	                        arr[i][2]=k;
	                    }
	                    h--;v++;
	                }
	                //System.out.println(h);
	                //System.out.println(v);     
	                System.out.println("Array Display");
	                for(int i=0;i<a;i++)
	                {
	                    for(int j=0;j<a;j++)
	                    {
	                        System.out.print(+arr[i][j]+" ");
	                    }
	                    System.out.println();
	                }
	    }
	public static void testGenerics(List<? extends Shape> shapes){
//		Sqaure obj1 = new Sqaure();
//		shapes.add(obj1);
		for (Shape shape : shapes) {
			shape.calculateArea();
		}
	}

	
	public static void calculateSeries(int n1,int n2){
		int result = n1 + n2;
		System.out.println("result : "+result);
		if(result < 20){
			calculateSeries(n2, result);
		}
	}
	
	public static int calculateFactorial(int n){
		System.out.println("n : "+n);
		if(n<=1)
			return n;
		else{
			n = n * calculateFactorial(n-1);
			return n;
		}
	}
	
	public static void checkWord(String word){
		String newWord = word.substring(0,word.length() -1);
		if(newWord.length()==0){
			System.out.println("Word ends");
		}
		else{
			System.out.println("Word :"+newWord);
			checkWord(newWord);
		}
		
	}
	
	public static void checkContainsPalindrome(String word){
		char[] arr = word.toCharArray();
		int cntPal = 0;
		Integer compl = 0;
		for (int i = 0; i < arr.length ; i++) {
			compl++;
			for (int j = i+1; j < arr.length; j++) {
				compl++;
				if(arr[i] == arr[j]){
					if(checkPalindrome(word.substring(i,j+1),compl))
						cntPal++;
				}
				System.out.println("checkpal ******complexitiy : "+compl);
			}
			
		}
		System.out.println("number of palindromes : "+cntPal);
		System.out.println("containspal complexity : "+compl);
	}
	
	public static boolean checkPalindrome(String word,Integer compl){
		char[] arr = word.toCharArray();
		System.out.println("word is "+word);
		boolean ispal = true;
		for (int i = 0,j=arr.length-1; j>= (arr.length)/2; i++,j--) {
			compl++;
			if(arr[i] == arr[j])
				continue;
			else{
				ispal = false;
				break;
			}
				
				
		}
		if(ispal)
			System.out.println("is palindrome");
		System.out.println("checkpal complexitiy : "+compl);
		return ispal;
	}
	
	public static void printStringPermutations(String prefix,String word){
		int n = word.length();
		System.out.println(fact((word.length())));
	    if (n == 0) System.out.println(prefix);
	    else {
	        for (int i = 0; i < n; i++)
	        	printStringPermutations(prefix + word.charAt(i), word.substring(0, i) + word.substring(i+1, n));
	    }
	}
	
	private static int fact(int n)
	{
		if(n <=1) return 1;
		else{
			n = n * fact(n-1);
			return n;
		}
	}
	
	public static void findDuplicates(int[] arr){
		int n = arr.length;
		int origSum = (n * (n+1))/2;
		System.out.println("orig sum : "+origSum);
		int actSum = 0;
		for (int i = 0; i < arr.length; i++) {
			actSum+=arr[i];
		}
		System.out.println("act sum : "+actSum);
		System.out.println(origSum - actSum);
//		for (int i = 0; i < arr.length; i++) {
//			for(int j=i+1;j<arr.length;j++){
//				if(arr[i] == arr[j])
//					System.out.println("duplicate is : "+arr[i]);
//				
//			}
//		}
		System.out.println(arr);
		int duplicate =0;
		Map<Integer, Integer> duplicateresolver = new HashMap<Integer, Integer>();
		for (int i = 0; i < arr.length; i++) {
			if(!duplicateresolver.containsKey(arr[i]))
					duplicateresolver.put(arr[i], i);
			else{
				duplicate = arr[i];
				System.out.println("Got the duplicate element  :: "+arr[i]);
			}
		}
		int diffSum = actSum - duplicate;
		System.out.println("Missing Element : "+(origSum - diffSum));
	
	}
	
	public static void shiftString(String str,int n ){
		char[] arr = str.toCharArray();
		int position = arr.length - n ;
		for (int i = 0; i < (arr.length); i++) {
			if(position >= arr.length) break;
			char temp = arr[i];
			arr[i] = arr[(position)];
			arr[(position)] = temp;
			position++;
		}
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
	
	}
	
	 public static int atoi( String input ){
	        int i = 0, number = 0;
	        boolean isNegative = false;
	        int len = input.length();
	        if( input.charAt(0) == '-' ){
	            isNegative = true;
	            i = 1;
	        }
	        while( i < len ){
	            number *= 10;
	            number += ( input.charAt(i++) - '0' );
	        }
	        if( isNegative )
	        number = -number;
	        System.out.println(number);
	        return number;
	    }
	 
	
}
