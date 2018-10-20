package com.test;

public enum SingletonEnum {

	INSTANCE;
	
	private SingletonEnum() {
	}
	private final int id = 0;
	private final String name = null;
	private final double sal = 0;
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public double getSal() {
		return sal;
	}
	
}
