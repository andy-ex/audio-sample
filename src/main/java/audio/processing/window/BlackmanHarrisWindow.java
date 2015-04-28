package audio.processing.window;

public class BlackmanHarrisWindow implements WindowFunction {
    @Override
    public double get(int index, int length) {
        return 0.35875 - 0.4889 * Math.cos(2.0 * Math.PI * index  / (length - 1))
                + 0.14128 * Math.cos(4.0 * Math.PI * index / (length - 1))
                - 0.01168 * Math.cos(6.0 * Math.PI * index / (length - 1));
    }
}
