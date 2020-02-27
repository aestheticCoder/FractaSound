package UI;

import audio.AudioBeamer;

import javax.swing.*;
import java.awt.*;


public class UIContainer extends JFrame {


    public UIContainer(){
        initUI();
    }


    private void initUI(){
        add(new graphics.FractalAnimationPanel(), BorderLayout.CENTER);
        setSize(800, 600);
        setTitle("Fractal Visualizer");

        /*
        Can't get this ButtonPanel to show up at the moment but it
        manages to run with no errors.
        Grant you think you could help me debug this?
        -Alex
         */
        add( new ButtonPanel(), BorderLayout.PAGE_END);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        /*
        May need to create a custom look and feel for our UI frame -- setup right now but might want a better or custom one
        As well as other optimizations and clean up for our UI
         */
    }
}
