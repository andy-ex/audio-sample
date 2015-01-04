package audio.processing.framing;

import java.util.Arrays;

/**
 * Created by Dmitry on 05.10.2014.
 */
public class SignalFramer {

    private int frequencyHz;
    private double frameStepSeconds = 0.010;
    private double frameLengthSeconds = 0.025;

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

    public int getFrameLength() {
        return (int) Math.ceil(frameLengthSeconds * frequencyHz);
    }

    public int getFrameStep() {
        return (int) Math.ceil(frameStepSeconds * frequencyHz);
    }
}
