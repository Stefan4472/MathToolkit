import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Allows for parsing between Strings and values and keeps track of significant figures
 */
public class Number extends MathObject {

    // stored value of the number
    private double value;
    // whether Number represents an integer or not (i.e. a decimal)
    private boolean isInt;
    // decimal format--round to 10 places
    private static DecimalFormat decimalFormat = getDecimalFormat();

    private static DecimalFormat getDecimalFormat() {
        DecimalFormat format = new DecimalFormat("#.###############");
        format.setRoundingMode(RoundingMode.CEILING);
        return format;
    }

    public double getValue() {
        return value;
    }

    public boolean isInt() {
        return isInt;
    }

    // construct Number from an integer
    public Number(int value) {
        this.value = value;
        isInt = true;
    }

    // construct Number from a double
    public Number(double value) {
        this.value = value;
        isInt = false;
    }

    public Number(double value, boolean isInt) {
        this.value = value;
        this.isInt = isInt;
    }

    // parses a Number from String and returns the initialized Number object
    public static Number parseNumber(String s) throws NumberFormatException { // todo: support scientific notation
        int decimal_place = 0;
        boolean decimal_found = false;
        boolean negative_found = false;
        double value = 0;
        // handle case string is in the form "Number()"
        if (s.startsWith("Number(") && s.endsWith(")")) {
            s = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                if (!decimal_found) { // before decimal
                    value = value * 10.0 + Character.getNumericValue(s.charAt(i));
                } else {
                    value = value + Character.getNumericValue(s.charAt(i)) * Math.pow(10, -decimal_place);
                    decimal_place++;
                }
            } else if (s.charAt(i) == '.') {
                if (decimal_found) {
                    throw new NumberFormatException("String to parse contains two decimal points");
                } else {
                    decimal_found = true;
                    decimal_place = 1;
                }
            } else if (s.charAt(i) == '-') {
                if (negative_found) {
                    throw new NumberFormatException("String to parse contains two negative signs");
                } else {
                    negative_found = true;
                }
            } else {
                throw new NumberFormatException("String to parse contains non-numeric characters");
            }
        }
        if (negative_found) {
            value *= -1;
        }
        if (decimal_found) {
            return new Number(value);
        } else {
            return new Number((int) value);
        }
    }

    @Override
    public MathObject add(MathObject toAdd) throws IllegalArgumentException { // todo: error-handling, what if it's not an instance of Number?
        if (toAdd instanceof Number) {
            Number toAdd_cast = (Number) toAdd;
            double result = getValue() + toAdd_cast.getValue();
            boolean result_isInt = (isInt() && toAdd_cast.isInt() ? true : false);
            return new Number(result, result_isInt);
        } else {
            throw new IllegalArgumentException("Objects are of different types and cannot be added");
        }
    }

    @Override
    public MathObject subtract(MathObject toSubtract) throws IllegalArgumentException {
        if (toSubtract instanceof Number) {
            Number toSubtract_cast = (Number) toSubtract;
            double result = getValue() - toSubtract_cast.getValue();
            boolean result_isInt = (isInt() && toSubtract_cast.isInt() ? true : false);
            return new Number(result, result_isInt);
        } else {
            throw new IllegalArgumentException("Objects are of different types and cannot be subtracted");
        }
    }

    @Override
    public MathObject multiply(MathObject toMultiply) throws IllegalArgumentException {
        if (toMultiply instanceof Vector) {
            return toMultiply.multiply(this);
        } else if (toMultiply instanceof Number) {
            Number toMultiply_cast = (Number) toMultiply;
            double result = getValue() * toMultiply_cast.getValue();
            boolean result_isInt = (result == Math.floor(result) ? true : false);
            return new Number(result, result_isInt);
        } else {
            throw new IllegalArgumentException("Objects cannot be multiplied");
        }
    }

    @Override
    public MathObject divide(MathObject toDivide) throws IllegalArgumentException {
        if (toDivide instanceof Number) {
            Number toDivide_cast = (Number) toDivide;
            double result = getValue() / toDivide_cast.getValue();
            boolean result_isInt = (result == Math.floor(result) ? true : false);
            return new Number(result, result_isInt);
        } else {
            throw new IllegalArgumentException("Objects are of different types and cannot be divided");
        }
    }

    @Override
    public MathObject absOrMag() {
        return new Number(Math.abs(getValue()), isInt());
    }

    @Override
    public MathObject powerOf(MathObject exponent) throws IllegalArgumentException {
        if (exponent instanceof Number) {
            Number toExponent_cast = (Number) exponent;
            double result = Math.pow(getValue(), toExponent_cast.getValue());
            boolean result_isInt = (result == Math.floor(result));
            return new Number(result, result_isInt);
        } else {
            throw new IllegalArgumentException("Objects are of different types and do not support exponents");
        }
    }

    @Override
    public MathObject negative() {
        return new Number(getValue() * -1.0, isInt());
    }

    public MathObject square() {
        return multiply(this);
    }

    public MathObject sqrt() {
        double result = Math.sqrt(this.getValue());
        // check if result is an int
        if (result == Math.floor(result)) {
            return new Number(result, true);
        } else {
            return new Number(result, false);
        }
    }

    @Override
    public String toString() {
        if (isInt) {
            return Integer.toString((int) value);
        } else {
            return decimalFormat.format(value);
        }
    }

    public static String toString(Number number) {
        return number.toString();
    }
}
