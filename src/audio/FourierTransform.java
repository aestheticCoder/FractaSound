package audio;

import startup.AbstractObserver;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class FourierTransform {
    private static SamplePeak latestPeak = new SamplePeak(0,0);
    private static List<AbstractObserver> observers = new ArrayList<>();

    /**
     * fourierTransform (FFT or DFT) helper method)
     * TODO: elaborate description of functionality
     *
     * @param another
     */
    public static synchronized void fourierHelper(byte[] another) {
        // Previously, call discreteFourier(another);

        // Now, call the following:
        float[] convertedPCM = convertBytes(another);
        int fakeNumSamples = convertedPCM.length / 2;
        int fakeSampleRate = 44100; // extract actual sample rate if needed later
        complexFFT(convertedPCM, fakeNumSamples, fakeSampleRate);
    }

    /**
     *
     * @return latestPeak
     */
    public static SamplePeak getLatestPeak() {
        return latestPeak;
    }

    /**
     *
     * @param obs
     */
    public static void attach(AbstractObserver obs) {
        observers.add(obs);
    }

    /**
     * // Replace this mess with something less cluttered that works as intended
     *
     * Converts a byte[] input into a float[] and then transforms it using Discrete Fourier
     *
     * @param convIntoFreq byte[] input to be converted into float[] and then undergo Discrete Fourier transformation
     */
    private static void discreteFourier(byte[] convIntoFreq) { // byte[] input format wrapper for double[] input
        final int byteSize = 4;
        int[] inFreq = new int[convIntoFreq.length / byteSize];

        // Convert byte[] into double[]
        byte[] tempByte = new byte[byteSize]; // 8-byte segments of convIntoFreq
        int byteCounter = 0; // increments by 8 per iteration of for-loop
        for (int i = 0; i < inFreq.length; i++) {
            System.arraycopy(convIntoFreq, byteCounter, tempByte, 0,byteSize);
            inFreq[i] = ByteBuffer.wrap(tempByte).getInt();
            byteCounter += byteSize;
        }

        // Begin Discrete Fourier transformation
        int timeInterv = inFreq.length;

        double maxReal = 0.0;
        double maxImag = 0.0;

        for (int k = 0; k < timeInterv; k++) {
            double sumReal = 0.0;
            double sumImag = 0.0;
            for (int t = 0; t < timeInterv; t++) {
                double angle = 2 * Math.PI * t * k / timeInterv;
                double currentVal = inFreq[t];
                sumReal += currentVal * Math.cos(angle) + currentVal * Math.sin(angle);
                sumImag += -currentVal * Math.cos(angle) + currentVal * Math.sin(angle);
            }

            if (sumReal > maxReal) {
                maxReal = sumReal;
            }
            if (sumImag > maxImag) {
                maxImag = sumImag;
            }
        }

        latestPeak = new SamplePeak(maxReal, maxImag);
        notifyAllObservers();
    }

    /**
     * Step 1 of replacement; separate the conversion step for better method cohesion
     *
     * NOTE THAT THIS ONLY WORKS IF USING UNSIGNED PCM DATA, ADDITIONAL CASE NECESSARY FOR SIGNED
     *
     * @param input byte array
     * @return float array
     */
    private static float[] convertBytes(byte[] input) {
        float[] floats = new float[input.length / 2];
        for(int i=0; i < input.length; i+=2) {
            floats[i/2] = input[i] | (input[i+1] << 8);
        }
        return floats;
    }

    /**
     * Step 2 of replacement; implement outer stage of FFT (
     *
     * @param sampleArray
     * @param numSamples
     * @param sampleRate
     */
    private static void complexFFT(float[] sampleArray, int numSamples, int sampleRate) {
        int n, mmax, m, j, istep, i; // Clumsy C-style variable naming, fix variable names and implement closer to usage
        float wtemp, wr, wpr, wpi, wi, theta, tempr, tempi;
        // determine later if downcast of above variables to int/float from long/double is problematic in java implementation

        // TODO: replace this horrifying direct-implementation of C++ code in Java (#define is gone at least)
        //the complex array is real+complex so the array
        // as a size n = 2* number of complex samples
        // real part is the data[index] and
        // the complex part is the data[index+1]
        n = numSamples * 2;

        //binary inversion (note that the indexes
        // start from 0 witch means that the
        // real part of the complex is on the even-indexes
        // and the complex part is on the odd-indexes
        j=0;
        for (i=0;i<n/2;i+=2) {
            if (j > i) {
                //swap the real part
                tempr = sampleArray[j];
                sampleArray[j] = sampleArray[i];
                sampleArray[i] = tempr;
                //swap the complex part
                tempr = sampleArray[j+1];
                sampleArray[j+1] = sampleArray[i+1];
                sampleArray[i+1] = tempr;
                // checks if the changes occurs in the first half
                // and use the mirrored effect on the second half
                if((j/2)<(n/4)){
                    //swap the real part
                    tempr = sampleArray[(n-(i+2))];
                    sampleArray[(n-(i+2))] = sampleArray[(n-(j+2))];
                    sampleArray[(n-(j+2))] = tempr;
                    //swap the complex part
                    tempr = sampleArray[(n-(i+2))+1];
                    sampleArray[(n-(i+2))+1] = sampleArray[(n-(j+2))+1];
                    sampleArray[(n-(j+2))+1] = tempr;
                }
            }
            m=n/2;
            while (m >= 2 && j >= m) {
                j -= m;
                m = m/2;
            }
            j += m;
        }

        // Next is Danielson-Lanczos//Danielson-Lanzcos routine
        mmax=2;
        //external loop
        while (n > mmax) {
            istep = mmax<<1;
            theta=(float)(2*Math.PI/mmax);
            wtemp=(float)Math.sin(0.5*theta);
            wpr = (float)-2.0*wtemp*wtemp;
            wpi=(float)Math.sin(theta);
            wr=1.0f;
            wi=0.0f;
            //internal loops TODO: find implicit arrayIndexOutOfBounds or infinite loop in nested loops below - THIS IS WHERE TO FOCUS
            for (m=1;m<mmax;m+=2) {
                for (i= m;i<=n;i+=istep) {
                    j=i+mmax;
                    tempr=wr*sampleArray[j-1]-wi*sampleArray[j];
                    tempi=wr*sampleArray[j]+wi*sampleArray[j-1];
                    sampleArray[j-1]=sampleArray[i-1]-tempr;
                    sampleArray[j]=sampleArray[i]-tempi;
                    sampleArray[i-1] += tempr;
                    sampleArray[i] += tempi;
                }
                wr=(wtemp=wr)*wpr-wi*wpi+wr;
                wi=wi*wpr+wtemp*wpi+wi;
            }
            mmax=istep;
        }

        // Next we determine fundamental frequency
        int fundamental_frequency=0;
        for(i=2; i<=sampleArray.length; i+=2) {
            if ((i+1) < sampleArray.length && (fundamental_frequency+1) < sampleArray.length) {
                if ((Math.pow(sampleArray[i], 2) + Math.pow(sampleArray[i + 1], 2)) > (Math.pow(sampleArray[fundamental_frequency], 2) + Math.pow(sampleArray[fundamental_frequency + 1], 2))) {
                    fundamental_frequency = i;
                }
            }
        }

        // Last, we update latestPeak as the fundamental frequency (absolute maxima)
        latestPeak = new SamplePeak(sampleArray[fundamental_frequency], sampleArray[fundamental_frequency + 1]);
        notifyAllObservers();
    }

    /**
     *
     */
    private static void notifyAllObservers() {
        for (AbstractObserver obs: observers) {
            obs.update();
        }
    }
}