package audio;

public class FastFourier {

    /***************************************************************
     * fft.c
     * Douglas L. Jones
     * University of Illinois at Urbana-Champaign
     * January 19, 1992
     * http://cnx.rice.edu/content/m12016/latest/
     *
     *   fft: in-place radix-2 DIT DFT of a complex input
     *
     *   input:
     * n: length of FFT: must be a power of two
     * m: n = 2**m
     *   input/output
     * x: double array of length n with real part of data
     * y: double array of length n with imag part of data
     *
     *   Permission to copy and use this program is granted
     *   as long as this header is included.
     ***************************************************************/
    public FastFourier(double[] x, double[] y, int n, int m, double[] cos, double[] sin) {
        int i,j,k,n1,n2,a;
        double c,s,e,t1,t2;

        // Bit-reverse
        j = 0;
        n2 = n/2;
        for (i=1; i < n - 1; i++) {
            n1 = n2;
            while ( j >= n1 ) {
                j = j - n1;
                n1 = n1/2;
            }
            j = j + n1;

            if (i < j) {
                t1 = x[i];
                x[i] = x[j];
                x[j] = t1;
                t1 = y[i];
                y[i] = y[j];
                y[j] = t1;
            }
        }

        // FFT
        n1 = 0;
        n2 = 1;

        for (i=0; i < m; i++) {
            n1 = n2;
            n2 = n2 + n2;
            a = 0;

            for (j=0; j < n1; j++) {
                c = cos[a];
                s = sin[a];
                a +=  1 << (m-i-1);

                for (k=j; k < n; k=k+n2) {
                    t1 = c*x[k+n1] - s*y[k+n1];
                    t2 = s*x[k+n1] + c*y[k+n1];
                    x[k+n1] = x[k] - t1;
                    y[k+n1] = y[k] - t2;
                    x[k] = x[k] + t1;
                    y[k] = y[k] + t2;
                }
            }
        }

        // Identify fundamental frequency
        double maxReal = x[0];
        double maxImag = y[0];
        double fundamental = 0.0;
        for (int iter = 0; iter < x.length; iter++) {
            if ( (Math.abs(x[iter]) + Math.abs(y[iter])) > (Math.abs(maxReal) + Math.abs(maxImag)) ) {
                maxReal = x[iter];
                maxImag = y[iter];
                fundamental = iter;
            }
        }

        /* NOT ACTUALLY RELEVANT FOR IMPLEMENTATION
        // Convert fundamental to frequency domain
        double freqMax = 10000; //max frequency in Hz
        double freqMin = 10; //min frequency in Hz
        fundamental = (fundamental / n) * (freqMax / n) + freqMin;
        */

        // Convert peak value to volume magnitude
        double volume = (Math.abs(maxReal) + Math.abs(maxImag)) / 2;

        //System.out.println("ff: " + fundamental + " vol: " + volume);
        FourierTransform.getInstance().setLatestPeak(new SamplePeak(fundamental, volume));
        FourierTransform.getInstance().notifyAllObservers();
    }
}
