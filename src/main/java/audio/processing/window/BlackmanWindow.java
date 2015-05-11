package audio.processing.window;

public class BlackmanWindow implements WindowFunction {
    @Override
    public double get(int index, int length) {
        return 0.42 - 0.5 * Math.cos(2 * Math.PI * index / (length)) + 0.08 * Math.cos(4 * Math.PI * index / (length));
    }
}
