package audio;

public class FastFourier {

    public FastFourier(byte[] another) {
        float[] transformData = new float[another.length];

        for (int i = 0; i < another.length; i++) {
            transformData[i] = another[i]; // strangely seems to work without altering bytes
        }
        /*
        for(int i = 0; i < another.length; i += 2)
            transformData[i/2] = ((another[i] & 0xFF)|(another[i + 1] << 8)) / 32768.0F;
        */

        complexFFT(transformData, 44100);

        /*
        Update Sequence:

        FourierTransform.setLatestPeak(new SamplePeak(maxReal, maxImag));
        FourierTransform.notifyAllObservers();
        */
    }

    private void complexFFT(float[] data, int sample_rate) {
        //variables for the fft
        //C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
        //ORIGINAL LINE: unsigned long n,mmax,m,j,istep,i;
        int n;
        int mmax;
        int m;
        int j;
        int istep;
        int i;
        double wtemp;
        double wr;
        double wpr;
        double wpi;
        double wi;
        double theta;
        double tempr;
        double tempi;

        int fundamental_frequency;

        //the complex array is real+complex so the array
        //as a size n = 2* number of complex samples
        //real part is the data[index] and
        //the complex part is the data[index+1]

        //new complex array of size n=2*sample_rate

        double[] vector = new double[2 * sample_rate];

        //put the real array in a complex array
        //the complex part is filled with 0's
        //the remaining vector with no data is filled with 0's
        for (n = 0; n < sample_rate;n++)
        {
            if (n < data.length)
            {
                vector[2 * n] = data[n];
            }
            else
            {
                vector[2 * n] = 0F;
            }
            vector[2 * n + 1] = 0F;
        }

        //binary inversion (note that the indexes
        //start from 0 witch means that the
        //real part of the complex is on the even-indexes
        //and the complex part is on the odd-indexes)
        n = sample_rate << 1;
        j = 0;
        for (i = 0;i < n / 2;i += 2)
        {
            if (j > i)
            {
                tempr = (vector[j]);
                (vector[j]) = (vector[i]);
                (vector[i]) = tempr;
                tempr = (vector[j + 1]);
                (vector[j + 1]) = (vector[i + 1]);
                (vector[i + 1]) = tempr;
                if ((j / 2) < (n / 4))
                {
                    tempr = (vector[(n - (i + 2))]);
                    (vector[(n - (i + 2))]) = (vector[(n - (j + 2))]);
                    (vector[(n - (j + 2))]) = tempr;
                    tempr = (vector[(n - (i + 2)) + 1]);
                    (vector[(n - (i + 2)) + 1]) = (vector[(n - (j + 2)) + 1]);
                    (vector[(n - (j + 2)) + 1]) = tempr;
                }
            }
            //C++ TO JAVA CONVERTER WARNING: The right shift operator was replaced by Java's logical right shift operator since the left operand was originally of an unsigned type, but you should confirm this replacement:
            m = n >>> 1;
            while (m >= 2 && j >= m)
            {
                j -= m;
                //C++ TO JAVA CONVERTER WARNING: The right shift operator was replaced by Java's logical right shift operator since the left operand was originally of an unsigned type, but you should confirm this replacement:
                m >>>= 1;
            }
            j += m;
        }
        //end of the bit-reversed order algorithm

        //Danielson-Lanzcos routine
        mmax = 2;
        while (n > mmax)
        {
            istep = mmax << 1;
            theta = -1 * (2 * Math.PI / mmax);
            wtemp = Math.sin(0.5 * theta);
            wpr = -2.0 * wtemp * wtemp;
            wpi = Math.sin(theta);
            wr = 1.0;
            wi = 0.0;
            for (m = 1;m < mmax;m += 2)
            {
                for (i = m;i <= n;i += istep)
                {
                    j = i + mmax;
                    tempr = wr * vector[j - 1] - wi * vector[j];
                    tempi = wr * vector[j] + wi * vector[j - 1];
                    vector[j - 1] = vector[i - 1] - tempr;
                    vector[j] = vector[i] - tempi;
                    vector[i - 1] += tempr;
                    vector[i] += tempi;
                }
                wr = (wtemp = wr) * wpr - wi * wpi + wr;
                wi = wi * wpr + wtemp * wpi + wi;
            }
            mmax = istep;
        }
        //end of the algorithm

        //determine the fundamental frequency
        //look for the maximum absolute value in the complex array
        fundamental_frequency = 0;
        for (i = 2; i <= sample_rate; i += 2)
        {
            if ((Math.pow(vector[i],2) + Math.pow(vector[i + 1],2)) > (Math.pow(vector[fundamental_frequency],2) + Math.pow(vector[fundamental_frequency + 1],2)))
            {
                fundamental_frequency = i;
            }
        }

        //since the array of complex has the format [real][complex]=>[absolute value]
        //the maximum absolute value must be ajusted to half
        fundamental_frequency = (int)Math.floor((float)fundamental_frequency / 2);

        // Last, we update latestPeak as the fundamental frequency (absolute maxima)
        FourierTransform.setLatestPeak(new SamplePeak(fundamental_frequency, fundamental_frequency));
        FourierTransform.notifyAllObservers();
    }
}
