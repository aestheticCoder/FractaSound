package UI;

import audio.AudioBeamer;

import javax.swing.*;
import java.awt.*;


public class UIFrame extends JFrame {


    public UIFrame(){
        initUI();
    }


    private void initUI(){
        add(new graphics.FractalAnimationPanel(), BorderLayout.CENTER);
        setSize(1200, 800);
        setTitle("Julia Fractal Explorer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*
        May need to create a custom look and feel for our UI frame
        As well as other optimizations and clean up for our UI
         */
    }

    /*
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UIFrame fracAnimation = new UIFrame();
                fracAnimation.setVisible(true);
            }

        });
    }
    */
}
