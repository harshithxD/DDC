package com.example.harshith.ddc;

public class StaticGesture extends Gesture {

	public int [][] eFrame;

	public StaticGesture(){
		eFrame = new int [sensors] [];
		for (int i=0; i<sensors; i++) {
			eFrame[i] = new int [2];
		}
	}

	public void printData(){
		
		for (int i=0; i<5; i++) {
			String text = String.format("%03d", eFrame[i][0]);
			L.m(text);
		}
		for (int i=5; i<sensors; i++) {
			if(eFrame[i][1]<0) System.out.print(" ");
			String text = String.format("%05d", eFrame[i][0]);
			L.m(text);
		}

		for (int i=0; i<5; i++) {
			String text = String.format("%03d", eFrame[i][1]);
			L.m(text);
		}
		for (int i=5; i<sensors; i++) {
			String text = String.format("%05d", eFrame[i][1]);
			L.m(text);
		}
	}

	public void updateFrame(int [][] sensorLimits){
		//super.updateFrame(sensorLimits);
//		for (int i=0; i<sensors; i++) {
//			for (int j=0; j<2; j++) {
//				eFrame[i][j] = sensorLimits[i][j];
//			}
//		}
		eFrame = sensorLimits.clone();
	}

	public void updateFrame(int []hand){

		int [][]data = new int [sensors][2];
		
		Frame []f = new Frame[sensors];

		for (int i=0; i<4; i++) {
			f[i] = new Frame(hand[i], false, adcUpper, adcLower, 2);
		}
		for (int i=4; i<sensors; i++) {
			f[i] = new Frame(hand[i], true, mpuUpper, mpuLower, 3);
		}

		for (int i=0; i<sensors; i++) {
			f[i].pushLimits(data[i]);
		}

		this.updateFrame(data);
	}

	public boolean isInFrame(Live live){
		for (int i=0; i<sensors; i++) {
			if(!((eFrame[i][0]<live.reading[i]) && (live.reading[i]<eFrame[i][1]))){
				return false;
			} 
		}
		return true;
	}
}