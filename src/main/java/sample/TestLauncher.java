package sample;

import audio.processing.model.Complex;
import audio.processing.model.ComplexArray;
import audio.processing.transform.DFT;
import audio.processing.transform.FFT;
import audio.processing.transform.FFTTest;
import audio.processing.transform.FourierTransform;
import audio.processing.window.HammingWindow;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;


public class TestLauncher {

    public static void main(String[] args) {

        temp();
    }

    private static void temp() {

        double[] array = new double[] {0, 1, 2, 3, 4, 5, 6, 7};
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