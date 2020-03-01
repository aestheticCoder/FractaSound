package graphics;

import audio.FourierTransform;
import startup.AbstractObserver;

import javax.security.auth.Subject;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FractalAnimationPanel extends JPanel /*implements ChangeListener*/ implements AbstractObserver {

    private BufferedImage img;
    private CoordToComplexConverter cc;
    private double x,y = 0.0;
    private final double W;
    private final double H;
    private Subject subject;

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
        cc.setOriginRangeX(0,(int) W);
        cc.setOriginRangeY(0,(int) H);
    }
    
    private void doDrawing(Graphics g){
        Graphics2D g2d = (Graphics2D)g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        double r = cc.convertToRe(this.x);
        double i = cc.convertToIm(this.y);
        System.out.println(r + " " + i);
        createFractal(r,i);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
        g.drawImage(this.img, 0, 0, null);
        System.out.println("Got Here");
    }

    /*
    @Override
    public void stateChanged(ChangeEvent e) {
        if (FourierTransform.isSamplePeakChanging()) {
            audio.SamplePeak currentPeakValue = FourierTransform.getLatestPeak();
            this.x = currentPeakValue.getReal();
            this.y = currentPeakValue.getImag();
            paintComponent(getGraphics());
        }
    }
    */

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
        // Creating a new blank RGB Image to draw the fractal on
        this.img = new BufferedImage((int) W, (int) H, BufferedImage.TYPE_3BYTE_BGR);

        //pick initial ReC and ImC values
        //ReC = -0.7;
        //ImC = 0.27015;

        //set initial maxIterations value
        int maxIterations = 300;
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

                // Checking if the pixel is an escapee
                // If yes, setting the brightness to the maximum
                // If no, setting the brightness to zero since the pixel is a prisoner
                float Brightness = iter < maxIterations ? 0.75f : 0; // Change this line only for brightness offset

                // Wraps hue around to start at blue (RGB 240) rather than red (RGB 0)
                float colorModHue = iter + 185.0f; // Only change this line to set color offset
                if (colorModHue > 255.0f) colorModHue -= 255.0f;

                // Setting Hue to a function of number of iterations (i) taken to escape the radius 2
                // Hue = (i%256)/255.0f;
                // i%256 to bring i in range [0,255]
                // Then dividing by 255.0f to bring it in range [0,1] so that we can pass it to Color.getHSBColor(H,S,B) function
                float Hue = (colorModHue % 256) / 255.0f;

                // Creating the color from HSB values and setting the pixel to the computed color
                Color color = Color.getHSBColor(Hue, Saturation, Brightness);
                this.img.setRGB(x, y, color.getRGB());

            }
        }
    }

    @Override
    public void update() {
        audio.SamplePeak currentPeakValue = FourierTransform.getLatestPeak();
        this.x = currentPeakValue.getReal();
        this.y = currentPeakValue.getImag();
        repaint();
    }
}
