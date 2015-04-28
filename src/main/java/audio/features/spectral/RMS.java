package audio.features.spectral;

import audio.features.FeatureExtractor;

public class RMS implements FeatureExtractor{

    @Override
    public double[][] extract(double[] sourceSignal, int sampleRate) {
        double sum = 0;

        for (int i = 0; i < sourceSignal.length; i++) {
            sum += Math.pow(sourceSignal[i], 2.0);
        }

        return new double[][] {{  Math.sqrt( sum / sourceSignal.length) }};
    }
}
