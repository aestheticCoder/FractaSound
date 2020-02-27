package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel implements ActionListener {

    JPanel bPanel = new JPanel();
    JButton playButton = new JButton("Play");
    JButton stopButton = new JButton("Stop");

    public ButtonPanel(){
        setSize(200, 200);
        this.setBackground(Color.white);
        playButtonProperties();
        stopButtonProperties();
    }

    private void playButtonProperties(){
        playButton.setBounds(0,200,100,40);
        bPanel.add(playButton);
        playButton.addActionListener(this);
    }

    private void stopButtonProperties(){
        stopButton.setBounds(100,200,100,40);
        bPanel.add(stopButton);
        stopButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Playing AudioStream");
    }


}
