package audio.features.spectral;

import audio.features.FeatureExtractor;

public class ZCR implements FeatureExtractor {

    @Override
    public double[][] extract(double[] sourceSignal, int sampleRate) {
        int sum = 0;

        for (int i = 1; i < sourceSignal.length; i++) {
            sum += Math.abs( Math.signum(sourceSignal[i]) - Math.signum(sourceSignal[i - 1]) );
        }

        return new double[][] {{ (double) (sum / 2) / sourceSignal.length}};
    }
}
