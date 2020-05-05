package graphics;

import audio.FourierTransform;
import audio.SamplePeak;
import startup.AbstractObserver;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FractalAnimationPanel extends JPanel implements AbstractObserver {

    private BufferedImage img;
    private CoordToComplexConverter cc;
    // complex coordinate position
    private double x = 0.275;
    private double y = 0.0;

    // transversal, amplitude, and freq/vol from previous Mandelbrot Cardioid
    private double prevT, lastFreq, lastVol = 0.0;
    private double prevA = 1.0;

    private final double W;
    private final double H;

    public FractalAnimationPanel(){
        this.setPreferredSize(new Dimension(800,600));
        W = this.getPreferredSize().getWidth();
        H = this.getPreferredSize().getHeight();
        // initMouseListener();
        initComplexConverter();
        this.setBackground(Color.black);
        FourierTransform.getInstance().attach(this);
    }

    private void initComplexConverter(){
        this.cc = new CoordToComplexConverter("mandelbrot");
        //Need to change conversion as it assumes a constant scale not a logarithmic one
        //Hz is logarithmic so most of the output gets concentrated to one small end of the set

        //Changed Range from -2 bil to +2bil, to 0 to +700 thou as it better reflects our range
        //and doesn't overly shrink our data
        cc.setOriginRangeX(0.0, 700000.0);
        cc.setOriginRangeY(0.0, 700000.0);
    }
    
    private void doDrawing(Graphics g){
        Graphics2D g2d = (Graphics2D)g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        /* Probably not needed anymore, but will need for DiscreteFourier if ever used again
        double r = cc.convertToRe(this.x);
        double i = cc.convertToIm(this.y);
        */
        //System.out.println(r + " " + i);
        createFractal(x,y);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
        g.drawImage(this.img, 0, 0, null);
    }

    /*
    public void mouseMoved(java.awt.event.MouseEvent e) {
        //eventOutput("Mouse moved", e);
    }

    public void mouseDragged(java.awt.event.MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
        repaint();
    }
    */


    private void createFractal( double ReC, double ImC){

        float Saturation = 1f;
        float HUE_SHIFT = 0.85f;

        // Creating a new blank RGB Image to draw the fractal on
        this.img = new BufferedImage((int) W, (int) H, BufferedImage.TYPE_3BYTE_BGR);

        //pick initial ReC and ImC values
        //ReC = -0.7;
        //ImC = 0.27015;

        //Make color map based on Hue setting
        int [] out_colors = HueMapper.getInstance().getHueSetting();
        //set initial maxIterations value
        //int maxIterations = 1024;
        int maxIterations = HueMapper.getInstance().getMaxIters();
        double newReC, newImC, oldReC, oldImC;


        //draw fractal
        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {

                //newReC = 1.5 * (x - W / 2) / (0.5 * zoom * w) + moveX;
                newReC = 1.5 * (x - W / 2) / (W / 2);

                //newImC = (y - H / 2) / (0.5 * zoom * h) + moveY;
                newImC = (y - H / 2) / (H / 2);


                //represent number of iterations
                int iter;

                //start iteration process
                for (iter = 0; iter < maxIterations; iter++) {

                    //remember previous iteration ReC and ImC
                    oldReC = newReC;
                    oldImC = newImC;

                    //calculate ReC and ImC for the current iteration
                    newReC = oldReC * oldReC - oldImC * oldImC + ReC;
                    newImC = 2 * oldReC * oldImC + ImC;

                    //if point is in escape set (outside radius 2 circle): stop
                    if ((newReC * newReC + newImC * newImC) > 4)
                        break;
                }


                // Taking the ratio of number of iterations to max_iter
                double value = (double)iter / maxIterations;
                double d = Math.max(0.0, Math.min(1.0, value));

                // Multiplying the ratio with the length of the array to get an index
                int index = (int)(d * (out_colors.length - 1));
                // Using the color at the index to write the pixel
                img.setRGB(x,y,out_colors[index]);

            }
        }
    }

    @Override
    public void update() {
        audio.SamplePeak currentPeakValue = FourierTransform.getInstance().getLatestPeak();

        double freq = currentPeakValue.getReal();
        double vol = currentPeakValue.getImag();

        animateTransform(freq, vol);
    }

    private synchronized void animateTransform(double freq, double vol) {
        double t; // increase traversal for higher frequency, decrease for lower/equal
        if (freq > this.lastFreq) {
            t = this.prevT + 0.1;

            /*

            TODO: Improve this by using a scaled difference between freq and lastFreq [not as essential as volume]

             */
        }
        else {
            t = this.prevT - 0.1;
        }

        double a; // decrease amplitude for higher volume, increase for lower/equal;
        // all amplitude values must be in range [1.0, 1.4]
        if (vol > this.lastVol && prevA > 1.0) {
            a = prevA - 0.02;

            /*

            TODO: Improve this by using a scaled difference between vol and lastVol

             */
        }
        else if (prevA <= 1.4) {
            a = prevA + 0.02;
        }
        else {
            a = prevA;
        }

        // Find new circumference-position based on Mandelbrot Cardioid
            // Mandelbrot cardioid: complex coordinate = (1 - (e^(i*t) - 1)^2) / 4
            // = ( [cos(2t) - 2cos(t)] + i[sin(2t) - 2sin(t)] ) / 4
        this.x = (a / 4) * ( Math.cos(2*t) - (2 * Math.cos(t)) );
        this.y = (a / 4) * ( Math.sin(2*t) - (2 * Math.sin(t)) );

        // Set "previous" values for next animateTransform
        prevT = t;
        if (a > 1.4) {
            prevA = 1.4;
        }
        else if (a < 1.0) {
            prevA = 1.0;
        }
        else {
            prevA = a;
        }
        lastFreq = freq;
        lastVol = vol;
        /*
        System.out.println(prevT + " " + prevA + " " + lastFreq + " " + lastVol);
        System.out.println(x + " - " + y);
        */
        repaint();
    }
}
