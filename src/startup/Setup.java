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
        } catch (Exception ignored) {
        }

        // Initialize audio classes
        FourierTransform fourierTransformer = new FourierTransform();


        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UIContainer uiContainer = new UIContainer();
                uiContainer.pack();
                uiContainer.setVisible(true);
            }

        });

        // button calls beamMeUp.streamFile(audiophile);
    }
}