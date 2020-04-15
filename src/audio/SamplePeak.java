package audio;

public class SamplePeak {
    private double real, imag;

    public SamplePeak() {
        real = 0.0;
        imag = 0.0;
    }
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
