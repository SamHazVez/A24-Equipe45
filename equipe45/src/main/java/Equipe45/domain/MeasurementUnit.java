package Equipe45.domain;

public enum MeasurementUnit {
    MILLIMETER("mm", 1.0),
    INCH("pouce", 25.4);

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

    public static double parseValue(String input) {
        try {
            if (input.contains(" ")) {
                String[] parts = input.split(" ", 2);
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid mixed number format: " + input);
                }
                double wholeNumber = Double.parseDouble(parts[0]);
                double fraction = parseFraction(parts[1]);
                return wholeNumber + fraction;
            }
            else if (input.contains("/")) {
                double fraction = parseFraction(input);
                return fraction;
            } else {
                double value = Double.parseDouble(input);
                return value;
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + input, e);
        }
    }

    private static double parseFraction(String fraction) {
        String[] parts = fraction.split("/");
        if (parts.length == 2) {
            double numerator = Double.parseDouble(parts[0]);
            double denominator = Double.parseDouble(parts[1]);
            return numerator / denominator;
        } else {
            return Double.parseDouble(fraction);
        }
    }


    public int toMillimeters(int value) {
        return  Double.valueOf((Integer.valueOf(value).doubleValue()  * conversionToMillimeter)).intValue();
    }

    public int toMillimetersWithFloat(float value) {
        return  Double.valueOf(value  * conversionToMillimeter).intValue();
    }

    public int toMillimeters(String input) {
        double parsedValue = parseValue(input);
        return Double.valueOf(parsedValue * conversionToMillimeter).intValue();
    }

    public float toMillimetersFloat(String input) {
        double parsedValue = parseValue(input);
        return Double.valueOf(parsedValue * conversionToMillimeter).floatValue();
    }

    public double fromMillimeters(double value) {
        return value / conversionToMillimeter;
    }

    public double toInches(double value) {
        return value / INCH.getConversionToMillimeter();
    }

    public int toInchesInt(double value) {
        return (int) (value / INCH.getConversionToMillimeter());
    }

}