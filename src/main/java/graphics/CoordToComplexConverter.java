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

            setRealNumRange(0.5,-2.0);
            setImaginaryNumRange(-1,1);
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

    public double getOriginMinX() {
        return originMinX;
    }
    public double getOriginRangeX() {
        return originRangeX;
    }
    public double getOriginMinY() {
        return originMinY;
    }
    public double getOriginRangeY() {
        return originRangeY;
    }
}