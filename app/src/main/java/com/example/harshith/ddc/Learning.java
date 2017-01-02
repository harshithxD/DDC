package com.example.harshith.ddc;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.pmml.Array;

/**
 * Created by harshith on 1/2/17.
 */

public class Learning {
    public void train() {
        int noFrame = 50;
        int noAttr = Gesture.sensors * noFrame + 1;
        int noGestures = 5;
        ArrayList<Attribute> attrs = new ArrayList<>(noAttr);
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

        Classifier cModel = new RandomForest();
    }
}
