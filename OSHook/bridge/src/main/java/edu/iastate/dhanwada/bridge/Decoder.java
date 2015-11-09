package edu.iastate.dhanwada.bridge;

/**
 * interface for decoders to decode the input samples and transfer them to the fft for analysis
 * Created by IceAuror on 11/8/15.
 */
public interface Decoder {
    /**
     * reads the sample's length and returns the number of samples read.
     * @param samples
     * @return
     */
    public int readSamples(float []samples);
}
