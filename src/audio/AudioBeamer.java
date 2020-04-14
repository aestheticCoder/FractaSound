package audio;

import graphics.HueMapper;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioBeamer {

    private static final int BUFFER_SIZE = 4;
    private String audioFilePath = "src/audio/sweep_10Hz_10000Hz_-3dBFS_10s.wav";

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

    public void setAudioFilePath( int audioSource){
        if( audioSource > 7 ){
            System.out.println("Not a valid Hue setting, Hue Setting will be set to: Default");
            audioSource = 4;
        }
        switch(audioSource){
            case 0:
                this.audioFilePath = "src/audio/island_music_x.wav";
                System.out.println("Audio set to: Island Music");
                break;
            case 1:
                this.audioFilePath = "src/audio/sin_1000Hz_-3dBFS_3s.wav";
                System.out.println("Audio set to: sin 1000Hz 3s");
                break;
            case 2:
                this.audioFilePath = "src/audio/test250Hz_44100Hz_16bit_05sec.wav";
                System.out.println("Audio set to: 250Hz 5s");
                break;
            case 3:
                this.audioFilePath = "src/audio/sweep_10Hz_10000Hz_-3dBFS_2s.wav";
                System.out.println("Audio set to: Sweep 10-10000Hz 2s");
                break;
            case 4:
                this.audioFilePath = "src/audio/sweep_10Hz_10000Hz_-3dBFS_10s.wav";
                System.out.println("Audio set to: Sweep 10-10000Hz 10s");
                break;
            case 5:
                this.audioFilePath = "src/audio/Medley1.wav";
                System.out.println("Audio set to: Medley1");
                break;
            case 6:
                this.audioFilePath = "src/audio/ShakeYourBootay.wav";
                System.out.println("Audio set to: Shake Your Bootay");
                break;
        }


    }
    public String getFilePath() { return audioFilePath; }

}



