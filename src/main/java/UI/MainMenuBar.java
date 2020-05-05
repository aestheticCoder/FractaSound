package UI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainMenuBar extends JMenuBar {

    private JFileChooser fc;
    private File selectedFile;
    public static String userWAVFilePath;
    public static String userMP3FilePath;

    public MainMenuBar(){
        //JMenu Stuff
        JMenu menu = new JMenu("Import Music");
        this.add(menu);
        JMenuItem i1 = new JMenuItem("Import .WAV file");
        JMenuItem i2 = new JMenuItem("Import .MP3 file");
        //Add ActionListeners
        i1.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                userWAVFilePath = null;
                System.out.println("Import a wav file");
                fc = new JFileChooser();
                //fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                //fc.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "WAV audio files", "wav");
                fc.setFileFilter(filter);
                fc.setDialogTitle("Import a .wav file");


                //Show it.
                int returnVal = fc.showDialog( null,
                        "Import .WAV");

                //Process the results.
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fc.getSelectedFile();
                    fc.setSelectedFile(selectedFile);
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    userWAVFilePath = selectedFile.getAbsolutePath();

                } else if (returnVal == JFileChooser.CANCEL_OPTION) {
                    selectedFile = null;
                    System.out.println("Import Canceled, no file selected.");
                }
                //Reset the file chooser for the next time it's shown.
                fc.setSelectedFile(null);
            }
        });
        i2.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                userMP3FilePath = null;
                System.out.println("Import a mp3 file");
                fc = new JFileChooser();

                //fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                //fc.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "MP3 audio files", "mp3");
                fc.setFileFilter(filter);
                fc.setDialogTitle("Import a .mp3 file");

                //Show it.
                int returnVal = fc.showDialog( null,
                        "Import .MP3");

                //Process the results.
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fc.getSelectedFile();
                    fc.setSelectedFile(selectedFile);
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    userMP3FilePath = selectedFile.getAbsolutePath();

                } else if (returnVal == JFileChooser.CANCEL_OPTION) {
                    selectedFile = null;
                    System.out.println("Import Canceled, no file selected.");
                }
                //Reset the file chooser for the next time it's shown.
                fc.setSelectedFile(null);
            }
        });
        menu.add(i1);
        menu.add(i2);
        this.setVisible(true);
    }

    public static String getUserWAVFilePath() {
        return userWAVFilePath;
    }

    public static String getUserMP3FilePath() { return userMP3FilePath; }
}

