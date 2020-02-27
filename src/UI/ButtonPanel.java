package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel implements ActionListener {

    private JButton playButton = new JButton("Play");
    private JButton stopButton = new JButton("Stop");

    public ButtonPanel(){
        this.setPreferredSize(new Dimension(800,200));
        this.setBackground(Color.black);
        playButtonProperties();
        stopButtonProperties();
    }

    private void playButtonProperties(){
        playButton.setBounds(0,200,100,40);
        this.add(playButton);
        playButton.addActionListener(this);
        playButton.setVisible(true);
    }

    private void stopButtonProperties(){
        stopButton.setBounds(100,200,100,40);
        this.add(stopButton);
        stopButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Playing AudioStream");
    }


}
