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
        FourierTransform.attach(this);
    }

    private void initComplexConverter(){
        this.cc = new CoordToComplexConverter("mandelbrot");
        cc.setOriginRangeX(-2147483648, 2147483647);
        cc.setOriginRangeY(-2147483648, 2147483647);
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
        int maxIterations = 256;
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
        audio.SamplePeak currentPeakValue = FourierTransform.getLatestPeak();

        double x = cc.convertToRe(currentPeakValue.getReal());
        double y = cc.convertToIm(currentPeakValue.getImag());

        acceleration = new SamplePeak(x, y);
        animateTransform();
    }

    private synchronized void animateTransform() {
        if (System.currentTimeMillis() - lastUpdateTime < 10) {
            lastUpdateTime = System.currentTimeMillis();

            // early return for update called too early
            return;
        }

        SamplePeak currentAccel = acceleration;

        this.velx += currentAccel.getReal();
        if ( (this.x + this.velx) > (cc.getOriginMinX() + cc.getOriginRangeX()) || (this.x + this.velx) < cc.getOriginMinX() ) {
            this.velx *= -1;
        }

        this.vely += currentAccel.getImag();
        if ( (this.y + this.vely) > (cc.getOriginMinY() + cc.getOriginRangeY()) || (this.y + this.vely) < cc.getOriginMinY() ) {
            this.vely *= -1;
        }

        acceleration = new SamplePeak();

        this.x += this.velx;
        this.y += this.vely;
        repaint();
    }
}
