package UI;

import graphics.FractalAnimationPanel;

import javax.swing.*;
import java.awt.*;


public class UIContainer extends JFrame {

    private JFileChooser fc;

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
        //Add MenuBar
        this.setJMenuBar(new MainMenuBar());
        //Add Fractal Animation Panel
        FractalAnimationPanel fracAP = new graphics.FractalAnimationPanel();
        add(fracAP, BorderLayout.CENTER);
        //Add ButtonPanel
        add(new ButtonPanel(fracAP), BorderLayout.SOUTH);
    }
}
