package com.example.iceauror.capturemusic;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;

import java.util.ArrayList;
import java.util.logging.LogRecord;

public class MainActivity extends Activity {
    //Handler Object
    Handler handler;
    //Runnable
    Runnable sampler;
    //UI objects
    Button play;
    TextView info, info_FFT;
    //used to get the session id of the media player for visual analysis of the file
    String fileAnalysis ="";
    //instance of the visualizer
    Visualizer visualizer;
    //arraylist
    //ArrayList <Integer> peakValues = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting the layout of the activity
        setContentView(R.layout.activity_main);
        //finding the UI objects
        //-->buttons
        play = (Button) findViewById(R.id.play);//play the recording button
        //-->TextView
        info = (TextView)findViewById(R.id.textView);
        //info_FFt
        info_FFT=(TextView)findViewById(R.id.textview2);
            //Analyzing music from background mix
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Analyzing", Toast.LENGTH_SHORT).show();
                    visualizer = new Visualizer(0);
                    visualizer.setMeasurementMode(Visualizer.MEASUREMENT_MODE_PEAK_RMS);
                    handler = new Handler();
                    sampler = new Runnable() {
                        @Override
                        public void run() {
                            Visualizer.MeasurementPeakRms measurementPeakRms = new Visualizer.MeasurementPeakRms();
                            visualizer.getMeasurementPeakRms(measurementPeakRms);
                            updateRMS("Sampling Rate of the audio:" + visualizer.getSamplingRate(),
                                    "Peak Value Mode "+ measurementPeakRms.mPeak+'\n'
                                    ,"RMS Value "+ measurementPeakRms.mRms +'\n');
                            handler.postDelayed(sampler, 200);
                        }
                    };
                    visualizer.setEnabled(true);
                    sampler.run();
                }
            });
    }

    public void updateRMS(String SamplingRate, String PeakValue, String RMSValue){
        fileAnalysis    =  SamplingRate+'\n'
                + PeakValue+'\n'
                + RMSValue +'\n';
        //peakValues.add(Integer.parseInt(PeakValue));
        info.setText(fileAnalysis);
    }

}


