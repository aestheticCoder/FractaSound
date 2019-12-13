package audio;

import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.nio.ByteOrder;

/**
 * AudioCapture is designed to be the back-end audio collection class for use with our program,
 * and accordingly there should only ever be one instance of this class at any time.  Nonetheless,
 * methods are not declared static (except for the Mixer.Info[] for smoother interfacing with actionListener.
 *
 */
public class AudioCapture {
    private boolean running;
    private Mixer localMix;
    private AudioFormat audioFormat;
    private static Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
    private static int bufferSize;
    private static float[] dataBuff;

    public AudioCapture() {
        running = false;
        audioFormat = getAudioFormat();
        bufferSize = (int) audioFormat.getSampleRate() * audioFormat.getFrameSize();
        dataBuff = new float[bufferSize/2];
        localMix = AudioSystem.getMixer(mixerInfo[0]); // TODO: query/select proper mixer here
    }

    public int playAudio() {
        try {
            running = true;
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            TargetDataLine targetLine = (TargetDataLine) localMix.getLine(dataLineInfo);
            targetLine.open(audioFormat);
            targetLine.start();

            Runnable captureRunner = () -> {
                byte[] inBuffer = new byte[bufferSize];

                while (running) {
                    int bytesAvailable = targetLine.available();
                    if (bytesAvailable >= bufferSize)
                    {
                        int bytesRead = targetLine.read(inBuffer, 0, bufferSize);

                        // Convert  to floating point
                        for(int i = 0; i < bufferSize; i += 2)
                            dataBuff[i/2] = ((inBuffer[i] & 0xFF)|(inBuffer[i + 1] << 8)) / 32768.0F;
                    }
                }
            };
            targetLine.flush();
            targetLine.stop();
            targetLine.close();
            return 0; // Audio stream ended without error
        }
        catch (LineUnavailableException e ) {
            return -1; // Audio stream ended with error
        }
    }

    public void stopAudio() {
        running = false;
    }

    public float[] getDataBuff() {
        return dataBuff;
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;
        int sampleSizeInBits = 8;
        int channels = 1; // TODO: change this to stereo if needed
        boolean signed = true;
        boolean bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN); // gets Endianness of machine
        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }
}
