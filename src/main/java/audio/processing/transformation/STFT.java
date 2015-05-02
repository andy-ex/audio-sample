package audio.processing.transformation;

import audio.processing.model.ComplexArray;
import util.ArraysHelper;

import java.util.ArrayList;
import java.util.List;

public class STFT {

    private static final int DEFAULT_WINDOW_SIZE = 4096;

    public static List<ComplexArray> transform(double[] source) {
        return STFT.transform(source, DEFAULT_WINDOW_SIZE);
    }

    public static List<ComplexArray> transform(double[] source, int windowSize) {
        int frames = (source.length - 1) / windowSize;
        List<ComplexArray> result = new ArrayList<>();

        FFT fft = new FFT();
        double[] buffer = new double[windowSize];
        for (int i = 0; i < frames; i++) {
            ArraysHelper.copyTo(source, i * windowSize, (i + 1) * windowSize, buffer);
            result.add(fft.transform(buffer));
        }

        return result;
    }

}
