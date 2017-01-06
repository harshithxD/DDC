package com.example.harshith.ddc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Debug;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.pmml.Array;

/**
 * Created by harshith on 1/2/17.
 */

public class Learning {
    Classifier cModel;
    int noFrame;
    int noGestures;
    int noAttr;
    ArrayList<Attribute> attrs = new ArrayList<>(noAttr);

    Learning(int noGestures, int noFrame){
        this.noGestures = noGestures;
        this.noFrame = noFrame;
        this.noAttr = Gesture.sensors * noFrame + 1;

        Attribute attr;

        for(int i = 0; i != noAttr - 1; i++) {
            attr = new Attribute(i + "");
            attrs.add(attr);
        }

        ArrayList<String> gestures = new ArrayList<>(noGestures);
        for(int i = 0; i != noGestures; i++){
            gestures.add(i + "");
        }
        Attribute gesture = new Attribute("classAttribute", gestures);
        attrs.add(gesture);

        cModel = new RandomForest();
    }

    public void pseudoTrain(){
        Instances trainingSet = new Instances("Train", attrs, 10);

        trainingSet.setClassIndex(noAttr - 1);

        Instance instance = new DenseInstance(noAttr);
//        instance.setDataset(trainingSet);
        for(int i = 0; i != noAttr - 1; i++) {
            instance.setValue(attrs.get(i), i);
        }
        instance.setValue(attrs.get(noAttr - 1), "0");


        for(int i = 0; i != 10; i++) {
            trainingSet.add(instance);
        }

        instance = new DenseInstance(noAttr);
        for(int i = 0; i != noAttr - 1; i++) {
            instance.setValue(attrs.get(noAttr - 1 - i), i);
        }
        instance.setValue(attrs.get(noAttr - 1), "1");

        for(int i = 0; i != 10; i++) {
            trainingSet.add(instance);
        }
        Random rand = new Random(571321);
        instance = new DenseInstance(noAttr);
        for(int i = 0; i != noAttr - 1; i++) {
            int num = rand.nextInt(noAttr);
            instance.setValue(attrs.get(i), num);
        }

        try {
            cModel.buildClassifier(trainingSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void train(HashMap<Integer, ArrayList<ArrayList<ArrayList<Integer>>> >  instanceReadings) {

        Instances trainingSet = new Instances("Train", attrs, instanceReadings.size());

        trainingSet.setClassIndex(noAttr - 1);

        ArrayList<Instance> instancesArray = new ArrayList<>();

        for (Map.Entry<Integer,ArrayList<ArrayList<ArrayList<Integer>>>> entry: instanceReadings.entrySet()){
            for (ArrayList<ArrayList<Integer>> sessionRead: entry.getValue()){
                ArrayList<Integer> instanceRead = new ArrayList<>();
                for (ArrayList<Integer> read: sessionRead){
                    instanceRead.addAll(read);
                }
                Instance instance = new DenseInstance(noAttr);
                // putting values in the instance
                //instance.setDataset(trainingSet);
                for(int i = 0; i != noAttr - 1; i++) {
                    instance.setValue(instanceRead.get(i), i);
                }
                instance.setValue(attrs.get(noAttr - 1), entry.getKey()+"");
                trainingSet.add(instance);
            }
        }

        try {
            cModel.buildClassifier(trainingSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void predict() {
//        if(cModel != null) {
//            Instance instance = new DenseInstance(noAttr);

//            double[] scores = cModel.distributionForInstance();
//            for (double i : scores) {
//                L.m(i + "");
//            }
//        }
    }

}
