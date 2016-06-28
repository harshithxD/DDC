package com.example.harshith.ddc;

class DynamicGesture extends Gesture {

	public int dataPoints = 5;
	public int [][] point;

	public DynamicGesture(){
		point = new int [dataPoints][sensors];
	}

	public int[][] getPoint(){
		return point;
	}

	public void printData(){
		System.out.print('\n');
		for (int i=0; i<dataPoints; i++) {
			for (int j=0; j<sensors; j++) {
					System.out.print(point[i][j] + " ");
			}
			System.out.print('\n');
		}
	}

	public void updateFrame(int [][] sensorPoint){
		for (int i=0; i<dataPoints; i++) {
			for (int j=0; j<sensors; j++) {
				point[i][j] = sensorPoint[i][j];
			}
		}
	}

}