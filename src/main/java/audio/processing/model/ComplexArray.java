package audio.processing.model;

/**
 * Created by Dmitry on 8/6/2014
 */
public class ComplexArray {

    private double[] realPart;
    private double[] imaginaryPart;

    public ComplexArray(double[] realPart, double[] imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }

    public ComplexArray(int initialCapacity) {
        this.realPart = new double[initialCapacity];
        this.imaginaryPart = new double[initialCapacity];
    }

    public double[] getRealPart() {
        return realPart;
    }

    public void setRealPart(double[] realPart) {
        this.realPart = realPart;
    }

    public double[] getImaginaryPart() {
        return imaginaryPart;
    }

    public void setImaginaryPart(double[] imaginaryPart) {
        this.imaginaryPart = imaginaryPart;
    }

    public double[] abs() {
        double[] abs = new double[realPart.length];
        for (int i = 0; i < abs.length; i++) {
            abs[i] = Math.sqrt( Math.pow(realPart[i], 2) + Math.pow(imaginaryPart[i], 2));
        }
        return abs;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < realPart.length; i++) {
            result.append("(").append(realPart[i]).append(",");
            result.append(imaginaryPart[i]).append("), ");
        }
        result.append("]");
        return result.toString();
    }
}
