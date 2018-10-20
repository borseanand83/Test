package com.test;

import java.util.Arrays;

public class MyMap<K, V> {
	  private int size;
	  private int DEFAULT_CAPACITY = 16;
	  private MyEntry<K, V>[] values =   new MyEntry[DEFAULT_CAPACITY];

	  public synchronized V  get(K key){
		  for (int i = 0; i < values.length; i++) {
			if(values[i].getKey().equals(key)){
				return values[i].getValue();
			}
		}
		return null;
	  }
	  
	  public void put(K key,V value){
		  boolean insert = false;
		  for (int i = 0; i < values.length; i++) {
			if(values[i].getKey().equals(key)){
				synchronized (values) {
					values[i].setValue(value);
					insert = true;
				}
				
			}
		}
		if(!insert){
			ensureCapa();
			synchronized (values) {
				values[size++] = new MyEntry<K, V>(key, value);
			}
		}
	  }
	  
	  private void ensureCapa() {
		    if (size == values.length) {
		      int newSize = values.length * 2;
		      values = Arrays.copyOf(values, newSize);
		    }
	  }
	  
	  public void remove(K key){
		  for (int i = 0; i < values.length; i++) {
			if(values[i].getKey().equals(key)){
				synchronized (values) {
					values[i] = null;	
				}
			}
		  }
	  }
}
