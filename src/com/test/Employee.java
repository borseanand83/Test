package com.test;

class Person {
	String name = "No name";
	public Person(String nm) { name = nm; }
	public Person() {
		// TODO Auto-generated constructor stub
	}
}

public class Employee extends Person {
	String empID = "0000";
	public Employee(String id) { 
		empID = id; }

	public static void main(String[] args){
		Employee e = new Employee("4321");
		System.out.println(e.empID);
		parse("invalid");
		StringBuilder sb1 = new StringBuilder("123");
		String s1 = "123";
		sb1.append("abc"); s1 = s1.concat("abc");
		System.out.println(sb1 + " " + s1);
		double d = 12.345;
		System.out.printf("|%7.3f| \n", d);
		System.out.printf("|%3.7f| \n", d);
		//		System.out.printf("|%7d| \n", d);
		boolean assertsOn = true;
		//		assert (assertsOn) : assertsOn = true;
		if(assertsOn) {
			System.out.println("assert is on");
		}
		new Boxer1(new Integer(4));
	}



		public static void parse(String str) {
			float f=0;
			try {
				f = Float.parseFloat(str);
			} catch (NumberFormatException nfe) {
				f = 0;
			} finally {
				System.out.println(f);
			}
		}


	}

	class Super {
		private int a;
		protected Super(int a) { this.a = a; }
	}
	class Sub extends Super {
		public Sub(int a) { super(a); }
		public Sub() { this(5); }
	}
	class Boxer1{
		Integer i;
		int x;
		public Boxer1(int y) {
			x = i+y;
			System.out.println(x);
		}
	}