package com.example.harshith.ddc;

class LagQueue{
	public int noOfSlots = 2000;
	public int noOfGestures;
	public DynamicGesture [] gesture;
	public int [] minDtwGap;
	public Live []liveElement;
	public boolean []shortlist;

	public int latestElement = -1;
	public int [] threshold;
	public int lowerBoundary = -200;
	public int upperBoundary = -50;
	
	public LagQueue(DynamicGesture []gest, int[] threshold){
		gesture = gest.clone();		
		noOfGestures = gesture.length;
		this.threshold = threshold.clone();

		liveElement = new Live[noOfSlots];

		shortlist = new boolean[noOfGestures];

		for (int j=0; j<noOfGestures; j++) {
			shortlist[j] = true;
		}

		minDtwGap = new int[noOfGestures];

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
		latestElement = (latestElement+1)%noOfSlots;
		
		liveElement[latestElement] = new Live(live);
		for (int i=0; i<noOfGestures; i++) {
			shortlist[i] = true;
		}

		overflowCheck();
	}

	public int getShortlistCount(){
		int count = 0;
		for (int i=0; i<noOfGestures; i++) {
			if(shortlist[i]) count++;
		}
		return count;
	}

	public int firstShortlistGestureIndex(){
		int i;
		for (i=0; i<noOfGestures; i++) {
			if(shortlist[i]) break;
		}
		return i;
	}

	public boolean warpFit(int gestIndex, int slot, int tail){
		// thresholdupdate();
		return true;

	}

	public int[][] liveTempArrayComp(int slotNo, int gestNo){
		
		int[][] x = new int[latestElement - (lowerBoundary + slotNo)][gesture[gestNo].getConsiderCount()];

		int index = 0;
		for (int i=0; i<liveElement[slotNo].sensors; i++){
			if(gesture[gestNo].consider[i]){
				for (int j = lowerBoundary + slotNo + latestElement; j < latestElement; j++) {
					x[j - (lowerBoundary + slotNo + latestElement)][index] = liveElement[j].reading[i]; //gesture[gestNo].point[j][i];
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

	public int arrayMin(int [] arr){
		int mini = 1000000;
		for (int i=0; i<arr.length; i++) {
			mini = Math.min(arr[i],mini);
		}
		return mini;
	}

	public int minDtwGapProcess(int gestNo){
		
		int [] dtwValues = new int[upperBoundary - lowerBoundary];

		DTW x;
		int [][] gestarraytemp = validSensorArrayComp(gestNo);

		for (int i = 0; i < upperBoundary - lowerBoundary; i++) {
			x = new DTWtwoD();
			
			int [][] arraytemp = liveTempArrayComp(i, gestNo);
			
			x.arrayInput(gestarraytemp, arraytemp);
			//dtwGap[slotNo][i].add( Math.max( 0.0, (x.sdtwDistance() + dtwGap[slotNo][i].size()*alterFactor)) );
			dtwValues[i] = x.dtwDistance();
		}

		return arrayMin(dtwValues);
	} 

	public void processQueue(){

		for (int i=0; i<noOfGestures; i++) {
			minDtwGap[i] = minDtwGapProcess(i);
		}
	}

	public int proceedExecution(){
		for (int i=0; i<noOfGestures; i++) {
			if(minDtwGap[i]<=threshold[i]) shortlist[i] = true;
			else shortlist[i] = false;
		}

		if(getShortlistCount()==0) latestElement = (latestElement+1)%noOfSlots;
		else if(getShortlistCount()==1) {
            //gesture execute
			System.out.println(firstShortlistGestureIndex());
            return firstShortlistGestureIndex();
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
            System.out.print("Gesture " + i + " : " + minDtwGap[i]);
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