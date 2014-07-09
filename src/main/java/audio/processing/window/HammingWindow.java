package audio.processing.window;

/**
 * Created by Dmitry on 7/9/2014.
 */
public class HammingWindow implements WindowFunction {

    @Override
    public double[] apply(int[] in) {
        int length = in.length;
        double[] result = new double[length];

        for (int i = 0; i < length; ++i) {
            result[i] = in[i] * get(i, length);
        }
        return result;
    }

    @Override
    public double get(int index, int length) {
        return 0.54f - 0.46f * (float) Math.cos( (Math.PI * 2.0) * index / length);
    }
}
