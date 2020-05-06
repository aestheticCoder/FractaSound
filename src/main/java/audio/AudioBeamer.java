package audio;

import UI.MainMenuBar;
import fr.delthas.javamp3.Sound;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AudioBeamer implements Runnable {
    private String audioFilePath = "src/main/java/nativeAudioFiles/Medley1.wav";
    private boolean endStream;
    private boolean running = false;

    @Override
    public void run() {
        running = true;
        endStream = false;
        boolean isWav = true;
        if (audioFilePath.substring(audioFilePath.length() - 4).equalsIgnoreCase(".mp3")) {
            isWav = false;
        }

        //AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

        try {
            if (isWav) { // wav file parsing
                File audioFile = new File(audioFilePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

                AudioFormat format = audioStream.getFormat();

                // Establish SourceDataLine for audio output to speakers
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                final SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                sourceLine.open();
                sourceLine.start();
                System.out.println("Playback started.");

                // Create byte[] for data analysis in FourierTransform
                FourierTransform.getInstance().setWindowSize((int)sourceLine.getFormat().getSampleRate());
                byte[] bytesBuffer = new byte[FourierTransform.getInstance().getWindowSize() * 2];
                int bytesRead = -1;

                // Read bytes from music via audioStream
                while ((bytesRead = audioStream.read(bytesBuffer)) != -1 && !endStream) {
                    double[] reals = new double[bytesBuffer.length / 2];
                    double[] imags = new double[bytesBuffer.length / 2];

                    int reImIt = 0;
                    for (int i = 0; i < bytesBuffer.length; i += 2) {
                        reals[reImIt] = bytesBuffer[i];
                        imags[reImIt] = bytesBuffer[i + 1];
                        reImIt++;
                    }
                    FourierTransform.getInstance().fourierHelper(reals, imags);
                    sourceLine.write(bytesBuffer, 0, bytesRead);
                }

                sourceLine.drain();
                sourceLine.close();
                audioStream.close();

                System.out.println("Playback completed.");
            }
            else { // mp3 file parsing
                Path path = Paths.get(audioFilePath);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                Sound sound = new Sound(new BufferedInputStream(Files.newInputStream(path)));

                AudioFormat format = sound.getAudioFormat();

                // Establish SourceDataLine for audio output to speakers
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                final SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                sourceLine.open();
                sourceLine.start();
                System.out.println("Playback started.");

                // Create byte[] for data analysis in FourierTransform
                FourierTransform.getInstance().setWindowSize((int)sourceLine.getFormat().getSampleRate());
                byte[] bytesBuffer = new byte[FourierTransform.getInstance().getWindowSize() * 2];
                int bytesRead = -1;

                // Read bytes from music via audioStream
                while ((bytesRead = sound.read(bytesBuffer)) != -1 && !endStream) {
                    double[] reals = new double[bytesBuffer.length / 2];
                    double[] imags = new double[bytesBuffer.length / 2];

                    int reImIt = 0;
                    for (int i = 0; i < bytesBuffer.length; i += 2) {
                        reals[reImIt] = bytesBuffer[i];
                        imags[reImIt] = bytesBuffer[i + 1];
                        reImIt++;
                    }
                    FourierTransform.getInstance().fourierHelper(reals, imags);
                    sourceLine.write(bytesBuffer, 0, bytesRead);
                }

                sourceLine.drain();
                sourceLine.close();
                sound.close();

                System.out.println("Playback completed.");
            }
        }
        catch(LineUnavailableException lue){
            lue.printStackTrace();
        }
        catch(UnsupportedAudioFileException uafe){
            uafe.printStackTrace();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
        running = false;
    }

    public void setAudioFilePath( int audioSource){
        if( audioSource > 9 ){
            System.out.println("Not a valid Hue setting, Hue Setting will be set to: Default");
            audioSource = 5;
        }
        switch(audioSource){
            case 0:
                this.audioFilePath = "src/main/java/nativeAudioFiles/island_music_x.wav";
                System.out.println("Audio set to: Island Music");
                break;
            case 1:
                this.audioFilePath = "src/main/java/nativeAudioFiles/sin_1000Hz_-3dBFS_3s.wav";
                System.out.println("Audio set to: sin 1000Hz 3s");
                break;
            case 2:
                this.audioFilePath = "src/main/java/nativeAudioFiles/test250Hz_44100Hz_16bit_05sec.wav";
                System.out.println("Audio set to: 250Hz 5s");
                break;
            case 3:
                this.audioFilePath = "src/main/java/nativeAudioFiles/sweep_10Hz_10000Hz_-3dBFS_2s.wav";
                System.out.println("Audio set to: Sweep 10-10000Hz 2s");
                break;
            case 4:
                this.audioFilePath = "src/main/java/nativeAudioFiles/sweep_10Hz_10000Hz_-3dBFS_10s.wav";
                System.out.println("Audio set to: Sweep 10-10000Hz 10s");
                break;
            case 5:
                this.audioFilePath = "src/main/java/nativeAudioFiles/Medley1.wav";
                System.out.println("Audio set to: Medley1");
                break;
            case 6:
                this.audioFilePath = "src/main/java/nativeAudioFiles/ShakeYourBootay.wav";
                System.out.println("Audio set to: Shake Your Bootay");
                break;
            case 7:
                this.audioFilePath = MainMenuBar.getUserWAVFilePath();
                System.out.println("Audio set to: Local WAV file.");
                break;
            case 8:
                this.audioFilePath = MainMenuBar.getUserMP3FilePath();
                System.out.println("Audio set to: Local MP3 file.");
                break;
        }
    }

    public String getFilePath() { return audioFilePath; }
    public void close() {
        endStream = true;
    }
    public boolean isRunning() {
        return running;
    }
}
