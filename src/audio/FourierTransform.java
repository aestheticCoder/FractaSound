package audio;

import startup.AbstractObserver;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;

public class FourierTransform {
    private static byte[] sufficientData = new byte[30];
    private static int dataInd = 0;
    private static SamplePeak latestPeak = new SamplePeak(0,0);
    //private static boolean changeFlag = false;
    private static List<AbstractObserver> observers = new ArrayList<>();
    private static long timeSinceUpdate = System.nanoTime();

    /**
     * Takes in another byte value, checks if the byte[] sufficientData is full, and then adds the new byte to the array
     *
     * @param another another byte to be added onto the byte[] of values for parsing
     */
    public static synchronized void byteChomper(byte another) {
        if (dataInd < sufficientData.length) {
            sufficientData[dataInd] = another;
            dataInd++;
        }
        else {
            discreteFourier(sufficientData);

            sufficientData[0] = another;
            dataInd = 1;
        }
    }

    /**
     * Takes in another byte value, checks if the byte[] sufficientData is full, and then adds the new byte to the array
     *
     * @param another another byte to be added onto the byte[] of values for parsing
     */
    public static synchronized void arrayChomski(byte[] another) {
        discreteFourier(another);
    }

    /**
     *
     * @return latestPeak
     */
    public static SamplePeak getLatestPeak() {
        return latestPeak;
    }


    /*
    /**
     * @return flag if SamplePeak changes
     *
    public static boolean isSamplePeakChanging() {
        if (changeFlag) {
            changeFlag = false;
            return true;
        }
        else return false;
    }
    */

    /**
     *
     * @param obs
     */
    public static void attach(AbstractObserver obs) {
        observers.add(obs);
    }

    /*
    private static SamplePeak discreteFourier(double[] inFreq) { // double[] input format
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

        return new SamplePeak(maxReal, maxImag);
    }
    */

    /**
     * Converts a byte[] input into a double[] and then transforms it using Discrete Fourier
     *
     * @param convIntoFreq byte[] input to be converted into double[] and then undergo Discrete Fourier transformation
     */
    private static void discreteFourier(byte[] convIntoFreq) { // byte[] input format wrapper for double[] input
        double[] inFreq = new double[convIntoFreq.length / 8];

        // Convert byte[] into double[]
        byte[] tempByte = new byte[8]; // 8-byte segments of convIntoFreq
        int byteCounter = 0; // increments by 8 per iteration of for-loop
        for (int i = 0; i < inFreq.length; i++) {
            System.arraycopy(convIntoFreq, byteCounter, tempByte, 0,8);
            inFreq[i] = ByteBuffer.wrap(tempByte).getDouble();
            byteCounter += 8;
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

        //changeFlag = true;

        boolean badValue = (maxReal == Double.POSITIVE_INFINITY || maxImag == Double.POSITIVE_INFINITY
                || maxReal == Double.NEGATIVE_INFINITY || maxImag == Double.NEGATIVE_INFINITY
                || Double.isNaN(maxReal) || Double.isNaN(maxImag));

        latestPeak = new SamplePeak(maxReal, maxImag);

        if ((System.nanoTime() - timeSinceUpdate > 1) && (!badValue)) {
            timeSinceUpdate = System.nanoTime();
            //System.out.println(latestPeak.getReal() + " " + latestPeak.getImag());
            notifyAllObservers();
        }
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