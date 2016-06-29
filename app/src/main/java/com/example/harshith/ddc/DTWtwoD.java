package com.example.harshith.ddc;

/**
 * Created by harshith on 28/6/16.
 */

class DTWtwoD extends DTW {

    public int [][]a; public int [][]b;
    public int dimSize = 0;

    public void arrayInput(int [][]arr1, int [][]arr2){
        a = arr1.clone(); b = arr2.clone();
        m = a.length; n = b.length;
        dimSize = a[0].length;
    }

    public double distance(int p, int q){
        double totalSquared = 0;

        for (int i=0; i<dimSize; i++) {
            totalSquared += Math.pow(a[p][i] - b[q][i],2);
        }

        return Math.sqrt(Math.sqrt(totalSquared));
    }

    public int sdtwDistance(){

        minAccDistProcess();

        int mini = 65536;
        for (int i=0; i<a.length; i++) {
            mini = Math.min(mini,minAccDist[i][n-1]);
        }

        //arrayPrint(minAccDist);

        return mini;

    }

}
