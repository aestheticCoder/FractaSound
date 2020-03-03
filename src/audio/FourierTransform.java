package audio;

import startup.AbstractObserver;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class FourierTransform {
    private static SamplePeak latestPeak = new SamplePeak(0,0);
    private static List<AbstractObserver> observers = new ArrayList<>();

    /**
     * Takes in another byte value, checks if the byte[] sufficientData is full, and then adds the new byte to the array
     *
     * @param another another byte to be added onto the byte[] of values for parsing
     */
    public static synchronized void fourierHelper(byte[] another) {
        discreteFourier(another);
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
     * Converts a byte[] input into a double[] and then transforms it using Discrete Fourier
     *
     * @param convIntoFreq byte[] input to be converted into double[] and then undergo Discrete Fourier transformation
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
     *
     */
    private static void notifyAllObservers() {
        for (AbstractObserver obs: observers) {
            obs.update();
        }
    }
}