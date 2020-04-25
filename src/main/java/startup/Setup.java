package startup;

import UI.UIContainer;
import javax.swing.*;
import de.javasoft.plaf.synthetica.*;
import de.javasoft.synthetica.blackeye.SyntheticaBlackEyeLookAndFeel;

import java.text.ParseException;


public class Setup {
    public static void main(String[] args) {

        try {
            // Set L&F to Synthethica Black Eye
            UIManager.setLookAndFeel( new SyntheticaBlackEyeLookAndFeel());
        }
        catch (UnsupportedLookAndFeelException ulfe) {
            ulfe.printStackTrace();
        }
        catch(ParseException pe){
            pe.printStackTrace();
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