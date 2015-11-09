package edu.iastate.dhanwada.bridge;

/** read in the audio strea and convert it to a signed 32-bit float.
 * Created by IceAuror on 11/8/15.
 */
public class WaveDecoder implements Decoder{

    private final float MAX_VALUE = 1.0f /Short.MAX_VALUE;
    private final EndianDataInputStream in;
    @Override
    public int readSamples(float[] samples) {
        return 0;
    }
}
