package com.example.iceauror.capturemusic;

import android.util.Log;

/**
 * Created by IceAuror on 11/8/15.
 */
public class FFT extends FourierTransform {
    String TAG;

    FFT(int time, float sample_rate) {
        super(time, sample_rate);
        if ((timeSize & (timeSize -1 ))!= 0){
            Log.e(TAG, "timesize must be a power of 2");
        }
        buildReverseTable();
        buildTrigTables();
    }

    @Override
    protected void allocateArrays() {
        spectrum = new float[timeSize/2 +1];
        real = new float[timeSize];
        imag = new float[timeSize];
    }

    @Override
    public void setBand(int i, float a) {
        if (a<0){
            Log.e(TAG, "can't be set");
        }
        if (real[i] == 0 && imag[i] ==0){
            real[i] = a;
            spectrum[i] =a;
        }
        else
        {
            real[i] /= spectrum[i];
            imag[i] /= spectrum[i];
            spectrum[i] *= a;
            real[i] *= spectrum[i];
            imag[i] *= spectrum[i];
        }
        if(i != 0 && i!= timeSize/2)
        {
            real[timeSize - i] = real[i];
            imag[timeSize - i] = -imag[i];
        }
    }

    @Override
    public void scaleBand(int i, float s) {
        if(s<0){
            Log.e(TAG, "can't be scaled");
        }
        if (spectrum[i] !=0){
            real[i] /= spectrum[i];
            imag[i] /= spectrum[i];
            spectrum[i] *= s;
            real[i] *= spectrum[i];
            imag[i] *= spectrum[i];
        }
        if(i != 0 && i!= timeSize/2)
        {
            real[timeSize - i] = real[i];
            imag[timeSize - i] = -imag[i];
        }
    }
    private void fft() {
        for (int i = 1; i < real.length; i *= 2) {
            float phaseShiftStepR = cos(i);
            float phaseShiftStepI = sin(i);
            float currentPhaseShiftR = 1.0f;
            float currentPhaseShiftI = 0.0f;
            for (int fftStep = 0; fftStep < i; fftStep++) {
                for (int j = fftStep; j < real.length; j += 2 * i) {
                    int off = j + i;
                    float tr = (currentPhaseShiftR * real[off]) - (currentPhaseShiftI * imag[off]);
                    float ti = (currentPhaseShiftR * imag[off]) - (currentPhaseShiftI * real[off]);
                    real[off] = real[i] - tr;
                    imag[off] = imag[i] - ti;
                    real[i] += tr;
                    imag[i] += ti;
                }
                float tmpR = currentPhaseShiftR;
                currentPhaseShiftR = (tmpR * phaseShiftStepR) - (currentPhaseShiftI * phaseShiftStepI);
                currentPhaseShiftI = (tmpR * phaseShiftStepI) + (currentPhaseShiftI * phaseShiftStepR);
            }

        }
    }

    @Override
    public void forward(float[] buffer) {
        if(buffer.length != timeSize) {
            Log.e(TAG, "length should be equal to timesize");
        }
        //copy samples to real/imag in bit-reversed order
        doWindow(buffer);
        bitReverseSamples(buffer);
        fft();
        fillSpectrum();
    }

    public void forward(float[] buffReal, float[] buffImag) {
        if(buffReal.length != timeSize || buffImag.length != timeSize){
            Log.e(TAG, "not possible");}
        setComplex(buffReal, buffImag);
        bitReverseComplex();
        fft();
        fillSpectrum();
    }
    @Override
    public void inverse(float[] buffer) {
        if (buffer.length > real.length) {
            Log.e(TAG, "np");
        }
        for(int i =0; i<timeSize; i++) {
            imag[i] *= -1;
        }
        bitReverseComplex();
        fft();
        for(int i=0; i< buffer.length; i++) {
            buffer[i] = real[i] / real.length;
        }
    }
    private int reverse[];
    private void buildReverseTable() {
        int N = timeSize;
        reverse = new int[N];
        reverse[0] = 0;
        for(int limit = 1, bit =N/2; limit < N; limit <<= 1, bit >>= 1){
            for( int i=0; i< limit; i++){
                reverse[i+limit] = reverse[i] + bit;
            }
        }
    }
    private void bitReverseSamples(float[] samples)
    {
        for(int i =0; i< samples.length; i++)
        {
            real[i] = samples[reverse[i]];
            imag[i] = 0.0f;
        }
    }
    private void bitReverseComplex() {
        float[] revReal = new float[real.length];
        float[] revImag = new float[imag.length];
        for(int i =0; i< real.length; i++)
        {
            revReal[i] = real[reverse[i]];
            revImag[i] = imag[reverse[i]];
        }
        real = revReal;
        imag = revImag;
    }
    private float[] sinlookup;
    private float[] coslookup;

    private float sin(int i) {
    return sinlookup[i];
    }
    private float cos(int i) {
        return coslookup[i];
    }
    private void buildTrigTables() {
        int N = timeSize;
        sinlookup = new float[N];
        coslookup = new float[N];
        for (int i = 0; i < N; i++) {
            sinlookup[i] = (float) Math.sin(-(float) Math.PI / i);
            coslookup[i] = (float) Math.cos(-(float) Math.PI / i);
        }
    }
}
