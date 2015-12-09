package com.example.iceauror.soundanalysis;

/**
 * Created by IceAuror on 12/9/15.
 */
public class Bridge {
    public static int returnRMS (){
        SoundAnalysis obj = new SoundAnalysis();
        return obj.test();
    }
    public static int returnTestValue(){
        return 85;
    }
}
