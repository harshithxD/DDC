package com.example.harshith.ddc;

public class DynamicGesture extends Gesture {

	public int [][][] eFrame;

	public DynamicGesture(){
		eFrame = new int [sensors][][];
		for (int i=0; i<sensors; i++) {
			eFrame[i] = new int [dataPoints][];
			for (int j=0; j<dataPoints; j++) {
				eFrame[i][j] = new int[2];
			}
		}
	}

	public void printData(){
		for (int i=0; i<sensors; i++) {
			for (int j=0; j<dataPoints; j++) {
					System.out.print(eFrame[i][j][0]); System.out.print(":");
					System.out.print(eFrame[i][j][1] + " ");
			}
			System.out.print('\n');
		}
	}

	public void updateFrame(int [][][] sensorLimits){
		for (int i=0; i<sensors; i++) {
			for (int j=0; j<dataPoints; j++) {
				for (int k=0; k<2; k++) {
					eFrame[i][j][k] = sensorLimits[i][j][k];
				}
			}
		}
	}

	public boolean isInFrame(Live live, int index){for (int i=0; i<sensors; i++) {
			if(!((eFrame[i][index][0]<live.reading[i]) && (live.reading[i]<eFrame[i][index][1]))){
				return false;
			} 
		}
		return true;
	}

}