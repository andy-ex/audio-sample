package sample;

import audio.processing.framing.SignalFramer;
import audio.processing.mfcc.MelFilterBank;
import audio.processing.model.Complex;
import audio.processing.model.ComplexArray;
import audio.processing.transformation.DFT;
import audio.processing.transformation.FFT;
import audio.processing.transformation.FFTTest;
import util.ArraysHelper;

import java.util.Arrays;


public class TestLauncher {

    public static void main(String[] args) {

        mfccTest();

    }

    private static void mfccTest() {
        MelFilterBank filterBank = new MelFilterBank();

        int[] melSpacedFrequencies = filterBank.getMelSpacedFrequencies(300, 8000, 512, 26);
        System.out.println(melSpacedFrequencies.length);
        System.out.println(Arrays.toString(melSpacedFrequencies));

        System.out.println(filterBank.createFilterBank(melSpacedFrequencies, 1).length);
        System.out.println(Arrays.toString(filterBank.createFilterBank(melSpacedFrequencies, 1)));
    }

    private static void frameTest() {
        SignalFramer framer = new SignalFramer(16);
        double[] signal = ArraysHelper.createSequentialDoubleArray(15);

//        DoubleFFT_1D fft1d = new DoubleFFT_1D(signal.length);
//
//        double[] fftd = new double[signal.length * 2];
//        System.arraycopy(signal, 0, fftd, 0, signal.length);
//        fft1d.realForwardFull(fftd);

        ComplexArray fft = new FFT().transform(signal);
        ComplexArray dft = new DFT().transform(signal);

        //fft.setRealPart(new HammingWindow().apply(fft.getRealPart()));
        //dft.setRealPart(new HammingWindow().apply(dft.getRealPart()));

        System.out.println(fft.toString());
        System.out.println(dft.toString());
    }

    private static void temp() {

        double[] array = new double[]{0, 1, 2, 3, 4, 5, 6, 7};
        double[] arrayCopy = Arrays.copyOf(array, array.length);

        FFT fft = new FFT();

        FFTTest.transformRadix2(array, new double[array.length]);
        ComplexArray result = fft.fft(arrayCopy);

        System.out.println(Arrays.toString(array));
        System.out.println(Arrays.toString(result.getRealPart()));

//        int shift = 1 + Integer.numberOfLeadingZeros(array.length);
//
//        for (int i = 0; i < array.length; ++i) {
//            int reversed = Integer.reverse(i) >>> shift;
//            if (reversed > i) {
//                int temp = array[i];
//                array[i] = array[reversed];
//                array[reversed] = temp;
//            }
//        }

//        for (int i = 2; i <= array.length; i *= 2) {
//            int half = i / 2;
//            for (int j = 0; j < array.length; j += i) {
//                for (int k = j; k < j + half; ++k) {
//                    array[k] += array[k + half];
//                    array[k + half] = array[k];
//                }
//                System.out.println(Arrays.toString(array));
//            }
//        }
        //System.out.println(Arrays.toString(array));

    }

    private static void testFFT() {
        DFT dft = new DFT();
        FFT fft = new FFT();

        double[] real = {0.0, 1.0, 2.0, 3.0, 4, 5, 6, 7};
        double[] imag = {0, 0, 0, 0, 0, 0, 0, 0};

        // Complex[] fftResult = fft.fft(real);
        ComplexArray dftResult = dft.transform(real);
        FFTTest.transform(real, imag);

        System.out.println(Arrays.toString(real));
        System.out.println(Arrays.toString(imag));
        System.out.println(Arrays.toString(dftResult.getRealPart()));
        //System.out.println(Arrays.toString(fftResult));
    }

    private static Complex[] toComplexArray(double[] in) {
        Complex[] result = new Complex[in.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new Complex(in[i], 0);
        }
        return result;
    }

    private static void windowing() {
        int nSamples = 100;
        int m = nSamples / 2;
        double r;
        double pi = Math.PI;
        double[] w = new double[nSamples];

        r = pi / m;
        for (int n = -m; n < m; n++)
            w[m + n] = 0.54f + 0.46f * Math.cos(n * r);

        System.out.println(Arrays.toString(w));

        int[] e = new int[nSamples];
        Arrays.fill(e, 1);
        //System.out.println(Arrays.toString( new HammingWindow().apply(e)));
    }
}