package audio.processing.framing;

import java.util.Arrays;

/**
 * Created by Dmitry on 05.10.2014.
 */
public class SignalFramer {

    private int frequencyHz;
    private double frameStepSeconds = 0.04;
    private double frameLengthSeconds = 0.05;

    public SignalFramer(int frequency) {
        this.frequencyHz = frequency;
    }

    public int[] getFrame(int[] source, int frameNumber) {
        int from = frameNumber * getFrameStep();
        int to = from + getFrameLength();

        return Arrays.copyOfRange(source, from, to);
    }

    public double[] getFrame(double[] source, int frameNumber) {
        int from = frameNumber * getFrameStep();
        int to = from + getFrameLength();

        return Arrays.copyOfRange(source, from, to);
    }

    public void getFrame(double[] source, int frameNumber, double[] putInto) {
        if (putInto.length != getFrameLength()) {
            throw new IllegalArgumentException("Output array must be the size of frame!");
        }

        int from = frameNumber * getFrameStep();
        int to = from + getFrameLength();

        for (int i = from; i < Math.min(to, source.length); i++) {
            putInto[i - from] = source[i];
        }

        if (source.length < to) {
            // Pad with zeros
            for (int i = source.length - from; i < putInto.length; ++i) {
                putInto[i] = 0;
            }
        }
    }

    public int getFrameLength() {
        return (int) Math.ceil(frameLengthSeconds * frequencyHz);
    }

    public int getFrameStep() {
        return (int) Math.ceil(frameStepSeconds * frequencyHz);
    }
}
