package graphics;

import java.awt.*;
import java.util.Arrays;

public class HueMapper {

    private static HueMapper obj = new HueMapper();

    enum hueSet {
        DEFAULT,
        CALM,
        PARTY,
        NOIR,
    }
    private hueSet hueSetting;

    public HueMapper(){
        this.hueSetting = hueSet.DEFAULT;
    }

    //Utilizing the eager instantiation of the Singleton Design Pattern
    // so that FractalAniPanel, and Button Panel can both access the same instance of HueMapper
    public static HueMapper getInstance(){
        {
            return obj;
        }
    }

    public void setHueSetting(int newHueSetting){
        if( newHueSetting > 3 ){
            System.out.println("Not a valid Hue setting, Hue Setting will be set to: Default");
            newHueSetting = 0;
        }
        switch(newHueSetting){
            case 0:
                this.hueSetting = hueSet.DEFAULT;
                System.out.println("Color set to: DEFAULT");
                break;
            case 1:
                this.hueSetting = hueSet.CALM;
                System.out.println("Color set to: CALM");
                break;
            case 2:
                this.hueSetting = hueSet.PARTY;
                System.out.println("Color set to: PARTY");
                break;
            case 3:
                this.hueSetting = hueSet.NOIR;
                System.out.println("Color set to: NOIR");
                break;
        }
    }

    public int [] getHueSetting(){
        switch(this.hueSetting) {
            case DEFAULT:
                return  makeColorMap(1024, new Color(0x010143), Color.WHITE,new Color(0xFF00FF),Color.BLACK);
            case CALM:
                return  makeColorMap(1024, new Color(0xB0E0E6), new Color (0x008080),new Color(0xFFFF33), new Color(0x2E8B57));
            case PARTY:
                return  makeColorMap(1024, new Color(0x0000CD), new Color(0xFFD700), new Color( 0xFF0000), new Color( 0x8B008B));
            case NOIR:
                return  makeColorMap(1024, new Color(0x2F4F4F), Color.WHITE, new Color(0xC0C0C0), Color.BLACK);
        }
        return makeColorMap(1024, new Color(0x010143), Color.WHITE, new Color(0xFF00FF), Color.BLACK);
    }


    /**
     * Generates a color map based on the input colors
     * @param in_colors the input colors
     * @return an array containing the colors linearly changing from the first input color to the last
     */
    private static int[] makeColorMap(int steps, Color ... in_colors)
    {
        int colorMap[] = new int[steps];
        if (in_colors.length == 1)
        {
            Arrays.fill(colorMap, in_colors[0].getRGB());
            return colorMap;
        }
        double colorDelta = 1.0 / (in_colors.length - 1);
        for (int i=0; i<steps; i++)
        {
            double globalRel = (double)i / (steps - 1);
            int index0 = (int)(globalRel / colorDelta);
            int index1 = Math.min(in_colors.length-1, index0 + 1);
            double localRel = (globalRel - index0 * colorDelta) / colorDelta;

            Color c0 = in_colors[index0];
            int r0 = c0.getRed();
            int g0 = c0.getGreen();
            int b0 = c0.getBlue();
            int a0 = c0.getAlpha();

            Color c1 = in_colors[index1];
            int r1 = c1.getRed();
            int g1 = c1.getGreen();
            int b1 = c1.getBlue();
            int a1 = c1.getAlpha();

            int dr = r1-r0;
            int dg = g1-g0;
            int db = b1-b0;
            int da = a1-a0;

            int r = (int)(r0 + localRel * dr);
            int g = (int)(g0 + localRel * dg);
            int b = (int)(b0 + localRel * db);
            int a = (int)(a0 + localRel * da);
            int rgb =
                    (a << 24) |
                            (r << 16) |
                            (g <<  8) |
                            (b <<  0);
            colorMap[i] = rgb;
        }
        return colorMap;
    }
}
