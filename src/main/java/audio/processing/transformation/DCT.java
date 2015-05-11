package audio.processing.transformation;

/**
 * Created by Dmitry on 06.10.2014.
 */
public class DCT implements Transformation<double[], double[]> {

    @Override
    public double[] transform(double[] source) {
        int N = source.length;

        double[] result = new double[N];
        for (int i = 0; i < N; i++) {
            double sum = 0.0;
            for (int j = 0; j < N; j++) {
                sum +=  source[j] * Math.cos(Math.PI * (2*j + 1) * i / (2.0 * N));
            }
            result[i] = 2 * sum * getScalingFactor(i, N);
        }

        return result;
    }

    private double getScalingFactor(int index, int N) {
        return index == 0 ? Math.sqrt(1.0/(4*N)) : Math.sqrt(1.0/(2*N));
    }
}
