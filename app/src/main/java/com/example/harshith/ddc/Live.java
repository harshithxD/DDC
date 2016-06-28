package com.example.harshith.ddc;

import java.util.Scanner;

class Live{
	public int reading[];
	public int sensors = 11;

	public Live(){
		reading = new int [sensors];
	}

	public Live(Live staticLive){
		reading = new int [staticLive.sensors];
		for (int i=0; i<sensors; i++) {
			reading[i] = staticLive.reading[i];
		}
	}

	public void update(int reading){
		for (int i=0; i<sensors; i++) {
			this.reading[i] = reading;
		}
	}

	public void update(int[] readings){
		reading = readings.clone();
	}

	public void updateConsole(){
		Scanner scan = new Scanner(System.in).useDelimiter("\\s");
		for (int i=0; i<sensors; i++) {
			reading[i] = scan.nextInt();
		}
	}

	public void print(){
		for (int i=0; i<sensors; i++) {
			System.out.print(reading[i] + " ");
		}
		System.out.print('\n');
	}

}