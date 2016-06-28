package com.example.harshith.ddc;

class DynamicQueue{
	public int noOfSlots = 7;
	public int noOfGestures;
	public Gesture [] gesture;
	public int [][] dtwGap;
	public Live []liveElement;
	public boolean [][]shortlist;

	public int latestElement = 0;
	public int foremostElement = 0;

	public DynamicQueue(Gesture []gest){
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

	public int[][] liveTempArrayComp(int slotNo, int livesTailNo){
		int count = 0;
		for (int i=slotNo; i!=livesTailNo; i=(i+1)%noOfSlots){count++;}
		int[][] x = new int[count][liveElement[0].sensors];

		for (int i=0; i<count; i++){
		// for (int i=slotNo; i!=livesTailNo; i=(i+1)%noOfSlots){
			x[i] = liveElement[(i+slotNo)%noOfSlots].reading;
		}
		
		return x;
	}

	public void processLive(int slotNo, int livesTailNo){
		int threshold = 5;
		
		DTW x;	
		int[][] arraytemp = liveTempArrayComp(slotNo,livesTailNo);

		for (int i=0; i<noOfGestures; i++) {
			x = new DTWtwoD();
			x.arrayInput(gesture[i].getPoint(), arraytemp);
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

	public void proceedExecution(){
		for (int i=foremostElement; i!=latestElement; i=(i+1)%noOfSlots) {
			if(getShortlistCount(i)==0) foremostElement = (foremostElement+1)%noOfSlots;
			else if(getShortlistCount(i)==1) {
				//gesture execute
				gesture[firstShortlistGestureIndex(foremostElement)].execute(firstShortlistGestureIndex(foremostElement));
				foremostElement = (foremostElement+1)%noOfSlots;
			}
			else break;
		}
		overflowCheck();
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
		System.out.println("Gesture Status:");
		for (int i=0; i<noOfGestures; i++) {
			for (int j=0; j<noOfSlots; j++) {

				int d=0;
				if(shortlist[j][i]) d=1;
				System.out.print(d + " ");
			}
			System.out.print('\n');			
		}
		System.out.println(foremostElement + " " + latestElement);		
	}
}