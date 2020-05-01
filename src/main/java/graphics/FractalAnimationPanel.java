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
    private double x,y,velx,vely = 0.0;
    private SamplePeak acceleration = new SamplePeak();
    private final double W;
    private final double H;
    private double lastUpdateTime = System.currentTimeMillis();

    public FractalAnimationPanel(){
        this.setPreferredSize(new Dimension(800,600));
        W = this.getPreferredSize().getWidth();
        H = this.getPreferredSize().getHeight();
        // initMouseListener();
        initComplexConverter();
        this.setBackground(Color.white);
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

        double r = cc.convertToRe(this.x);
        double i = cc.convertToIm(this.y);
        //System.out.println(r + " " + i);
        createFractal(r,i);
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
        int maxIterations = 1024;
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

        //this.x = currentPeakValue.getReal();
        //this.y = currentPeakValue.getImag();

        /**This old implementation was converting x and y twice already converted once in repaint.
         * it also reset the acceleration with a new sample peak esentally deleting any data for acceleration to
         * work of off
         */
        //double xval = cc.convertToRe(currentPeakValue.getReal());
        //double yval = cc.convertToIm(currentPeakValue.getImag());

        //Debug Print lns
        //System.out.println("Real Peak: " + currentPeakValue.getReal() + " Imag Peak: " + currentPeakValue.getImag());
        //System.out.println("Real: " + xval + " Imaginary: " + yval);

        //These comments are from before my fixes, but I'm leaving them so you can
        //better understand what was going wrong.
        //These values are losing a lot of data from being converted.
        // before conversion Real and Imag peak values look dynamic/ good
        //connection is fine
        // xval stays around -0.750..no matter the aduio file
        // yval stays 4.9 - 5.0 E -5 no matter the track

        //acceleration = new SamplePeak;

        //Changed this so we don't delete our data
        acceleration = currentPeakValue;

        animateTransform();
        //repaint();
    }


    /**
     * This likly needs to be fixed so it doesn't get stuck after it goes across once diagonally.
     * As it currently is.
     */
    private synchronized void animateTransform() {
        // Artificial limiter on animation if FastFourier returns too quickly (not likely)
        /*
        if (System.currentTimeMillis() - lastUpdateTime < 10) {
            lastUpdateTime = System.currentTimeMillis();

            // early return for update called too early
            return;
        }
        */

        SamplePeak currentAccel = acceleration;

        if (this.velx > 0) {
            this.velx += currentAccel.getReal();
        }
        else {
            this.velx -= currentAccel.getReal();
        }
        if ( (this.x + this.velx) > (cc.getOriginMinX() + cc.getOriginRangeX()) || (this.x + this.velx) < cc.getOriginMinX() ) {
            this.velx *= -1;
        }

        if (this.vely > 0) {
            this.vely += currentAccel.getImag();
        }
        else {
            this.vely -= currentAccel.getImag();
        }
        if ( (this.y + this.vely) > (cc.getOriginMinY() + cc.getOriginRangeY()) || (this.y + this.vely) < cc.getOriginMinY() ) {
            this.vely *= -1;
        }

        acceleration = new SamplePeak();

        this.x += this.velx;
        this.y += this.vely;
        repaint();
    }

    // Mandelbrot determinant: complex coordinate = (1 - (e^(i*t) - 1)^2) / 4
}
