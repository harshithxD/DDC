package com.example.harshith.ddc;

class DynamicPrime{
	public static void main(String [] args){

		int noOfGestures = 2;
		
		DynamicGesture []a = new DynamicGesture[noOfGestures];
		for (int i = 0; i<noOfGestures; i++) {
			a[i] = new DynamicGesture();
		}

		int [][]array = new int[5][11];
		int []cons = new int[11];
		for (int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				array[i][j] = i*2;
			}
		}
		//cons = new int[] {1,1,1,1,1,0,0,0,0,0,0};
		cons = new int[] {1,1,1,1,1,1,1,1,1,1,1};
		a[0].updateFrame(array,cons);
		a[0].printData();

		int [][]array2 = new int[5][11];
		int []cons2 = new int[11];
		for (int i=0; i<5; i++) {
			for (int j=0; j<11; j=j+2) {
				array2[i][j] = i*2;
			}
			for (int j=1; j<11; j=j+2) {
				array2[i][j] = 0;
			}
		}
		cons2 = new int[] {1,1,1,1,1,0,0,0,1,0,0};
		a[1].updateFrame(array2,cons2);
		a[1].printData();		

		// initialising the 10 gestures

		DynamicQueue q = new DynamicQueue(a);
		Live dynamiclive = new Live();

		//dynamiclive.update(100);
		q.foremostElement = 0;
		for (int i=0; i<5; i++) {
			dynamiclive.update(i*2);
			q.updateQueue(dynamiclive);
			q.processQueue();
			q.proceedExecution();
			q.print();			
		}
	}
}