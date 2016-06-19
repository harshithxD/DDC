package com.example.harshith.ddc;


public class Gesture{

	public int dataPoints = 15;
	public int sensors = 11;
	public int adcLevels = 2;
	public int adcUpper = 60;
	public int adcLower = 200;
	public int mpuLevels = 3;
	public int mpuUpper = 18000;
	public int mpuLower = -18000;

	public Gesture(){}

	public void printData(){
		System.out.println("Printing data...");
	}

	public void updateFrame(int [][] sensorLimits){
		System.out.println("Updating static frame...");
	}

	public void updateFrame(int [][][] sensorLimits){
		System.out.println("Updating dynamic frame...");
	}

	public void updateFrame(int []fing){
		System.out.println("Updating frame for real...");
	}

	public boolean isInFrame(Live live){
		System.out.println("Checking if the value is in the static frame...");
		return true;
	}

	public boolean isInFrame(Live live, int index){
		System.out.println("Checking if the value is in the dynamic frame...");
		return true;
	}

	public void execute(int gest){
		// perform system function
		System.out.println(gest);
	}
}