package com.example.harshith.ddc;

import java.util.Scanner;

class Prime{
	public static void main(String [] args){
		
		Gesture []a = new Gesture[8];
		for (int i = 0; i<8; i++) {
			a[i] = new StaticGesture();
		}

		int []hand = {0,0,0,0,0,0,0,0,0,0,0}; int q=0;

		q=0; hand = new int [] {0,0,0,0,0,8,8,8,8,8,8};
		// neutral
		a[q].updateFrame(hand);

		q=1; hand = new int [] {1,1,0,0,0,8,8,8,8,1,8};
		// voice search
		a[q].updateFrame(hand);

		q=2; hand = new int [] {1,0,1,1,1,8,8,8,8,1,8};
		// tap
		a[q].updateFrame(hand);

		q=3; hand = new int [] {0,1,1,1,0,8,8,8,8,1,8};
		// call
		a[q].updateFrame(hand);

		q=4; hand = new int [] {0,0,1,1,1,8,8,8,-1,8,8};
		// cam open
		a[q].updateFrame(hand);

		q=5; hand = new int [] {0,0,0,1,1,8,8,8,-1,8,8};
		// cam click
		a[q].updateFrame(hand);

		q=6; hand = new int [] {1,0,1,1,0,8,8,8,8,1,8};
		// music open
		a[q].updateFrame(hand);

		q=7; hand = new int [] {0,0,1,1,0,8,8,8,8,1,8};
		// music play
		a[q].updateFrame(hand);

		for (int i=0; i<2; i++) {
			System.out.println(i);
			a[i].printData();
		}

		Live staticLive = new Live();
		int no = 2;
		//StaticLive.print();
		int count = 0;
		int gestActive = -1; boolean stay = true;
				
		while(stay){

			if(count == no){
				a[gestActive].execute(gestActive);
				count = 0;
				gestActive = -1;
				System.out.print("Continue? ");
				Scanner contd = new Scanner(System.in);
				String str = contd.next();
				//if( str.equals("n") ) { stay = false; break; }
			}

			if(count<no){
				staticLive.updateConsole();
			}

			for (int i=0; i<8; i++) {
				if(a[i].isInFrame(staticLive)){
					if(gestActive == i){
						count++; continue;
					}
					else{
						gestActive = i; count = 1; continue;
					}
				}
			}

		}
		

	}
}