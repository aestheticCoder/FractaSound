package UI;

import audio.AudioBeamer;
import graphics.FractalAnimationPanel;
import graphics.HueMapper;

import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel {

    private JButton playButton = new JButton("Play");
    private JButton stopButton = new JButton("Stop");
    private Dimension buttonDimension = new Dimension(70, 60);
    private AudioBeamer localAudioStream;
    private  HueMapper localHueMapper;
    private FractalAnimationPanel fracAPInstance;


    public ButtonPanel(FractalAnimationPanel fracAP){
        this.setPreferredSize(new Dimension(800,80));
        localAudioStream = new AudioBeamer();
        localHueMapper = HueMapper.getInstance();
        fracAPInstance = fracAP;

        //Add the elements to the panel
        streamSourceIconProperties();
        colorSelector();
        iterationSelector();
        playButtonProperties();
        stopButtonProperties();
    }

    private void playButtonProperties(){
        playButton.setPreferredSize(buttonDimension);
        this.add(playButton);
        playButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!localAudioStream.isRunning()) {
                    System.out.println("Playing AudioStream");
                    //Start of AudioBeamer Worker Thread
                    fracAPInstance.resetValues();
                    Thread t = new Thread(localAudioStream);
                    t.start();
                }
            }
        });
    }

    private void stopButtonProperties(){
        stopButton.setPreferredSize(buttonDimension);
        this.add(stopButton);
        stopButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                System.out.println("Stopping AudioStream");
                localAudioStream.close();
            }
        });
    }

    private void streamSourceIconProperties(){
        JLabel label = new JLabel("Stream source: ");
        this.add(label);
        String[] wavFiles = { "island_music_x.wav", "sin_1000Hz_-3dBFs_3s.wav", "test250Hz_44100Hz_16bit_05sec.wav",
                "sweep_10Hz_10000Hz_-3dBFS_2s.wav", "sweep_10Hz_10000Hz_-3dBFS_10s.wav",
                "Medley1.wav", "ShakeYourBootay.wav", "Imported WAV file", "Imported MP3 file"};
        final JComboBox<String> cb = new JComboBox<String>(wavFiles);
        cb.setVisible(true);
        this.add(cb);
        Font sourceFont = new Font("SansSerif", Font.ITALIC, 11);
        cb.setFont(sourceFont);
        cb.setSelectedIndex(5);

        cb.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JComboBox cb = (JComboBox)e.getSource();
                int audioIndex = cb.getSelectedIndex();
                String audioSource = (String) cb.getSelectedItem();
                localAudioStream.setAudioFilePath(audioIndex);
                System.out.println("Changed Audio Source to: " + audioSource);
            }
        });

    }

    private void colorSelector(){
        JLabel label = new JLabel("Color Mood: ");
        this.add(label);
        String[] colorMoods = { "Default", "Calm", "Party", "Classical", "Tool", "Noir",};
        final JComboBox<String> cb = new JComboBox<String>(colorMoods);
        cb.setVisible(true);
        this.add(cb);
        Font sourceFont = new Font("SansSerif", Font.ITALIC, 11);
        cb.setFont(sourceFont);
        cb.setSelectedIndex(0);

        cb.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JComboBox cb = (JComboBox)e.getSource();
                int colorNum = cb.getSelectedIndex();
                String color = (String) cb.getSelectedItem();
                localHueMapper.setHueSetting(colorNum);
                System.out.println("Changed Color Setting: " + color);
            }
        });
    }
    private void iterationSelector() {
        JLabel label = new JLabel("Max Iters: ");
        this.add(label);
        String[] maxIters = {"128", "256", "64", "32", };
        final JComboBox<String> cb = new JComboBox<String>(maxIters);
        cb.setVisible(true);
        this.add(cb);
        Font sourceFont = new Font("SansSerif", Font.ITALIC, 11);
        cb.setFont(sourceFont);
        cb.setSelectedIndex(0);

        cb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                int iterIndex = cb.getSelectedIndex();
                String maxIterNum = (String) cb.getSelectedItem();
                localHueMapper.setMaxIters(Integer.parseInt(maxIterNum));
                System.out.println("Changed Max Iterations to: " + maxIterNum);
            }
        });
    }
}
