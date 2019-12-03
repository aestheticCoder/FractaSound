package graphics;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class FractalAnimationPanel extends JPanel implements MouseMotionListener {

    private BufferedImage img;
    private CoordToComplexConverter cc;
    private int x,y = 0;
    private final double W;
    private final double H;
    //I think i might be having a namespace clash on width and height,
    //when i comment out my H and W the event thread error goes away as runs fine but i see nothing
    //when i leave in i have event thread error width/hieght can't be set to 0?
    //i think it's messing up my converter as it sets them to 1 and 2 for w and h
    //Fix: needed to setSize for both the UIFrame and tbe Fractal Animation Panel

    //Just need to clean this up a bit and then put it on git! :)

    public FractalAnimationPanel(){
        setSize(900, 600);
        W = this.getSize().getWidth();
        H = this.getSize().getHeight();
        initMouseListener();
        initComplexConverter();
        this.setBackground(Color.white);
    }

    private void initComplexConverter(){
        this.cc = new CoordToComplexConverter("mandelbrot");
        cc.setOriginRangeX(0,(int) W);
        cc.setOriginRangeY(0,(int) H);
    }

    private void initMouseListener(){
        addMouseMotionListener(this);
    }

    private void doDrawing(Graphics g){
        Graphics2D g2d = (Graphics2D)g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        double r = cc.convertToRe(this.x);
        double i = cc.convertToIm(this.y);
        createFractal(r,i);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
        g.drawImage(this.img, 0, 0, null);
    }


    public void mouseMoved(java.awt.event.MouseEvent e) {
        //eventOutput("Mouse moved", e);
    }

    public void mouseDragged(java.awt.event.MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
        repaint();
    }


    private void createFractal( double ReC, double ImC){

        float Saturation = 1f;
        // Creating a new blank RGB Image to draw the fractal on
        this.img = new BufferedImage((int) W, (int) H, BufferedImage.TYPE_3BYTE_BGR);

        //pick initial ReC and ImC values
        //ReC = -0.7;
        //ImC = 0.27015;

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

                // Checking if the pixel is an escapee
                // If yes, setting the brightness to the maximum
                // If no, setting the brightness to zero since the pixel is a prisoner
                float Brightness = iter < maxIterations ? 1f : 0;

                // Setting Hue to a function of number of iterations (i) taken to escape the radius 2
                // Hue = (i%256)/255.0f;
                // i%256 to bring i in range [0,255]
                // Then dividing by 255.0f to bring it in range [0,1] so that we can pass it to Color.getHSBColor(H,S,B) function
                float Hue = (iter % 256) / 255.0f;

                // Creating the color from HSB values and setting the pixel to the computed color
                Color color = Color.getHSBColor(Hue, Saturation, Brightness);
                this.img.setRGB(x, y, color.getRGB());

            }
        }
    }


}
