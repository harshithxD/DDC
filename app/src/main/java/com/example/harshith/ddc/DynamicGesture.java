package com.example.harshith.ddc;

class DynamicGesture extends Gesture {

	public int dataPoints = 5;
	public int [][] point;
	public boolean [] consider;

	public DynamicGesture(){
		point = new int [dataPoints][sensors];
		consider = new boolean[sensors];
		/*for (int i=0; i<sensors; i++) {
			consider[i] = true;
		}*/
	}

	public int[][] getPoint(){
		return point;
	}

	public void printData(){
		System.out.print('\n');
		for (int j=0; j<sensors; j++) {
			int prnt = 0;
			if(consider[j]) prnt = 1;
			System.out.print(prnt + " ");
		}
		System.out.print('\n');
		for (int i=0; i<dataPoints; i++) {
			for (int j=0; j<sensors; j++) {
					System.out.print(point[i][j] + " ");
			}
			System.out.print('\n');
		}
	}

	public void updateFrame(int [][] sensorPoint, int[] considerPoints){
		for (int i=0; i<dataPoints; i++) {
			for (int j=0; j<sensors; j++) {
				point[i][j] = sensorPoint[i][j];
			}
		}

		updateConsider(considerPoints);
	}

	public void updateConsider(int [] array){
		
		for (int i=0; i<array.length; i++) {
			if(array[i]==1) consider[i] = true;
			//else consider[i] = false;
		}

	}

	public int getConsiderCount(){
		int count = 0;
		for (int i=0; i<consider.length; i++) {
			if(consider[i]) count++;
		}

		return count;
	}

}