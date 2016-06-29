package com.example.harshith.ddc;

class DynamicQueue{
	public int noOfSlots = 2000;
	public int noOfGestures;
	public DynamicGesture [] gesture;
	public int [][] dtwGap;
	public Live []liveElement;
	public boolean [][]shortlist;

	public int latestElement = 0;
	public int foremostElement = 0;

	public DynamicQueue(DynamicGesture []gest){
		gesture = gest.clone();
		noOfGestures = gesture.length;

		liveElement = new Live[noOfSlots];
		for (int i=0; i<noOfSlots; i++) {
			liveElement[i] = new Live();
		}

		shortlist = new boolean[noOfSlots][noOfGestures];

		for (int i=0; i<noOfSlots; i++) {
			for (int j=0; j<noOfGestures; j++) {
				shortlist[i][j] = true;
			}
		}

		dtwGap = new int[noOfSlots][noOfGestures];
	}

	public void overflowCheck(){
/*		if(latestElement == foremostElement){
			System.out.print("\n");
			System.out.println("#########################");
			System.out.println("#### Queue Overflow! ####");
			System.out.println("##### Bogus follows #####");
			System.out.println("#########################");
		}*/
	}

	public void updateQueue(Live live){
		liveElement[latestElement] = new Live(live);
		for (int i=0; i<noOfGestures; i++) {
			shortlist[latestElement][i] = true;
		}
		latestElement = (latestElement+1)%noOfSlots;

		overflowCheck();
	}

	public int getShortlistCount(int slotNo){
		int count = 0;
		for (int i=0; i<noOfGestures; i++) {
			if(shortlist[slotNo][i]) count++;
		}
		return count;
	}

	public int firstShortlistGestureIndex(int slotNo){
		int i;
		for (i=0; i<noOfGestures; i++) {
			if(shortlist[slotNo][i]) break;
		}
		return i;
	}

	public boolean warpFit(int gestIndex, int slot, int tail){
		// thresholdupdate();
		return true;

	}

	public int[][] liveTempArrayComp(int slotNo, int livesTailNo, int gestNo){
		
		int count = 0;
		for (int i=slotNo; i!=livesTailNo; i=(i+1)%noOfSlots){count++;}
		
		int[][] x = new int[count][gesture[gestNo].getConsiderCount()];

		int index = 0;
		for (int i=0; i<liveElement[slotNo].sensors; i++){
			if(gesture[gestNo].consider[i]){
				for (int j=0; j<count; j++) {
					x[j][index] = liveElement[(j+slotNo)%noOfSlots].reading[i]; //gesture[gestNo].point[j][i];
				}
				index++;
			}
		}		
		return x;
	}

	public int[][] validSensorArrayComp(int gestNo){

		int[][] x = new int[gesture[gestNo].dataPoints][gesture[gestNo].getConsiderCount()];

		int index = 0;
		for (int i=0; i<gesture[gestNo].sensors; i++){
			if(gesture[gestNo].consider[i]){
				for (int j=0; j<gesture[gestNo].dataPoints; j++) {
					x[j][index] = gesture[gestNo].point[j][i];
				}
				index++;
			}
		}
		
		return x;
	}	

	public void processLive(int slotNo, int livesTailNo){
		int threshold = 30;
		
		DTW x;

		for (int i=0; i<noOfGestures; i++) {
			x = new DTWtwoD();
			
			int[][] arraytemp = liveTempArrayComp(slotNo, livesTailNo, i);
			int[][] gestarraytemp = validSensorArrayComp(i);
			
			x.arrayInput(gestarraytemp, arraytemp);
			dtwGap[slotNo][i] = x.sdtwDistance();
		}
		for (int i=0; i<noOfGestures; i++) {
			if(dtwGap[slotNo][i]>=threshold) shortlist[slotNo][i] = false;
		}

	}

	public void processQueue(){
		for (int i=foremostElement; i!=latestElement; i=(i+1)%noOfSlots) {
			processLive(i,latestElement);
		}
	}

	public int proceedExecution(){
		for (int i=foremostElement; i!=latestElement; i=(i+1)%noOfSlots) {
			if(getShortlistCount(i)==0) foremostElement = (foremostElement+1)%noOfSlots;
			else if(getShortlistCount(i)==1) {
                //gesture execute
				System.out.println(foremostElement);
                return firstShortlistGestureIndex(foremostElement);
//				foremostElement = (foremostElement+1)%noOfSlots;
			}
			else break;
		}
		overflowCheck();
        return -1;
	}

	public void print(){
		System.out.print('\n');
		System.out.println("Live Elements:");
		for (int i=0; i<liveElement[0].sensors; i++) {
			for (int j=0; j<noOfSlots; j++) {
				System.out.print(liveElement[j].reading[i] + " ");
			}
			System.out.print('\n');			
		}

		gestureStatusPrint();
	}

	public void gestureStatusPrint(){
		System.out.print('\n');
		System.out.println("DTW Status:");
		for (int i=0; i<noOfGestures; i++) {
			for (int j=0; j<noOfSlots; j++) {
				System.out.print(dtwGap[j][i] + " ");
			}
			System.out.print('\n');			
		}
//		System.out.println("Gesture Status:");
//		for (int i=0; i<noOfGestures; i++) {
//			for (int j=0; j<noOfSlots; j++) {
//
//				int d=0;
//				if(shortlist[j][i]) d=1;
//				System.out.print(d + " ");
//			}
//			System.out.print('\n');
//		}
//		System.out.println(foremostElement + " " + latestElement);
	}
}