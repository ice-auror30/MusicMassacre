/*
 * Link for tutorial to make a plugin: http://www.thegamecontriver.com/2015/04/android-plugin-unity-android-studio.html
 * https://blog.nraboy.com/2014/06/creating-an-android-java-plugin-for-unity3d/
 */
package com.example.iceauror.capturemusic;

import android.app.Activity;
import android.content.Context;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;

import java.util.ArrayList;

public class MainActivity extends Activity {
    //String for Logging purpose
    String TAG;
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
    //arraylist to store the peak values of waveform
    public ArrayList<Integer> peakValues = new ArrayList<Integer>();
    //arraylist to store the rms values
    public ArrayList<Integer> rmsValues = new ArrayList<Integer>();
    //Byte Array to store the FFT
    byte[] fft;
    //for the plugin in unity
    private Context context;
    private static MainActivity instance;
    //plugin
    public MainActivity(){
        this.instance = this;
    }
    //plugin
    public static MainActivity instance(){
        if(instance == null) {
            instance = new MainActivity();
        }
        return instance;
    }
    //plugin
    public void setContext(Context context) {
        this.context = context;
    }

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
        Log.i(TAG, "Peak"+ '\t' + "RMS");
            //Analyzing music from background mix
            analyze();
    }
    //function will do the calculations
    public void analyze(){
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
                                "Peak Value Mode " + measurementPeakRms.mPeak + '\n'
                                , "RMS Value " + measurementPeakRms.mRms + '\n');
                        peakValues.add(measurementPeakRms.mPeak);
                        rmsValues.add(measurementPeakRms.mRms);
                        Log.i(TAG, "" + visualizer.getCaptureSizeRange()[0] + " " + visualizer.getCaptureSizeRange()[1]);
                        //visualizer.setCaptureSize(visualizer.getCaptureSizeRange()[0]);
                            /*visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
                                @Override
                                public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {

                                }
                                @Override
                                public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

                                }
                            },visualizer.getCaptureSize(), true, false);*/
                        handler.postDelayed(sampler, 1000);//basically used as a timer that will update the values of peak and rms every "1000" mS.
                    }
                };
                visualizer.setEnabled(true);
                sampler.run();
            }
        });
    }
    public void updateRMS(String SamplingRate, String PeakValue, String RMSValue){
       /* String fft_byte = "";
        for(int i= 0; i< fft.length; ++i)
        {
            fft_byte += (char)fft[i];
        }*/
        fileAnalysis    =  SamplingRate+'\n'
                + PeakValue+'\n'
                + RMSValue +'\n';
                //+ fft_byte + '\n';
        //peakValues.add(Integer.parseInt(PeakValue));
        info.setText(fileAnalysis);
    }
    public ArrayList<Integer> getRMS()
    {
        return rmsValues;
    }
    public ArrayList<Integer> getPeak()
    {
        return peakValues;
    }
}


