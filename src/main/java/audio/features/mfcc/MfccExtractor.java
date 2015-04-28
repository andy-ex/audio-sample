package audio.features.mfcc;

import audio.processing.framing.SignalFramer;
import audio.processing.model.ComplexArray;
import audio.processing.transformation.DCT;
import audio.processing.transformation.FFT;
import audio.processing.transformation.FourierTransform;

import java.util.Arrays;

import static util.ArraysHelper.*;

/**
 * Created by Dmitry on 09.10.2014.
 */
public class MfccExtractor {

    public static final double EPS = 0.000001;
    private FourierTransform transformation = new FFT();
    private static final int COEFFICIENTS_COUNT = 26;

    public double[][] extractCoefficients(double[] sourceSignal, int sampleRate) {
        SignalFramer signalFramer = new SignalFramer(sampleRate);
        int framesCount = sourceSignal.length / signalFramer.getFrameStep();

        int effectiveFFTSize = FFT.getFFTSize(signalFramer.getFrameLength());
        MelFilterBank filterBank = new MelFilterBank(0, sampleRate / 2, effectiveFFTSize, COEFFICIENTS_COUNT);

        //System.out.println("MFCC: frameLength - " + signalFramer.getFrameLength() + ", fft - " + effectiveFFTSize);

        double[] frameBuffer = new double[signalFramer.getFrameLength()];
        double[] powerSpectrumBuffer = new double[effectiveFFTSize / 2 + 1];

        double[][] result = new double[framesCount][COEFFICIENTS_COUNT];

        for (int i = 0; i < framesCount; ++i) {
            // Get frame of the input signal
            signalFramer.getFrame(sourceSignal, i, frameBuffer);

            // Perform Fourier transform
            ComplexArray dft = transformation.transform(frameBuffer);

            // Obtain power spectrum
            powerSpectrum(dft.abs(), powerSpectrumBuffer);

            // Apply filter bank

            double[] coefficients = new double[COEFFICIENTS_COUNT];
            for (int j = 0; j < coefficients.length; j++) {
                coefficients[j] = sum(multiply(powerSpectrumBuffer, filterBank.getFilterBank(j)));
                if (Math.abs(coefficients[j]) < EPS) {
                    coefficients[j] = EPS;
                }
            }

            // Take natural logarithm
            log(coefficients);

            // Perform Discrete Cosine Transform
            double[] dct = new DCT().transform(coefficients);
            //dct = normalize(dct);

            result[i] = Arrays.copyOfRange(dct, 0, 13);
        }

        return result;
    }

    private void powerSpectrum(double[] in, double[] out) {
        if (in.length != out.length) {
            throw new IllegalArgumentException("Arrays must be the same size!");
        }
        double n = in.length;
        for (int i = 0; i < in.length; i++) {
            out[i] = 1 / n * Math.pow(in[i], 2);
        }
    }


}
