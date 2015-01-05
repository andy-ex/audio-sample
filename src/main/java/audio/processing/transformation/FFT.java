package audio.processing.transformation;

import audio.processing.model.Complex;
import audio.processing.model.ComplexArray;

import java.util.Arrays;

/**
 * Created by Dmitry on 7/24/2014.
 */
public class FFT implements FourierTransform {

    @Override
    public ComplexArray transform(double[] realPart) {
        realPart = zeroPad(realPart);

        return fft(realPart);
    }

    public ComplexArray fft(double[] in) {
        int n = in.length;
        double[] realPart = in;
        double[] imaginaryPart = new double[n];

        bitReverse(realPart);

        double[] array = in;

        for (int i = 2; i <= array.length; i *= 2) {
            int half = i / 2;
            for (int j = 0; j < array.length; j += i) {
                for (int k = j, angleStep = 0; k < j + half; ++k, angleStep += n/i) {
                    double angle = 2.0 * Math.PI * (angleStep) / array.length;
                    double w_real = realPart[k + half] * Math.cos(angle) + imaginaryPart[k + half] * Math.sin(angle);
                    double w_imaginary = - realPart[k + half] * Math.sin(angle) + imaginaryPart[k + half] * Math.cos(angle);

                    realPart[k + half] = realPart[k] - w_real;
                    imaginaryPart[k+half] = imaginaryPart[k] - w_imaginary;

                    realPart[k] += w_real;
                    imaginaryPart[k] += w_imaginary;

                }
            }
        }

//        Complex[] result = new Complex[array.length];
//        for (int i = 0; i < n; i++) {
//            Complex first = applyDft(getEvenValues(in), i);
//            Complex second = applyDft(getOddValues(in), i);
//
//            double angle = - 2.0 * Math.PI * i / n;
//            Complex factor = new Complex(Math.cos(angle), Math.sin(angle));
//            result[i] = first.plus(factor.times(second));
//        }
//        return result;
        ComplexArray result = new ComplexArray(array.length / 2 + 1);
        result.setRealPart(Arrays.copyOfRange(realPart, 0, array.length / 2 + 1));
        result.setImaginaryPart(Arrays.copyOfRange(imaginaryPart, 0, array.length / 2 + 1));

        return result;
    }

    private void bitReverse(double[] array) {
        int shift = 1 + Integer.numberOfLeadingZeros(array.length);

        for (int i = 0; i < array.length; ++i) {
            int reversed = Integer.reverse(i) >>> shift;
            if (reversed > i) {
                double temp = array[i];
                array[i] = array[reversed];
                array[reversed] = temp;
            }
        }
    }

    private Complex applyDft(double[] real, int k) {

        if (real.length == 1) {
            return new Complex(real[0], 0);
        }
        Complex result = new Complex(0, 0);

        Complex first = applyDft(getEvenValues(real), k);
        Complex second = applyDft(getOddValues(real), k);

        double angle = - 2.0 * Math.PI * k / real.length;
        Complex factor = new Complex(Math.cos(angle), Math.sin(angle));
        result = first.plus(factor.times(second));

        return result;
    }

    public Complex[] inplaceFFT(Complex[] x) {
        int N = x.length;

        // base case
        if (N == 1) return new Complex[]{x[0]};

        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0) {
            throw new RuntimeException("N is not a power of 2");
        }

        // fft of even terms
        Complex[] even = new Complex[N / 2];
        for (int k = 0; k < N / 2; k++) {
            even[k] = x[2 * k];
        }
        Complex[] q = inplaceFFT(even);

        // fft of odd terms
        Complex[] odd = even;  // reuse the array
        for (int k = 0; k < N / 2; k++) {
            odd[k] = x[2 * k + 1];
        }
        Complex[] r = inplaceFFT(odd);

        // combine
        Complex[] y = new Complex[N];
        for (int k = 0; k < N / 2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].plus(wk.times(r[k]));
            y[k + N / 2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    public Complex[] toComplex(double[] x) {
        Complex[] complexes = new Complex[x.length];
        for (int i = 0; i < complexes.length; i++) {
            complexes[i] = new Complex(x[i], 0.0);
        }
        return complexes;
    }

    private double[] getEvenValues(double[] in) {
        int size = in.length / 2;
        double[] result = new double[size];
        for (int i = 0; i < size; ++i) {
            result[i] = in[2 * i];
        }
        return result;
    }

    private double[] getOddValues(double[] in) {
        int size = in.length / 2;
        double[] result = new double[size];
        for (int i = 0; i < size; ++i) {
            result[i] = in[2 * i + 1];
        }
        return result;
    }

    private double[] zeroPad(double[] in) {

        int size  = nearestPowerOf2(in.length);
        double[] array = new double[size];

        for (int i = 0; i < size; ++i) {
            array[i] = i > in.length - 1 ? 0 : in[i];
        }

        return array;
    }

    public static int getFFTSize(int arraySize) {
        return nearestPowerOf2(arraySize);
    }

    private static int nearestPowerOf2(int in) {
        int lower = powerOf2(in/2);
        int bigger = powerOf2(in);

        if (in - lower > bigger - in) {
            return bigger;
        } else {
            return lower;
        }
    }

    private static int powerOf2(int length) {
        if (isPowerOf2(length)) {
            return length;
        }
        length--;
        length |= length >> 1;
        length |= length >> 2;
        length |= length >> 4;
        length |= length >> 8;
        length |= length >> 16;
        return ++length;
    }

    private static boolean isPowerOf2(int n) {
        return (n & (n - 1)) == 0;
    }
}
