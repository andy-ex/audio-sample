package audio.processing.model;

/**
 * Created by Dmitry on 7/16/2014.
 */
public class Complex {

    private double real;
    private double imaginary;

    public Complex() {
    }

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public Complex plus(Complex another) {
        this.setReal(another.getReal() + this.getReal());
        this.setImaginary(another.getImaginary() + this.getImaginary());

        return this;
    }

    public Complex minus(Complex another) {
        this.setReal(another.getReal() - this.getReal());
        this.setImaginary(another.getImaginary() - this.getImaginary());

        return this;
    }

    public Complex times(Complex another) {
        double real = this.getReal() * another.getReal() - this.getImaginary() * another.getImaginary();
        double imaginary = this.getReal() * another.getImaginary() + this.getImaginary() * another.getReal();

        this.setReal(real);
        this.setImaginary(imaginary);

        return this;
    }

    public double getAbsoluteValue() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
    }

    @Override
    public String toString() {
        return real + " + i*" + imaginary;
    }
}