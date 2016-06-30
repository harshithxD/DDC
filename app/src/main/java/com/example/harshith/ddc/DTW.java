package com.example.harshith.ddc;

import java.util.Scanner;

class DTW{
	public int m; public int n,w;
	public int [][]distance; public int [][]minAccDist;

	public void arrayInput(int []arr1, int []arr2){
		System.out.println("Inherited.");
	}

	public void arrayInput(int [][]arr1, int [][]arr2){
		System.out.println("Inherited.");
	}

	public void consoleInput(){
		System.out.println("Inherited.");	
	}

	public double distance(int p, int q){
		System.out.println("Inherited.");
		return 0;
	}	
	
	public void distanceProcess(){
		distance = new int[m][n];
		for (int i=0; i<m; i++) {
			for (int j=0; j<n; j++) {
				distance[i][j] = (int) distance(i,j);
			}
		}

	}
	
	public void minAccDistProcess(){

		distanceProcess();
		
		minAccDist = new int[m][n];
		for(int i = 0;i < m; i++){
			for(int j = 0 ; j < n;j++){
				minAccDist[i][j] = 0;
			}
		}

		minAccDist[0][0] = distance[0][0];

		w = 2000000; // set window here
		w=Math.max(w,Math.abs(m-n));

		for (int i=1; i<Math.min(m,w); i++) {
			minAccDist[i][0] = distance[i][0] + minAccDist[i-1][0];
		}
		for (int j=1; j<Math.min(n,w); j++) {
			minAccDist[0][j] = distance[0][j] + minAccDist[0][j-1];
		}

		for (int i=1; i<m; i++) {
			for (int j=Math.max(1,i-w); j<Math.min(n,i+w); j++) {
				minAccDist[i][j] = distance[i][j] + Math.min( minAccDist[i-1][j-1], Math.min( minAccDist[i-1][j], minAccDist[i][j-1] ) );
			}
		}
	}

	public int dtwDistance(){
		
		minAccDistProcess();
		
		return minAccDist[m-1][n-1];
	}

	public int sdtwDistance(){

		return 0;

	}

	public void arrayPrint(int [][]array){
		System.out.print('\n');
		for (int i=0; i<m; i++) {
			for (int j=0; j<n; j++) {
				String text = String.format("%03d", array[i][j]);
				boolean done = false;
				if(array[i][j]!=0){
					for (int k=0; k<text.length(); k++) {
						if(text.charAt(k)=='0' && !done) System.out.print(" ");
						else { System.out.print(text.charAt(k)); done = true; }
					}
				}
				else{
					for (int k=0; k<text.length()-1; k++) {
						System.out.print(" ");
					}
					System.out.print(0);
				}
				System.out.print(" ");
			}
			System.out.print("\n");
		}
	}
}
