package UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainMenuBar extends JMenuBar {

    private JFileChooser fc;
    private File selectedFile;
    public static String userFilePath;

    public MainMenuBar(){
        //JMenu Stuff
        JMenu menu = new JMenu("Import Music");
        this.add(menu);
        JMenuItem i1 = new JMenuItem("Import .WAV file");
        JMenuItem i2 = new JMenuItem("Import .mp3 file");
        //Add ActionListeners
        i1.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                userFilePath = null;
                System.out.println("Import a wav file");
                fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.setAcceptAllFileFilterUsed(false);

                //Show it.
                int returnVal = fc.showDialog( null,
                        "Import");

                //Process the results.
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fc.getSelectedFile();
                    fc.setSelectedFile(selectedFile);
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    userFilePath = selectedFile.getAbsolutePath();

                } else if (e.equals(JFileChooser.CANCEL_SELECTION)) {
                    selectedFile = null;
                    System.out.println("Import Canceled, no file selected.");
                }
                //Reset the file chooser for the next time it's shown.
                fc.setSelectedFile(null);
            }
        });
        i2.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                System.out.println("Import a mp3 file");
            }
        });
        menu.add(i1);
        menu.add(i2);
        this.setVisible(true);
    }

    public static String getUserFilePath() {
        return userFilePath;
    }
}

