package com.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Singleton implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7920934897314016803L;
	private int id;
	private String name;
	private double sal;
	
	
	private Singleton(){}
	
	private static volatile transient Singleton instance =null;
	
	public static Singleton getInstance(){
		if(instance == null){
			synchronized(Singleton.class){
				if(instance == null){
					instance = new Singleton();
				}
			}
		}
		return instance;
	}

	protected Object readResolve(){
		return getInstance();
	}
	private static class SingletonHolder{
		private static final Singleton instance = new Singleton();
	}
	
	public static Singleton getInstancethruHolder(){
		return SingletonHolder.instance;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		Singleton instanceOne = Singleton.getInstancethruHolder();
		instanceOne.setId(1);
		instanceOne.setName("Anand");
		instanceOne.setSal(2000);
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(
                "filename.ser"));
        out.writeObject(instanceOne);
        out.close();
         
        //deserailize from file to object
        ObjectInput in = new ObjectInputStream(new FileInputStream(
                "filename.ser"));
        Singleton instanceTwo = (Singleton) in.readObject();
        in.close();
         
        System.out.println("instanceOne hashCode="+instanceOne.hashCode());
        System.out.println("instanceTwo hashCode="+instanceTwo.hashCode());
        System.out.println(instanceTwo.getId());
        System.out.println(instanceTwo.getName());
        System.out.println(instanceTwo.getSal());
        
        
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSal() {
		return sal;
	}

	public void setSal(double sal) {
		this.sal = sal;
	}

}

class Singleton2 {
	
	private Singleton2(){
		
	}
	private static final Singleton2 instance = new Singleton2();
	
	public static Singleton2 getInstance(){
		return instance;
	}
	
}

