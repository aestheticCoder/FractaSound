package UI;

import audio.AudioBeamer;
import graphics.HueMapper;

import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ButtonPanel extends JPanel {

    private JButton playButton = new JButton("Play");
    private JButton stopButton = new JButton("Stop");
    private Dimension buttonDimension = new Dimension(70, 60);
    private AudioBeamer localAudioStream;
    private  HueMapper localHueMapper;
    private JColorChooser tcc;
    private ColorSelectionModel csm;
    private ExecutorService executor;



    public ButtonPanel(){
        this.setPreferredSize(new Dimension(800,80));
        localAudioStream = new AudioBeamer();
        localHueMapper = HueMapper.getInstance();

        //Add the elements to the panel
        streamSourceIconProperties();
        colorSelector();
        iterationSelector();
        playButtonProperties();
        stopButtonProperties();
        //volumeSliderProperties();
    }

    private void playButtonProperties(){
        playButton.setPreferredSize(buttonDimension);
        this.add(playButton);
        playButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                System.out.println("Playing AudioStream");
                executor = Executors.newSingleThreadExecutor();
                //Start of AudioBeamer Worker Thread
                executor.submit(() -> localAudioStream.streamFile(localAudioStream.getFilePath()));
            }
        });
    }

    private void stopButtonProperties(){
        stopButton.setPreferredSize(buttonDimension);
        this.add(stopButton);
        stopButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                System.out.println("Stopping AudioStream");
                executor.shutdown();
            }
        });
    }

    private void volumeSliderProperties(){
        JSlider volSlider = new JSlider(JSlider.HORIZONTAL,0,100,50);
        JLabel label = new JLabel("Volume: ");
        this.add(label);
        this.add(volSlider);
        volSlider.setMajorTickSpacing(50);
        volSlider.setMinorTickSpacing(10);
        volSlider.setPaintTicks(true);
        volSlider.setPaintLabels(true);
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
        String[] colorMoods = { "Default", "Calm", "Party", "Noir", };
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
        String[] maxIters = {"256", "1024", "128", "64", "32", "2", };
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
