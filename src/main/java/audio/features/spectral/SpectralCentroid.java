package audio.features.spectral;

import audio.features.FeatureExtractor;
import audio.processing.model.ComplexArray;
import audio.processing.transformation.STFT;
import util.ArraysHelper;

import java.util.List;

public class SpectralCentroid implements FeatureExtractor {

    @Override
    public double[][] extract(double[] sourceSignal, int sampleRate) {
        List<ComplexArray> magnitudeSpectrum = STFT.transform(sourceSignal);

        double[][] result = new double[1][magnitudeSpectrum.size()];
        for (int i = 0; i < magnitudeSpectrum.size(); ++i) {
            double[] powerSpectrum = ArraysHelper.powerSpectrum(magnitudeSpectrum.get(i));

            double sum = 0;
            for (int j = 1; j <= powerSpectrum.length; j++) {
                sum += j * powerSpectrum[j-1];
            }

            result[0][i] = sum / ArraysHelper.sum(powerSpectrum);
        }

        return result;
    }
}
