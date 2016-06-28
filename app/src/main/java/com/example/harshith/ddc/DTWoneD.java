package com.example.harshith.ddc;

import java.util.Scanner;

/**
 * Created by harshith on 28/6/16.
 */

class DTWoneD extends DTW {
    public int []a; public int []b;

    public void arrayInput(int []arr1, int []arr2){
        a = arr1.clone(); b = arr2.clone();
        m = a.length; n = b.length;
    }

    public void consoleInput(){
        Scanner scan = new Scanner(System.in);
        m = scan.nextInt();
        n = scan.nextInt();

        scan = new Scanner(System.in);

        a = new int[m];
        for (int i=0; i<m; i++) {
            a[i] = scan.nextInt();
        }

        scan = new Scanner(System.in);

        b = new int[n];
        for (int i=0; i<n; i++) {
            b[i] = scan.nextInt();
        }

        //minAccDistProcess();
    }

    public double distance(int p, int q){
        return Math.abs(a[p] - b[q]);
    }

}
