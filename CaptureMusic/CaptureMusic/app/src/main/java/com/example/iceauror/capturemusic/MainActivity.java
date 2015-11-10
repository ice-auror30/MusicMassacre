package com.example.iceauror.capturemusic;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Environment;
//import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity {
    //output file
    String output_file = null;
    //declare TAG for debugging purposes
    String TAG = "Recording Test";
    //instance of the MediaRecorder for audio capture
    private MediaRecorder recorder;
    //instance of the MediaPlayer for the recording playback
    private MediaPlayer player;
    //UI objects
    Button record, stop_record, play, stop_play, exit;
    TextView info;
    //used to get the session id of the media player for visual analysis of the file
    int session_id; String fileAnalysis ="";
    //instance of the visualizer
    Visualizer visualizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting the layout of the activity
        setContentView(R.layout.activity_main);
        //finding the UI objects
        //-->buttons
        record = (Button) findViewById(R.id.record);//record button
        stop_record = (Button) findViewById(R.id.stopRecord);//stop record button
        play = (Button) findViewById(R.id.play);//play the recording button
        stop_play = (Button) findViewById(R.id.stopPlay);//stop playing the recording button
        exit = (Button)findViewById(R.id.exit);//
        //-->TextView
        info = (TextView)findViewById(R.id.textView);
        //create an instance of the audio recorder
        recorder = new MediaRecorder();
        //set the source of the "audio capture"
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //set the format of the recording to be saved in
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        //encode the audio file to be played by the media player
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        //setting the name of the recording file
        output_file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        recorder.setOutputFile(output_file);
            //recording
            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "recording", Toast.LENGTH_SHORT).show();
                    try {
                        recorder.prepare();
                        recorder.start();
                    } catch (IOException e) {
                        Log.e(TAG, "record() failed");
                    }
                }
            });

            //stop recording
            stop_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recorder.stop();
                    recorder.release();
                    recorder = null;

                    Toast.makeText(getApplicationContext(), "Stop recording", Toast.LENGTH_SHORT).show();
                }
            });

            //play the recording
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Playing", Toast.LENGTH_SHORT).show();
                    try {
                        player = new MediaPlayer();
                        player.setDataSource(output_file);
                        player.prepare();
                        player.start();
                    } catch (Exception e) {
                        Log.e(TAG, "play failed");
                    }
                    session_id = player.getAudioSessionId();
                    visualizer = new Visualizer(session_id);
                    fileAnalysis    +=  "Sampling Rate of the audio:" + visualizer.getSamplingRate()+'\n'
                                        +"Capture Size: "+visualizer.getCaptureSize()+'\n'
                                        +"Capture Size Range: "+visualizer.getCaptureSizeRange()+'\n'
                                        +"Scaling Mode"+visualizer.getScalingMode()+'\n'
                                        +"Measurement Mode"+visualizer.getMeasurementMode()+'\n';
                    info.setText(fileAnalysis);

                }
            });

            //stop playing
            stop_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Playing stopped", Toast.LENGTH_SHORT).show();
                    player.release();
                    player = null;
                }
            });

    }
}


