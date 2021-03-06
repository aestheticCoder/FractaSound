package audio;

import startup.AbstractObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FourierTransform {
    private static FourierTransform four = new FourierTransform();

    private SamplePeak latestPeak;
    private List<AbstractObserver> observers;
    private ExecutorService executor;
    private int windowSize, windowLogTwo;

    // Lookup tables.  Only need to recompute when size of FFT changes.
    private double[] cos;
    private double[] sin;

    private FourierTransform() {
        observers = new ArrayList<>();
        executor = Executors.newCachedThreadPool();
    }

    private void makeWindow() {
        // formerly an instance variable, but no longer needed
        double[] window = new double[windowSize];
        for(int i = 0; i < window.length; i++)
            window[i] = 0.42 - 0.5 * Math.cos(2 * Math.PI * i / (windowSize - 1))
                    + 0.08 * Math.cos(4 * Math.PI * i / (windowSize - 1));
    }

    public static FourierTransform getInstance() {
        return four;
    }

    public void fourierHelper(byte[] another) {
        // Only used with DFT
        executor.submit(() -> new DiscreteFourier(another));
    }

    public void fourierHelper(double[] reals, double[] imags) {
        executor.submit(() -> new FastFourier(reals, imags, windowSize, windowLogTwo, cos, sin));
    }

    public void setWindowSize(int sampleRate) {
        // Set default values
        latestPeak = new SamplePeak(0,0);

        if (sampleRate <= 11025) {
            windowSize = 32;
        }
        else if (sampleRate <= 44100) {
            windowSize = 256;
        }
        else {
            windowSize = 1024;
        }
        windowLogTwo = (int)(Math.log(windowSize) / Math.log(2));

        // throw runtime exception if windowSize is not a power of 2
        if(windowSize != (1 << windowLogTwo))
            throw new RuntimeException("FFT length must be power of 2");

        // precompute tables
        cos = new double[windowSize / 2];
        sin = new double[windowSize / 2];

        for(int i=0; i<windowSize/2; i++) {
            cos[i] = Math.cos(-2 * Math.PI * i / windowSize);
            sin[i] = Math.sin(-2 * Math.PI * i / windowSize);
        }

        makeWindow();
    }

    public void attach(AbstractObserver obs) {
        observers.add(obs);
    }

    public void notifyAllObservers() {
        for (AbstractObserver obs: observers) {
            obs.update();
        }
    }

    public void setLatestPeak(SamplePeak nextPeak) {
        latestPeak = nextPeak;
    }
    public SamplePeak getLatestPeak() {
        return latestPeak;
    }
    public int getWindowSize() {
        return windowSize;
    }
}