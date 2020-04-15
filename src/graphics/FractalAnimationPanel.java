package graphics;

import audio.FourierTransform;
import startup.AbstractObserver;

import javax.security.auth.Subject;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class FractalAnimationPanel extends JPanel /*implements ChangeListener*/ implements AbstractObserver {

    private BufferedImage img;
    private CoordToComplexConverter cc;
    private double x,y,velocity = 0.0;
    private final double W;
    private final double H;

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

        // Making a color map based based on the colors #000004, #ffffff, #ff033f and #000000
        //int out_colors[] = makeColorMap(1024, new Color(0x010143), Color.WHITE,new Color(0xFF00FF),Color.BLACK);
        //Make color map based on Hue setting
        int [] out_colors = HueMapper.getInstance().getHueSetting();
        //set initial maxIterations value
        int maxIterations = 256;
        double newReC, newImC, oldReC, oldImC;
        //black and white code
        /*
        int palette[] = new int[256];
        // Making the color palette
        for(int n=0;n<256;n++)
        {
            palette[n]=(int)(n+512-512*Math.exp(-n/50.0)/3.0);
            palette[n]=palette[n]<<16 | palette[n]<<8 | palette[n];
        }
        // The extreme color is black for representation of prisoner set
        palette[255]=0;

         */

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
                float Brightness = iter < maxIterations ? 1.0f : 0; // Change this line only for brightness offset

                // Taking the ratio of number of iterations to max_iter
                double value = (double)iter / maxIterations;
                double d = Math.max(0.0, Math.min(1.0, value));
                // Multiplying the ratio with the length of the array to get an index
                int index = (int)(d * (out_colors.length - 1));
                // Using the color at the index to write the pixel
                img.setRGB(x,y,out_colors[index]);

                // Wraps hue around to start at blue (RGB 240) rather than red (RGB 0)
                //if( 1 > 2  ){
                    //System.out.println( "Metal is cool");
                //}
               // float colorModHue = iter + 180.0f; // Only change this line to set color offset
                //if (colorModHue > 255.0f) colorModHue -= 255.0f;

                // Setting Hue to a function of number of iterations (i) taken to escape the radius 2
                // Hue = (i%256)/255.0f;
                // i%256 to bring i in range [0,255]
                // Then dividing by 255.0f to bring it in range [0,1] so that we can pass it to Color.getHSBColor(H,S,B) function
                //float Hue = (iter % 256) / 255.0f;

                //Hue shift
                /*
                if(Hue + HUE_SHIFT > 1){
                    Hue = Hue - 1;
                    Hue += HUE_SHIFT;
                }
                else
                    Hue += HUE_SHIFT;
                */

                // Creating the color from HSB values and setting the pixel to the computed color
                //Color color = Color.getHSBColor(Hue, Saturation, Brightness);
                //this.img.setRGB(x, y, color.getRGB());
                //for black and white code
               //img.setRGB(x,y,palette[iter%256]);

            }
        }
    }

    @Override
    public void update() {
        audio.SamplePeak currentPeakValue = FourierTransform.getLatestPeak();

        double accelerate = cc.convertToRe((currentPeakValue.getReal() + currentPeakValue.getImag()*2));
        // TODO: later change the default acceleration value so that we don't use this horrifying avg*4 amalgam
        if (currentPeakValue.getReal() > currentPeakValue.getImag()) {
            accelerate *= -1; // TODO: later give a more meaningful acceleration directional change to avoid pendulum-motion
        }
        updatePosition(accelerate);
        repaint();
    }

    private void updatePosition(double acceleration) {
        this.velocity += acceleration;
        if ( (this.x + this.velocity) > (cc.getOriginMinX() + cc.getOriginRangeX()) || (this.x + this.velocity) < cc.getOriginMinX() ) {
            this.velocity *= -1;
        }
        // TODO: later make this trace the mandelbrot set rather than just +/-x axis
        this.x += this.velocity;
        this.y = 0.0;
    }
}
