package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioBeamer {

    private static final int BUFFER_SIZE = 44100; // 4

    public synchronized void streamFile(String audioFilePath){

        File audioFile = new File(audioFilePath);

        //AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

        try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            final SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open();


            //final ByteArrayOutputStream out = new ByteArrayOutputStream();

            sourceLine.start();

            System.out.println("Playback started.");

            byte[] bytesBuffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;

            while ((bytesRead = audioStream.read(bytesBuffer)) != -1) {
                FourierTransform.fourierHelper(bytesBuffer);
                sourceLine.write(bytesBuffer, 0, bytesRead);
            }

            sourceLine.drain();
            sourceLine.close();
            audioStream.close();

            System.out.println("Playback completed.");
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
    }

    public void mixerQuery(){
        Mixer.Info[] mixInfos = AudioSystem.getMixerInfo();

        for(Mixer.Info info : mixInfos)
        {
            System.out.println(info.getName() + " ----- " + info.getDescription());
        }
    }

    public String getFilePath() {
        return "src/audio/island_music_x.wav";
    }

}



