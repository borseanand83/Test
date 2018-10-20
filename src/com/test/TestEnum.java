package com.test;

public enum TestEnum {

	INSTANCE;
	
	private Object readResolve(){
        return INSTANCE;
    }
}
