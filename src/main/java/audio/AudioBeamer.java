package audio;

import UI.MainMenuBar;
import fr.delthas.javamp3.Sound;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AudioBeamer {
    private String audioFilePath = "src/main/java/nativeAudioFiles/Medley1.wav";

    public synchronized void streamFile(String audioFilePath){
        boolean isWav = true;
        if (audioFilePath.substring(audioFilePath.length() - 4).equalsIgnoreCase(".mp3")) {
            isWav = false;
        }

        //AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

        try {
            AudioInputStream audioStream;

            if (isWav) { // wav file parsing
                File audioFile = new File(audioFilePath);
                audioStream = AudioSystem.getAudioInputStream(audioFile);
            }
            else { // mp3 file parsing
                Path path = Paths.get(audioFilePath);

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                Sound sound = new Sound(new BufferedInputStream(Files.newInputStream(path)));

                // Read and decode the encoded sound data into the byte array output stream (blocking)
                int read = sound.decodeFullyInto(os);

                // A sample takes 2 bytes
                int samples = read / 2;

                audioStream = new AudioInputStream(new ByteArrayInputStream(os.toByteArray()), sound.getAudioFormat(), samples);
            }

            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            final SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open();

            sourceLine.start();

            System.out.println("Playback started.");

            FourierTransform.getInstance().setWindowSize((int)sourceLine.getFormat().getSampleRate());
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

    public void playMP3Option1(String audioFilePath){

        /*
        try {
            Bitstream bitStream = new Bitstream(new FileInputStream(audioFilePath));

            //set condition to check if there are still more samples to decode if yes continue
            //else stop
            // aka while(undecoded != null)

            while (condition) {
                Decoder decoder = new Decoder();
                int[] samples = decoder.decodeFrame(bitStream.readFrame(), bitStream); //returns the next 2304 samples
                bitStream.closeFrame();

                //do whatever with your samples
            }
        }
        catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }
        catch(BitstreamException bse){
            bse.printStackTrace();
        }
        catch( DecoderException de){
            de.printStackTrace();
        }
         */
    }

    public void playMP3Option2(String audioFilePath){
        Path path = Paths.get(audioFilePath);

        try(Sound sound = new Sound(new BufferedInputStream(Files.newInputStream(path)))) {
            // no need to buffer the SoundInputStream

            // get sound metadata
            System.out.println(sound.getSamplingFrequency());

            // let's copy the decoded data samples into a file!
            Files.copy(sound, Paths.get("C:\\Users\\Alex\\Desktop\\MP3 output\\test1.raw"));
            //sound.decodeFullyInto(outputstream)
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
}
