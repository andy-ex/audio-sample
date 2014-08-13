package audio.processing.window;

/**
 * Created by Dmitry on 7/9/2014.
 */
public interface WindowFunction {

    double[] apply(double[] in);
    double get(int index, int length);

}
