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
    private AudioBeamer localAudioStream;
    private ExecutorService executor;

    public ButtonPanel(){
        this.setPreferredSize(new Dimension(800,200));
        this.setBackground(Color.black);
        playButtonProperties();
        stopButtonProperties();
        localAudioStream = new AudioBeamer();
    }

    private void playButtonProperties(){
        playButton.setBounds(0,200,200,100);
        //playButton.setBorder(new RoundedBorder(10));
        //playButton.setBackground(Color.black);
        //playButton.setForeground(Color.green);
        this.add(playButton);
        playButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                System.out.println("Playing AudioStream");
                executor = Executors.newSingleThreadExecutor();

                /*

                Start of AudioBeamer workthread

                 */
                executor.submit(() -> localAudioStream.streamFile(localAudioStream.getFilePath()));
            }
        });
    }

    private void stopButtonProperties(){
        stopButton.setBounds(100,200,200,100);
        this.add(stopButton);
        stopButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                System.out.println("Stopping AudioStream");
                executor.shutdown();
            }
        });
    }
}
