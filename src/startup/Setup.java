package startup;

import UI.UIContainer;
import audio.AudioBeamer;
import audio.FourierTransform;

import javax.swing.*;

public class Setup {
    public static void main(String[] args) {

        //Set Look and Feel
        //Right now set to system look and feel
        //Must before we initialize anything!
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ignored){}

        // Initialize audio classes
        FourierTransform fourierTransformer = new FourierTransform();
        AudioBeamer localAudioStream = new AudioBeamer();
        String localAudioStreamPath = getAudioPath();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UIContainer fracAnimation = new UIContainer();
                //fracAnimation.pack();
                fracAnimation.setVisible(true);
            }

        });

        // button calls beamMeUp.streamFile(audiophile);
    }

    private static String getAudioPath() {
        return "C:\\Users\\Grant's PC\\Documents\\Capstone\\cleanRepoMan\\FractaSound\\src\\audio\\island_music_x.wav";
        //String audioFilePath = "island_music_x.wav";
    }
}
