package startup;

import UI.UIContainer;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
//import de.javasoft.plaf.synthetica.*;
//import de.javasoft.synthetica.blackeye.SyntheticaBlackEyeLookAndFeel;

import java.text.ParseException;


public class Setup {
    public static void main(String[] args) {

        FlatDarkLaf.install();

        try {
            // Set L&F to Synthethica Black Eye
            UIManager.setLookAndFeel( new FlatDarkLaf());
        }
        catch (UnsupportedLookAndFeelException ulfe) {
            ulfe.printStackTrace();
        }



        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UIContainer uiContainer = new UIContainer();
                uiContainer.pack();
                uiContainer.setVisible(true);
            }
        });

    }
}