package audio.processing.mfcc;

/**
 * Created by Dmitry on 05.10.2014.
 */
public class MelFilterBank {

    public double[] createFilterBank(int[] melSpacedFrequencies, int sampleNumber) {

        double[] h = new double[melSpacedFrequencies[melSpacedFrequencies.length - 1] + 1];
        for (int k = 0; k < h.length; ++k) {
            h[k] = filterBankCoefficient(k, sampleNumber, melSpacedFrequencies);
        }

        return h;
    }

    private double filterBankCoefficient(int k, int m, int[] f) {

        if (k < f[m - 1]) {
            return 0;
        } else if (k < f[m]) {
            return new Double(k - f[m - 1]) / (f[m] - f[m - 1]);
        } else if (k <= f[m + 1]) {
            return new Double(f[m + 1] - k) / (f[m + 1] - f[m]);
        } else {
            return 0;
        }
    }

    public int[] getMelSpacedFrequencies(double lowerFreq, double upperFreq, double fftSize, int filtersCount) {

        int[] f = new int[filtersCount + 2];

        for (int i = 0; i < f.length; i++) {
            f[i] = (int) Math.floor(((fftSize + 1) / (2.0 * upperFreq)) * filterCoefficient(lowerFreq, upperFreq, i, filtersCount));
        }

        return f;
    }

    private double filterCoefficient(double lowerFreq, double upperFreq, int i, int size) {
        return toFreq(toMel(lowerFreq) + i * (toMel(upperFreq) - toMel(lowerFreq)) / (size + 1));
    }

    private double toMel(double freqHz) {
        return MelFrequencyConverter.frequencyToMel(freqHz);
    }

    private double toFreq(double mel) {
        return MelFrequencyConverter.melToFrequency(mel);
    }
}
