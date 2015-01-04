package util;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Dmitry on 05.10.2014.
 */
public class ArraysHelper {

    public static int[] createRandomIntArray(int size, int bound) {
        Random random = new Random( (long) (Math.random() * 1000));
        int[] result = new int[size];
        for (int i = 0; i < result.length; i++) {
            result[i] = random.nextInt(bound);
        }
        return result;
    }

    public static int[] createSequentialIntArray(int size) {
        int[] result = new int[size];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    public static double[] createSequentialDoubleArray(int size) {
        double[] result = new double[size];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    public static double[] toDoubleArray(int[] input) {
        double[] result = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = input[i];
        }
        return result;
    }

    public static double sum(double[] in) {
        double sum = 0;
        for (double v : in) {
            sum += v;
        }
        return sum;
    }

    public static double[] multiply(double[] first, double[] second) {
        if (first.length != second.length) {
            throw new IllegalArgumentException("Arrays must be the same size");
        }

        double[] result = new double[first.length];
        for (int i = 0; i < first.length; i++) {
            result[i] = first[i] * second[i];
        }
        return result;
    }

    public static double[] normalize(int[] input) {
        double max = Collections.max(Arrays.asList(ArrayUtils.toObject(input)));
        double[] result = new double[input.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = input[i] / max;
        }
        return result;
    }

    public static double[] normalize(double[] input) {
        double max = Collections.max(Arrays.asList(ArrayUtils.toObject(input)));
        double[] result = new double[input.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = input[i] / max;
        }
        return result;
    }

    public static void log(double[] in) {
        for (int i = 0; i < in.length; i++) {
            in[i] = Math.log(in[i]);
        }
    }
}
