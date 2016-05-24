import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhscompsci on 4/26/16.
 */
public class Vector extends MathObject {

    private List<Number> elements;

    public Vector() {
        elements = new ArrayList<>();
    }

    public static Vector initVector(List<MathObject> elements) {
        Vector new_vector = new Vector();
        for (MathObject o : elements) {
            new_vector.addElement(o);
        }
        return new_vector;
    }

    public static Vector parseVector(String toParse) throws NumberFormatException {
        // handle case string is in the form "Vector()"
        if (toParse.startsWith("Vector(") && toParse.endsWith(")")) {
            toParse = toParse.substring(toParse.indexOf("(") + 1, toParse.indexOf(")"));
        }
        Vector parsed = new Vector();
        while (toParse.contains(",")) {
            parsed.addElement(Number.parseNumber(toParse.substring(0, toParse.indexOf(","))));
            toParse = toParse.substring(toParse.indexOf(",") + 1);
        }
        parsed.addElement(Number.parseNumber(toParse));
        return parsed;
    }

    public void addElement(MathObject element) {
         elements.add((Number) element);
    }

    public Number getElement(int index) { // todo: Better Exceptions
        return elements.get(index);
    }

    public List<Number> getElements() {
        return elements;
    }

    public void setElement(int index, Number value) {
        elements.set(index, value);
    }

    public int size() {
        return elements.size();
    }

    @Override
    public MathObject add(MathObject toAdd) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (toAdd instanceof Vector) {
            Vector to_add = (Vector) toAdd;
            if (size() != to_add.size()) {
                throw new IndexOutOfBoundsException("Vectors must be the same size in order to add");
            } else {
                Vector sum = new Vector();
                for (int i = 0; i < size(); i++) {
                    sum.addElement(getElement(i).add(to_add.getElement(i)));
                }
                return sum;
            }
        } else {
            throw new IllegalArgumentException("Objects are of different types and cannot be added");
        }
    }

    @Override
    public MathObject subtract(MathObject toSubtract) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (toSubtract instanceof Vector) {
            Vector to_subtract = (Vector) toSubtract;
            if (size() != to_subtract.size()) {
                throw new IndexOutOfBoundsException("Vectors must be the same size in order to subtract");
            } else {
                Vector diff = new Vector();
                for (int i = 0; i < size(); i++) {
                    diff.addElement(getElement(i).subtract(to_subtract.getElement(i)));
                }
                return diff;
            }
        } else {
            throw new IllegalArgumentException("Objects are of different types and cannot be subtracted");
        }
    }

    @Override
    public MathObject multiply(MathObject toMultiply) throws IllegalArgumentException {
        if (toMultiply instanceof Vector) {
            Vector to_multiply = (Vector) toMultiply;
            MathObject dot_product = new Number(0);
            for (int i = 0; i < size(); i++) {
                dot_product = dot_product.add(getElement(i).multiply(to_multiply.getElement(i)));
            }
            return dot_product;
        } else if (toMultiply instanceof Number) {
            Number to_multiply = (Number) toMultiply;
            Vector product = new Vector();
            for (int i = 0; i < size(); i++) {
                product.addElement(getElement(i).multiply(to_multiply));
            }
            return product;
        } else {
            throw new IllegalArgumentException("Vector must multiply another vector or a scalar");
        }

    }

    @Override
    public MathObject divide(MathObject toDivide) throws IllegalArgumentException {
        if (toDivide instanceof Number) {
            Number to_divide = (Number) toDivide;
            Vector result = new Vector();
            for (Number element : elements) {
                result.addElement(element.divide(to_divide));
            }
            return result;
        } else {
            throw new IllegalArgumentException("Objects are of different types and cannot be divided");
        }
    }

    @Override // Vector magnitude
    public MathObject absOrMag() {
        Number result = new Number(0);
        for (Number element : elements) {
            result.add(element.square());
        }
        return result.sqrt();
    }

    @Override
    public MathObject powerOf(MathObject exponent) throws NullPointerException {
        throw new NullPointerException("Vectors do not support exponents");
    }

    @Override
    public MathObject negative() {
        Vector negative = new Vector();
        for (Number element : elements) {
            negative.addElement(element.negative());
        }
        return negative;
    }

    @Override
    public String toString() {
        String as_string = "Vector(";
        for (int i = 0; i < elements.size() - 1; i++) {
            as_string += elements.get(i).toString() + ",";
        }
        as_string += elements.get(elements.size() - 1) + ")";
        return as_string;
    }
}
