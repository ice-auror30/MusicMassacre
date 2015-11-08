package edu.iastate.dhanwada.bridge;

import android.util.Log;

/**
 * Created by IceAuror on 11/8/15.
 * Fourier Transform is an algorithm that transforms a signal in a time doamin, to a signal in a frequency domain.
 * Spectrum represents the frequency bands centered on particualr frequencies
 * center frequency represented as fractoin of sampling rate of time domain = index of freqband/total bands
 * total number of freq bands = length of time signal --> https://en.wikipedia.org/wiki/Nyquist_frequency
 * This program will analyze a signal so that we have definite values for teh frequency that will
 * help in determining the strength of the incoming enemy waves and the enemy.
 * Windowing --> shaping the audio samples before transforming them to the frequency domain.
 * http://www.dspguide.com/ch8.htm
 */
public abstract class FourierTransform {
    public static final int NONE = 0;
    public static final int HAMMING = 1;
    protected static final int LINAVG = 2;
    protected static final int LOGAVG = 3;
    protected static final int NOAVG = 4;
    protected static final float TWOPI = (float) (2* Math.PI);
    protected int timeSize;
    protected int SampleRate;
    protected float bandwidth;
    protected int whichWindow;
    protected float[] real;
    protected float[] imag;
    protected float[] spectrum;
    protected float[] averages;
    protected int whichAverage;
    protected int octaves;
    protected int avgPerOctave;
    private String TAG = "Log";
    /*
     * will construct a fourier transform that will analyze sample buffers of size 'time' and having a sample rate of sample_rate
     */
    FourierTransform(int time, float sample_rate){
        timeSize = time;
        SampleRate = (int)sample_rate;
        bandwidth = (2f / timeSize) * ((float)sample_rate / 2f);
        noAverages();
        allocateArrays();
        whichWindow = NONE;
    }
    //the real, imag, and spectrum will be allocated by the derived classes as the size of arrays will depend on the implementation.
    protected abstract void allocateArrays();

    protected void setComplex(float[] r, float[] i)
    {
        if(real.length != r.length && imag.length != i.length)
        {
            Log.e(TAG, "can't work");
        }
        else
        {
            System.arraycopy(r, 0, real, 0, r.length);
            System.arraycopy(i, 0, imag, 0, i.length);
        }
    }
    //fill the spectrum with the amplitudes of the data in real and imag
    protected void fillSpectrum(){
        for(int i=0; i<spectrum.length; i++)
        {
            spectrum[i] = (float) Math.sqrt(real[i] * real[i] + imag[i] * imag[i]);
        }

        if (whichAverage == LINAVG)
        {
            int avgWidth = (int) spectrum.length / averages.length;
            for (int i =0; i< averages.length; i++)
            {
                float avg =0;
                int j;
                for(j = 0; j<avgWidth; j++)
                {
                    int offset = j +i*avgWidth;
                    if(offset < spectrum.length)
                    {
                        avg += spectrum[offset];
                    }
                    else
                    {
                        break;
                    }
                }
                avg/= j +1;
                averages[i] = avg;
            }
        }
        else if(whichAverage == LOGAVG){
            for (int i=0; i< octaves; i++){
                float lowFreq, hiFreq, freqStep;
                if(i == 0)
                {
                    lowFreq = 0;
                }
                else
                {
                    lowFreq = (SampleRate/2) / (float)Math.pow(2, octaves - i);
                }
                hiFreq = (SampleRate/2) / (float)Math.pow(2, octaves -i -1);
                freqStep = (hiFreq - lowFreq) / avgPerOctave;
                float f =lowFreq;
                for(int j=0; j< avgPerOctave; j++)
                {
                    int offset = j +i * avgPerOctave;
                    averages[offset] = calcAvg(f, f + freqStep);
                    f += freqStep;
                }
            }
        }
    }

    //set the object ot not compute the average
    public void noAverages()
    {
        averages = new float[0];
        whichAverage = NOAVG;
    }
    /**
     * Sets the number of averages used when computing the spectrum and spaces the
     * averages in a linear manner. In other words, each average band will be
     * <code>specSize() / numAvg</code> bands wide.
     *
     * @param numAvg
     *          how many averages to compute
     */
     public void linAverages(int numAvg){
        if(numAvg > spectrum.length/2){
            Log.e(TAG, "Max number of averages should be "+spectrum.length/2);
        }
        else
        {
            averages = new float[numAvg];
        }
        whichAverage = LINAVG;
    }
    /**
     *  Sets the number of averages used when computing the specrum based on the minimum
     *  bandwidth for an octave and the number of bands per octave. For example, with an audio
     *  of sample rate 44100 Hz, logAverages(11, 1) will result in 12 averages, each corresponding
     *  to an octave, first spanning form 0 to 11 Hz.
     */
    public void logAverages(int minBandwidth, int bandsPerOctave){
        //https://en.wikipedia.org/wiki/Nyquist_frequency
        float nyquist = (float) SampleRate / 2f;
        octaves =1;
        while((nyquist/=2)> minBandwidth)
        {
            octaves ++;
        }
        avgPerOctave = bandsPerOctave;
        averages = new float[octaves * bandsPerOctave];
        whichAverage = LOGAVG;
    }
    /**
     * Sets the window to use on the samples
     */
    public void Window(int which)
    {
        if(which < 0 || which > 1)
        {
            Log.e(TAG, "invalid window");
        }else
        {
            whichWindow = which;
        }
    }
    protected void doWindow(float[] samples)
    {
        switch (whichWindow)
        {
            case HAMMING:
                hamming(samples);
                break;
        }
    }
    protected void hamming(float[] samples)
    {
        for (int i =0; i< samples.length; i++)
        {
            samples[i] *= (0.54f - 0.46f * Math.cos(TWOPI * i /(samples.length -1)));
        }
    }
    /**
     * returns the length of hte time domain signal expected by this wave form
     */
    public int timeSize()
    {
        return timeSize;
    }
    /**
     * returns the amplitude of teh requested frequency band
     */
    public float getBand(int i)
    {
        if (i<0) {
            i=0;
        }
        if(i>spectrum.length-1){
            i = spectrum.length - 1;
        }
        return spectrum[i];
    }
    /**
     * returns the with of each frequency band in the spectrum
     */
    public float getBandwidth()
    {
        return bandwidth;
    }
    public abstract void setBand(int i, float a);
    /**
     * scales the amplitude of the ith frequency band by the scaling factor of s
     */
    public abstract void scaleBand(int i, float s);
    /**
     * returns the indes of the frequency band that contains the requested frequency
     */
    public int freqToIndex(float freq)
    {
        if (freq < getBandwidth()/2) return 0;
        if (freq > SampleRate/2 - getBandwidth() / 2) return spectrum.length - 1;
        float fraction = freq / (float) SampleRate;
        int i = Math.round(timeSize * fraction);
        return i;
    }
    /**
     * returns the middlefrequency of the ith band
     */
    public float indextoFreq(int i)
    {
        float bw = getBandwidth();
        if( i== 0) return bw * 0.25f;
        if( i== spectrum.length -1)
        {
            float lastBinBeginFreq = (SampleRate / 2) - (bw/2);
            float binHalfWidth = bw * 0.25f;
            return lastBinBeginFreq + binHalfWidth;
        }
        return i*bw;
    }

    /**
     * returns the average frequency of ith band
     * @param i
     * @return
     */
    public float getAverageCenterFrequency(int i){
        if (whichAverage == LINAVG)
        {
            int avgWidth = (int)spectrum.length / averages.length;
            int centerBinIndex = i* avgWidth + avgWidth/2;
            return indextoFreq(centerBinIndex);
        }
        else if (whichAverage == LOGAVG){
            int octave = i/avgPerOctave;
            int offset = i% avgPerOctave;
            float lowFreq, hiFreq, freqStep;
            if(octave == 0)
            {
                lowFreq = 0;
            }
            else
            {
                lowFreq = (SampleRate / 2)/(float)Math.pow(2, octaves - octave);
            }
            hiFreq = (SampleRate/2) / (float) Math.pow(2, octaves - octave - 1);
            freqStep = (hiFreq - lowFreq) / avgPerOctave;
            float f = lowFreq + offset * freqStep;
            return f+ freqStep/2;
        }
        return 0;
    }
    /**
     * returns amplitude of the frequency
     */
    public float getFreq(float freq)
    {
        return getBand(freqToIndex(freq));
    }
    /**
     * sets amplitude of requested frequency in spectrum to a
     */
    public void setFreq(float freq, float a)
    {
        setBand(freqToIndex(freq), a);
    }

    /**
     * scales the amplitude
     * @param freq
     * @param s
     */
    public void scaleFreq(float freq, float s)
    {
        scaleBand(freqToIndex(freq), s);
    }

    /**
     * returns the number of averages currently being calculated
     * @return
     */
    public int avgSize()
    {
        return averages.length;
    }
    public float getAvg(int i)
    {
        if(averages.length > 0){
            return averages[i];
        } else
        {
            return  0;
        }
    }

    /**
     * calculate the average of the frequencies bounded by the arguments
     * @param lowFreq
     * @param hiFreq
     * @return
     */
    public float calcAvg(float lowFreq, float hiFreq){
        int lowBound = freqToIndex(lowFreq);
        int hiBound = freqToIndex(hiFreq);
        float avg =0;
        for(int i = lowBound; i<= hiBound; i++){
            avg += spectrum[i];
        }
        avg /= (hiBound - lowBound + 1);
        return avg;
    }

    /**
     * performs forward transformation on buffer
     * @param buffer
     */
    public abstract void forward(float[] buffer);

    /**
     * performs forward transformation on buffer
     * @param buffer
     * @param startAt
     */
    public void forward(float[] buffer, int startAt){
        if (buffer.length - startAt < timeSize)
        {
            Log.e(TAG, "Not enough smaples");
        }
        float[] section = new float[timeSize];
        System.arraycopy(buffer, startAt, section, 0,section.length);
        forward(section);
    }

    /**
     * performs inverse transformation
     * @param buffer
     */
    public abstract void inverse(float[] buffer);

    /**
     * performs inverse transformation
     * @param freqReal
     * @param freqImag
     * @param buffer
     */
    public void inverse(float[] freqReal, float[] freqImag, float[] buffer){
        setComplex(freqReal, freqImag);
        inverse(buffer);
    }

    /**
     * returns teh spectrum of teh last FourierTransform.forward() call
     * @return
     */
    public float[] getSpectrum()
    {
        return spectrum;
    }

    /**
     * return the real part of the last FourierTransform.forward() call
     * @return
     */
    public float[] getRealPart()
    {
        return real;
    }

    /**
     * returns the imaginary part of the last FourierTransform.forward() call
     * @return
     */
    public float[] getImaginaryPart()
    {
        return imag;
    }
}
