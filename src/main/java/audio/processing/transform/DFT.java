package audio.processing.transform;

import audio.processing.model.Complex;
import audio.processing.model.ComplexArray;

/**
 * Created by Dmitry on 7/16/2014.
 */
public class DFT implements FourierTransform {

    @Override
    public ComplexArray transform(double[] real) {

        int n = real.length;
        double[] realPart = new double[n];
        double[] imaginaryPart = new double[n];

        for (int i = 0; i < n; ++i) {
            double realSum = 0.0;
            double imaginarySum = 0.0;
            for (int k = 0; k < n; ++k) {
                double arg = 2.0 * Math.PI * i * k / n;
                realSum += real[k] * Math.cos(arg);
                imaginarySum += real[k] * -Math.sin(arg);
            }
            realPart[i] = realSum;
            imaginaryPart[i] = imaginarySum;
        }

        return new ComplexArray(realPart, imaginaryPart);
    }
}
