package startup;

import UI.UIFrame;
import audio.AudioBeamer;
import audio.FourierTransform;

public class Setup {
    public static void main(String[] args) {
        // Initialize audio classes
        FourierTransform fourierHorsemen = new FourierTransform();
        AudioBeamer beamMeUp = new AudioBeamer();
        String audiophile = getAudioPath();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UIFrame fracAnimation = new UIFrame();
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
