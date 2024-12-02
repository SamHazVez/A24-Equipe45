package Equipe45.domain;

public enum MeasurementUnit {
    MILLIMETER("mm", 1.0),
    CENTIMETER("cm", 10.0),
    METER("m", 1000.0),
    INCH("pouce", 25.4),
    FOOT("pied", 304.8);

    private final String symbol;
    private final double conversionToMillimeter;

    MeasurementUnit(String symbol, double conversionToMillimeter) {
        this.symbol = symbol;
        this.conversionToMillimeter = conversionToMillimeter;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getConversionToMillimeter() {
        return conversionToMillimeter;
    }

    public double toMillimeters(double value) {
        return value * conversionToMillimeter;
    }

    public double fromMillimeters(double value) {
        return value / conversionToMillimeter;
    }

}