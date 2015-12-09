/*
 * Link for tutorial to make a plugin: http://www.thegamecontriver.com/2015/04/android-plugin-unity-android-studio.html
 * https://blog.nraboy.com/2014/06/creating-an-android-java-plugin-for-unity3d/
 */
/**
 * Created by IceAuror on 12/7/15.
 */
package com.example.iceauror.soundanalysis;

import android.content.Context;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
public class SoundAnalysis {
    //Handler Object
    Handler handler;
    //Runnable
    Runnable sampler;
    //used to get the session id of the media player for visual analysis of the file
    String fileAnalysis ="";
    //instance of the visualizer
    Visualizer visualizer;
    //arraylist to store the peak values of waveform
    public ArrayList<Integer> peakValues = new ArrayList<Integer>();
    //arraylist to store the rms values
    public ArrayList<Integer> rmsValues = new ArrayList<Integer>();
    //for the plugin in unity
    public Context context;
    //for the plugin
    private static SoundAnalysis instance;
    //plugin
    public SoundAnalysis(){
        this.instance = this;
    }
    //plugin
    public static SoundAnalysis instance() {
        if(instance == null) {
            instance = new SoundAnalysis();
        }
        return instance;
    }
    //plugin
    public void setContext(Context context) {
        this.context = context;
    }
    //function will do the calculations
    public void analyze(){
                visualizer = new Visualizer(0);
                visualizer.setEnabled(true);
                visualizer.setMeasurementMode(Visualizer.MEASUREMENT_MODE_PEAK_RMS);
                handler = new Handler();
                sampler = new Runnable() {
                    @Override
                    public void run() {
                        Visualizer.MeasurementPeakRms measurementPeakRms = new Visualizer.MeasurementPeakRms();
                        visualizer.getMeasurementPeakRms(measurementPeakRms);
                        peakValues.add(measurementPeakRms.mPeak);
                        rmsValues.add(measurementPeakRms.mRms);
                        handler.postDelayed(sampler, 1000);//basically used as a timer that will update the values of peak and rms every "1000" mS.
                    }
                };
                sampler.run();
            }
    public ArrayList<Integer> getRMS()
    {
        return rmsValues;
    }
    public ArrayList<Integer> getPeak()
    {
        return peakValues;
    }
    public int test(){
        return rmsValues.get(0);
    }
}


