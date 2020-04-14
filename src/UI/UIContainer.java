package UI;

import audio.AudioBeamer;
import graphics.HueMapper;

import javax.security.auth.Subject;
import javax.swing.*;
import java.awt.*;


public class UIContainer extends JFrame {


    public UIContainer(){
        initUI();
    }


    private void initUI(){
        //Set default close op
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Setup for UI Frame
        setTitle("Fractal Visualizer");
        setLayout( new BorderLayout());

        //Intialize AudioBeamer - ACTUALLY DONT
        // AudioBeamer localAudioStream = new AudioBeamer();


        //Add Components
        //Add Fractal Animation Panel
        add(new graphics.FractalAnimationPanel(), BorderLayout.CENTER);

        //Add ButtonPanel
        add(new ButtonPanel(), BorderLayout.SOUTH);

        /*
        May need to create a custom look and feel for our UI frame -- setup right now but might want a better or custom one
        As well as other optimizations and clean up for our UI
         */
    }
}
