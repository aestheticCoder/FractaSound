package audio;

import java.nio.ByteBuffer;

public class FourierTransform {
    private byte[] sufficientData;
    private int dataInd;
    private SamplePeak latestPeak; // TODO: convert to out-reference

    public FourierTransform(int frameRate) {
        sufficientData = new byte[frameRate];
        dataInd = 0;
        latestPeak = new SamplePeak(0,0);
    }

    public synchronized void byteChomper(byte another) {
        if (dataInd < sufficientData.length) {
            sufficientData[dataInd] = another;
            dataInd++;
        }
        else {
            latestPeak = discreteFourier(sufficientData);

            sufficientData[0] = another;
            dataInd = 1;
        }
    }

    public SamplePeak getLatestPeak() {
        return latestPeak;
    }

    /**
     *
     * @param inFreq double[] input
     * @return
     */
    private SamplePeak discreteFourier(double[] inFreq) { // double[] input format
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

    /**
     * Wrapper(overloaded) version of above method call discreteFourier that handles byte[] input before calculation
     *
     * @param convIntoFreq byte[] input to be converted into double[] and then undergo DiscreteFourier
     * @return SamplePeak result of maxima of DiscreteFourier
     */
    private SamplePeak discreteFourier(byte[] convIntoFreq) { // byte[] input format wrapper for double[] input
        double[] values = new double[convIntoFreq.length / 8];

        byte[] tempByte = new byte[8]; // 8-byte segments of convIntoFreq
        int byteCounter = 0; // increments by 8 per iteration of for-loop
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(convIntoFreq, byteCounter, tempByte, 0,8);
            values[i] = ByteBuffer.wrap(tempByte).getDouble();
            byteCounter += 8;
        }

        return discreteFourier(values); // passes to double[] input version
    }
}