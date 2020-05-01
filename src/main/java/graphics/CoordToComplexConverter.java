package graphics;

public class CoordToComplexConverter {

    private double ReC;
    private double ImC;
    private double originRangeX;
    private double originRangeY;
    private double originMinX;
    private double originMinY;
    private double realNumMin;
    private double imaginaryNumMin;
    private double realNumRange;
    private double imaginaryNumRange;

    public CoordToComplexConverter() {}

    public CoordToComplexConverter(String fractalType) {
        if( fractalType.equalsIgnoreCase("mandelbrot")){
            //setRealNumRange(0.375,-1.375);

            setRealNumRange(-2.0,0.5);
            setImaginaryNumRange(-1.0,1.0);
        }
    }

    public void setOriginRangeX( double originStart, double originEnd ) {
        this.originMinX = originStart;
        this.originRangeX = originEnd - originStart;
    }
    public void setOriginRangeY( double originStart, double originEnd ) {
        this.originMinY = originStart;
        this.originRangeY = originEnd - originStart;
    }

    public void setRealNumRange( double newStart, double newEnd ) {
        this.realNumMin = newStart;
        this.realNumRange = newEnd - newStart;
    }

    public void setImaginaryNumRange( double newStart, double newEnd ) {
        this.imaginaryNumMin = newStart;
        this.imaginaryNumRange = newEnd - newStart;
    }

    public double convertToRe( double num ) {
        return ReC = (((num - originMinX) * realNumRange) / originRangeX) + realNumMin;
    }

    public double convertToIm( double num ) {
        return ImC = (((num - originMinY) * imaginaryNumRange) / originRangeY) + imaginaryNumMin;
    }
}
