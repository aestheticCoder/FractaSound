package UI;

import audio.AudioBeamer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ButtonPanel extends JPanel {

    private JButton playButton = new JButton("Play");
    private JButton stopButton = new JButton("Stop");
    private Dimension buttonDimension = new Dimension(80, 60);
    private AudioBeamer localAudioStream;
    private ExecutorService executor;

    public ButtonPanel(){
        this.setPreferredSize(new Dimension(800,80));
        this.setBackground(Color.lightGray);
        streamSourceIconProperties();
        playButtonProperties();
        stopButtonProperties();
        volumeSliderProperties();

        localAudioStream = new AudioBeamer();
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
        JTextField tf = new JTextField(12);
        this.add(label);
        this.add(tf);
        tf.setText("island_music_x.wav");
        Font sourceFont = new Font("SansSerif", Font.ITALIC, 11);
        tf.setFont(sourceFont);
    }
}
