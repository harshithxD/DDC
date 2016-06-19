package com.example.harshith.ddc;


public class Frame{

	boolean mpu;
	public int upperBound;
	public int lowerBound;

	public Frame(int index, boolean isMpu, int upBound, int lrBound, int levels){
		if(!isMpu){

			upperBound =  upBound + index*((lrBound-upBound)/levels);
			lowerBound =  upBound + (index+1)*((lrBound-upBound)/levels);

		}
		else{

			upperBound =  lrBound - (index+2)*((lrBound-upBound)/levels);
			lowerBound =  lrBound - (index+1)*((lrBound-upBound)/levels);

		}

		if(index == 8){
			upperBound = upBound;
			lowerBound = lrBound;
		}
		
	}

	public void pushLimits(int []d){
		d[0] = upperBound;
		d[1] = lowerBound;
	}

	public void print(){
		System.out.print(upperBound);
		System.out.print(' ');
		System.out.println(lowerBound);
	}
}