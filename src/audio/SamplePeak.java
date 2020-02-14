package audio;

public class SamplePeak {
    private double real, imag;

    public SamplePeak(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public double getImag() {
        return imag;
    }

    public double getReal() {
        return real;
    }
}
