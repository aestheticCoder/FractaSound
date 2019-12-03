import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class AudioCapture {
    private AudioFormat audioFormat;
    private SourceDataLine sourceDataLine;
    private static Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

    public AudioCapture() throws LineUnavailableException {
        Mixer localMix = AudioSystem.getMixer(mixerInfo[0]);
        audioFormat = getAudioFormat();
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        sourceDataLine = (SourceDataLine) localMix.getLine(dataLineInfo);
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();
        // Interact with sourceDataLine before closing
        sourceDataLine.flush();
        sourceDataLine.stop();
        sourceDataLine.close();
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;
        int sampleSizeInBits = 8;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }
}
