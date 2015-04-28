package audio.features.mfcc;

/**
 * Created by Dmitry on 05.10.2014.
 */
public class MelFilterBank {

    private int[] melSpacedFrequencies;
    private double[][] melFilterBanks;

    public MelFilterBank(double lowerFreq, double upperFreq, double fftSize, int filtersCount) {
        melSpacedFrequencies = new int[filtersCount + 2];

        for (int i = 0; i < melSpacedFrequencies.length; i++) {
            melSpacedFrequencies[i] = (int) Math.floor(((fftSize + 1) / (2.0 * upperFreq)) * filterCoefficient(lowerFreq, upperFreq, i, filtersCount));
        }

        createFilterBanks();
    }

    public double[] getFilterBank(int sampleNumber) {
        return melFilterBanks[sampleNumber];
    }

    public int getFiltersCount() {
        return melFilterBanks.length;
    }

    private void createFilterBanks() {
        melFilterBanks = new double[melSpacedFrequencies.length - 2][];

        for (int i = 1; i < melSpacedFrequencies.length - 1; ++i) {
            double[] h = new double[melSpacedFrequencies[melSpacedFrequencies.length - 1] + 1];
            for (int k = 0; k < h.length; ++k) {
                h[k] = filterBankCoefficient(k, i, melSpacedFrequencies);
            }
            melFilterBanks[i-1] = h;
        }
    }

    private double filterBankCoefficient(int k, int m, int[] f) {

        if (k < f[m - 1]) {
            return 0;
        } else if (k < f[m]) {
            return (double) (k - f[m - 1]) / (f[m] - f[m - 1]);
        } else if (k <= f[m + 1]) {
            return (double) (f[m + 1] - k) / (f[m + 1] - f[m]);
        } else {
            return 0;
        }
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
