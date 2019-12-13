package audio;

public class FourierTransform {
    private double[] realOutput;
    private double[] imagOutput;

    public FourierTransform(int len) {
        realOutput = new double[len];
        imagOutput = new double[len];
    }

    public void transform() {
        maxValues();
    }

    private void discreteFourier(double[] inFreq) {
        int timeInterv = inFreq.length;
        for (int k = 0; k < timeInterv; k++) {
            double sumReal = 0.0;
            double sumImag = 0.0;
            for (int t = 0; t < timeInterv; t++) {
                double angle = 2 * Math.PI * t * k / timeInterv;
                sumReal += t * Math.cos(angle) + inFreq[t] * Math.sin(angle);
                sumImag += -t * Math.cos(angle) + inFreq[t] * Math.sin(angle);
            }
            realOutput[k] = sumReal;
            imagOutput[k] = sumImag;
        }
    }

    private void maxValues() {
    }

    /*
    public double calculate_definite_integral_of_f(f, initial_step_size){
        /*
        This algorithm calculates the definite integral of a function
        from 0 to 1, adaptively, by choosing smaller steps near
        problematic points.

        double x = 0.0;
        h = initial_step_size;
        accumulator = 0.0;
        while (x < 1.0) {
            if (x + h > 1.0) {
                double h = 1.0 - x;  // At end of unit interval, adjust last step to end at 1.
            }
            if (error_too_big_in_quadrature_of_f_over_range(f,[x,x + h])) {
                    h = make_h_smaller(h);
            }
            else {
                    accumulator += quadrature_of_f_over_range(f,[x, x + h]);
                    x += h;
                    if (error_too_small_in_quadrature_of_over_range(f,[x,x + h])){
                        h = make_h_larger(h)  //Avoid wasting time on tiny steps.
                    }
                    return accumulator;
                }
            */
}