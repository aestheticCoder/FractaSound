package audio;

import UI.MainMenuBar;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioBeamer {
    private String audioFilePath = "src/main/java/nativeAudioFiles/Medley1.wav";

    public synchronized void streamFile(String audioFilePath){

        File audioFile = new File(audioFilePath);

        //AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

        try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            final SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(format, FourierTransform.getInstance().getWindowSize() * 2);


            //final ByteArrayOutputStream out = new ByteArrayOutputStream();

            sourceLine.start();

            System.out.println("Playback started.");

            byte[] bytesBuffer = new byte[FourierTransform.getInstance().getWindowSize() * 2];
            int bytesRead = -1;

            while ((bytesRead = audioStream.read(bytesBuffer)) != -1) {
                double[] reals = new double[bytesBuffer.length / 2];
                double[] imags = new double[bytesBuffer.length / 2];

                int reImIt = 0;
                for (int i = 0; i < bytesBuffer.length; i += 2) {
                    reals[reImIt] = bytesBuffer[i];
                    imags[reImIt] = bytesBuffer[i + 1];
                    reImIt++;
                }
                FourierTransform.getInstance().fourierHelper(reals, imags);

                /*
                int reImIt = 0;
                for (int i = 0; i < bytesBuffer.length; i += 2) {
                    byte b0 = bytesBuffer[i];
                    byte b1 = bytesBuffer[i + 1];

                    if (format.isBigEndian()) {
                        // zeros on right
                        reals[reImIt] = b0 << 4;
                        imags[reImIt] = b1 << 4;
                    }
                    else {
                        // zeros on left
                        reals[reImIt] = b0;
                        imags[reImIt] = b1;
                    }
                    reImIt++;
                }
                */

                //localTransform.fourierHelper(bytesBuffer);
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
        if( audioSource > 8 ){
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
                this.audioFilePath = MainMenuBar.getUserFilePath();
                System.out.println("Audio set to: user source");
                break;
        }


    }
    public String getFilePath() { return audioFilePath; }

}



