package com.example.harshith.ddc;


public class Gesture{

	public int dataPoints = 50;
	public int sensors = 10;
	public int adcLevels = 2;
	public int adcUpper = 0;
	public int adcLower = 100;
	public int mpuLevels = 3;
	public int mpuUpper = 18000;
	public int mpuLower = -18000;

	public Gesture(){}

	public void printData(){
		L.m("Printing data...");
	}

	public void updateFrame(int [][] sensorLimits){
		L.m("Updating static frame...");
	}

	public void updateFrame(int [][][] sensorLimits){
		L.m("Updating dynamic frame...");
	}

	public void updateFrame(int []fing){
		L.m("Updating frame for real...");
	}

	public boolean isInFrame(Live live){
		L.m("Checking if the value is in the static frame...");
		return true;
	}

	public boolean isInFrame(Live live, int index){
		L.m("Checking if the value is in the dynamic frame...");
		return true;
	}

	public void execute(int gest){
		// perform system function
		L.m("" + gest);
	}
}