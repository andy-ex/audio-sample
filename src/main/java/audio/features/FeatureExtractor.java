package audio.features;

public interface FeatureExtractor {

    double[][] extract(double[] sourceSignal, int sampleRate);
}
