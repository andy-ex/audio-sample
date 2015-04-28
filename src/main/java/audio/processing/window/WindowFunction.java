package audio.processing.window;

/**
 * Created by Dmitry on 7/9/2014.
 */
public interface WindowFunction {

    default double[] apply(double[] in) {
        int length = in.length;
        double[] result = new double[length];

        for (int i = 0; i < length; ++i) {
            result[i] = in[i] * get(i, length);
        }
        return result;
    }
    double get(int index, int length);

}
