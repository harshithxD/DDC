package com.example.harshith.ddc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.*;

class DynamicQueue{
	//public int noOfSlots = 2000;
	public int noOfGestures;
	public DynamicGesture [] gesture;
	public LinkedList <ArrayList [] > dtwGap;
	public LinkedList <Live> liveElement;
	public LinkedList <Boolean [] > shortlist;

	public int[] threshold;
	public double alterFactor = -1;

	public DynamicQueue(DynamicGesture []gest, int[] threshold){
		gesture = gest.clone();
		noOfGestures = gesture.length;
		this.threshold = threshold.clone();

		liveElement = new LinkedList <Live> ();

		shortlist = new LinkedList <Boolean []> ();

		/*for (int i=0; i<noOfSlots; i++) {
			for (int j=0; j<noOfGestures; j++) {
				shortlist[i][j] = true;
			}
		}*/

		dtwGap = new LinkedList <ArrayList []>();

		/*for (int i=0; i<noOfSlots; i++) {
			for(int j=0; j<noOfGestures; j++){
				dtwGap[i][j] = new ArrayList();
			}
		}*/
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

		liveElement.add(live);
		//[latestElement] = new Live(live);
		Boolean [] trueBool = new Boolean[noOfGestures];
		for (int i=0; i<noOfGestures; i++) {
			trueBool[i] = true;
		}
		shortlist.add(trueBool);

		//overflowCheck();
	}

	public int getShortlistCount(){
		int count = 0;
		for (int i=0; i<noOfGestures; i++) {
			if(shortlist.getFirst()[i]) count++;
		}
		return count;
	}

	public int firstShortlistGestureIndex(){
		int i;
		for (i=0; i<noOfGestures; i++) {
			if(shortlist.getFirst()[i]) break;
		}
		return i;
	}

	public boolean warpFit(int gestIndex, int slot, int tail){
		// thresholdupdate();
		return true;

	}

	public int[][] liveTempArrayComp(int slotNo, int livesTailNo, int gestNo){
		
		int count = livesTailNo - slotNo;
		
		int[][] x = new int[count][gesture[gestNo].getConsiderCount()];

		int index = 0;
		for (int i=0; i<liveElement.get(slotNo).sensors; i++){
			if(gesture[gestNo].consider[i]){
				for (int j=0; j<count; j++) {
					x[j][index] = liveElement.get(j+slotNo).reading[i]; //gesture[gestNo].point[j][i];
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
		DTW x;

		for (int i=0; i<noOfGestures; i++) {
			x = new DTWtwoD();
			
			int[][] arraytemp = liveTempArrayComp(slotNo, livesTailNo, i);
			int[][] gestarraytemp = validSensorArrayComp(i);
			
			x.arrayInput(gestarraytemp, arraytemp);
			dtwGap.get(slotNo)[i].add( Math.max( 0.0, ((double)(x.sdtwDistance() + dtwGap.get(slotNo)[i].size()*alterFactor)) ) );
		}
		for (int i=0; i<noOfGestures; i++) {
			if( (new Double((double)dtwGap.get(slotNo)[i].get(dtwGap.get(slotNo)[i].size()-1)).intValue() ) >=threshold[i]) shortlist.get(slotNo)[i] = false;
		}

	}

	public void processQueue(){
//        int no;
//        if(latestElement > foremostElement){
//            no = Math.max(foremostElement,latestElement-20)%noOfSlots;
//        }
//        else {
//            no = (latestElement - 20)%noOfSlots;
//        }
		for (int i=0; i < liveElement.size(); i++) {
			processLive(i,liveElement.size());
		}
	}

	public int proceedExecution(){
        int stopScan = 20;
//        no = Math.max((latestElement - 20)%noOfSlots,foremostElement);
        for (int i=0; i<Math.max(0,liveElement.size() - stopScan); i++ ){
			if(getShortlistCount()==0){
				liveElement.removeFirst();
				shortlist.removeFirst();
				dtwGap.removeFirst();
			}
			else if(getShortlistCount()==1) {
                //gesture execute
				System.out.println();
                return firstShortlistGestureIndex();
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
		for (int i=0; i<liveElement.get(0).sensors; i++) {
			for (int j=0; j<liveElement.size(); j++) {
				System.out.print(liveElement.get(j).reading[i] + " ");
			}
			System.out.print('\n');			
		}

		gestureStatusPrint();
	}

	public void gestureStatusPrint(){
		System.out.print('\n');
		System.out.println("DTW Status:");
		for (int i=0; i<noOfGestures; i++) {
			for (int j=0; j<dtwGap.size(); j++) {
				System.out.print(dtwGap.get(j)[i] + " ");
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