package com.company;

public class CoordToComplexConverter {

    private double ReC;
    private double ImC;
    private int originRangeX;
    private int originRangeY;
    private int originMinX;
    private int originMinY;
    private double realNumMin;
    private double imaginaryNumMin;
    private double realNumRange;
    private double imaginaryNumRange;

    public CoordToComplexConverter(){}

    public CoordToComplexConverter(String fractalType){
        if( fractalType.equalsIgnoreCase("mandelbrot")){
            //setRealNumRange(0.375,-1.375);
            setRealNumRange(0.5,-2.0);
            setImaginaryNumRange(-1,1);
        }
    }

    public void setOriginRangeX( int originStart, int originEnd ){
        this.originMinX = originStart;
        this.originRangeX = originEnd - originStart;
    }
    public void setOriginRangeY( int originStart, int originEnd ){
        this.originMinY = originStart;
        this.originRangeY = originEnd - originStart;
    }

    public void setRealNumRange( double newStart, double newEnd ){
        this.realNumMin = newStart;
        this.realNumRange = newEnd - newStart;
    }

    public void setImaginaryNumRange( double newStart, double newEnd ){
        this.imaginaryNumMin = newStart;
        this.imaginaryNumRange = newEnd - newStart;
    }


    public double convertToRe( int num ){
        return ReC = (((num - originMinX) * realNumRange) / originRangeX) + realNumMin;
    }

    public double convertToIm( int num ){
        return ImC = (((num - originMinY) * imaginaryNumRange) / originRangeY) + imaginaryNumMin;
    }
}
