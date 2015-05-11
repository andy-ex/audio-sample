package audio.processing.window;

/**
 * Created by Dmitry on 7/9/2014.
 */
public class HammingWindow implements WindowFunction {

    @Override
    public double get(int index, int length) {
        return 0.54f - 0.46f * (float) Math.cos( (Math.PI * 2.0) * index / length);
    }
}
