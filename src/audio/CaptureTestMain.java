package audio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CaptureTestMain extends JFrame {
    private CaptureTestMain() {
        super("Test Capturing Sound");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container content = getContentPane();

        AudioCapture audioIn = new AudioCapture();

        final JButton capture = new JButton("Capture");
        final JButton stop = new JButton("Stop");

        capture.setEnabled(true);
        stop.setEnabled(false);

        ActionListener captureListener = e -> {
                    capture.setEnabled(false);
                    stop.setEnabled(true);
                    if (audioIn.playAudio() != 0) { // This is redundant for now, but may be elaborated later for graceful functionality
                        System.out.println("Audio stream ended with LineUnavailableException");
                    }
        };
        capture.addActionListener(captureListener);
        content.add(capture, BorderLayout.NORTH);

        ActionListener stopListener = e -> {
                    capture.setEnabled(true);
                    stop.setEnabled(false);
                    audioIn.stopAudio();
        };
        stop.addActionListener(stopListener);
        content.add(stop, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        JFrame frame = new CaptureTestMain();
        frame.pack();
        frame.show();
    }
}
