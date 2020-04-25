package UI;

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

        //Add Components
        //Add Fractal Animation Panel
        add(new graphics.FractalAnimationPanel(), BorderLayout.CENTER);
        //Add ButtonPanel
        add(new ButtonPanel(), BorderLayout.SOUTH);
    }
}
