package com.test;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class B  extends A{
	
public String a="abc";
	
	public static void print() throws ArithmeticException{
//		super.print();
		System.out.println("abc");
	}
	
	public static void main(String[] args) {
		List<String> strList = new ArrayList<String>();
		String a = "foo";
		String b = "food".substring(0, 3);
		String c = b.intern();

		if (a.equals(b)) {
		    if (a == b) {
		        System.out.println("1");
		    } else if (a == c) {
		        System.out.println("2");
		    } else {
		        System.out.println("3");
		    }
		} else {
		    System.out.println("4");
		}
		A a2 = new A();
		B b2 = new B();
		a2 = b2;
		A a1 = new B();
		a1.print();
		System.out.println(a1.a);
	}
	
	public void func1() throws SQLException{
		System.out.println("B func1");
	}

}
