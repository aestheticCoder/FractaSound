package audio;

import startup.AbstractObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FourierTransform {
    private static SamplePeak latestPeak = new SamplePeak(0,0);
    private static List<AbstractObserver> observers = new ArrayList<>();
    private static ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * Takes in another byte value, checks if the byte[] sufficientData is full, and then adds the new byte to the array
     *
     * @param another another byte to be added onto the byte[] of values for parsing
     */
    public static synchronized void fourierHelper(byte[] another) {
        //executor.submit(() -> new DiscreteFourier(another));
        executor.submit(() -> new FastFourier(another));
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
     *
     */
    public static void notifyAllObservers() {
        for (AbstractObserver obs: observers) {
            obs.update();
        }
    }

    public static void setLatestPeak(SamplePeak nextPeak) {
        latestPeak = nextPeak;
    }
}