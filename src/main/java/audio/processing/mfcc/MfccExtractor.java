package audio.processing.mfcc;

import audio.processing.framing.SignalFramer;
import audio.processing.model.ComplexArray;
import audio.processing.transformation.DCT;
import audio.processing.transformation.DFT;
import audio.processing.transformation.FourierTransform;

import java.util.Arrays;

import static util.ArraysHelper.*;

/**
 * Created by Dmitry on 09.10.2014.
 */
public class MfccExtractor {

    private FourierTransform transformation = new DFT();
    private static final int FFT_SIZE = 512;
    private static final int COEFFICIENTS_COUNT = 26;

    public double[][] extractCoefficients(double[] sourceSignal, int sampleRate) {
        SignalFramer signalFramer = new SignalFramer(sampleRate);
        int framesCount = sourceSignal.length / signalFramer.getFrameStep();

        double[][] result = new double[framesCount][COEFFICIENTS_COUNT];

        for (int i = 0; i < framesCount; ++i) {
            // Get frame of the input signal
            double[] frame = signalFramer.getFrame(sourceSignal, i);

            // Perform Fourier transform
            ComplexArray dft = transformation.transform(Arrays.copyOfRange(frame, 0, FFT_SIZE));

            // Obtain power spectrum
            double[] powerSpectrum = Arrays.copyOfRange(powerSpectrum(dft.abs()), 0, FFT_SIZE / 2 + 1);

            // Apply filter bank
            MelFilterBank filterBank = new MelFilterBank();
            int[] melSpacedFrequencies = filterBank.getMelSpacedFrequencies(0, sampleRate / 2, FFT_SIZE, COEFFICIENTS_COUNT);
            double[] coefficients = new double[COEFFICIENTS_COUNT];
            for (int j = 0; j < coefficients.length; j++) {
                coefficients[j] = sum(multiply(powerSpectrum, filterBank.createFilterBank(melSpacedFrequencies, j + 1)));
            }

            // Take natural logarithm
            log(coefficients);

            // Perform Descrete Cosine Transform
            double[] dct = new DCT().transform(coefficients);
            //dct = normalize(dct);

            result[i] = dct;
        }


        return result;
    }

    private double[] powerSpectrum(double[] in) {
        double[] out = new double[in.length];
        double n = in.length;
        for (int i = 0; i < in.length; i++) {
            out[i] = 1 / n * Math.pow(in[i], 2);
        }
        return out;
    }


}
