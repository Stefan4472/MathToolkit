/**
 * Generic MathObject, representing any object that can be used in the program.
 * Contains abstract methods for child classes to implement a core of operations
 * as well as static methods to apply operations to two MathObjects.
 */
public abstract class MathObject {

    protected String id;

    // constructs MathObject with given ID
    public MathObject(String id) {
        this.id = id;
    }

    // constructs MathObject and leaves id field empty
    public MathObject() {
        this.id = "";
    }

    public static MathObject parseMathObject(String s) throws NumberFormatException {
        try {
            return Number.parseNumber(s);
        } catch (NumberFormatException e) {}
        throw new NumberFormatException("String " + s + " cannot be parsed");
    }

    // adds MathObjects and returns result as a new MathObject
    public abstract MathObject add(MathObject toAdd);

    // subtracts MathObjects and returns result as a new MathObject
    public abstract MathObject subtract(MathObject toSubtract);

    // multiplies MathObjects and returns result as a new MathObject
    public abstract MathObject multiply(MathObject toMultiply);

    // divides MathObjects and returns result as a new MathObject
    public abstract MathObject divide(MathObject toDivide);

    // returns absolute value or magnitude of MathObject as new MathObject
    public abstract MathObject absOrMag();

    // todo: other operations, exponent
    public abstract MathObject powerOf(MathObject exponent);

    // returns nagative of MathObject as new MathObject
    public abstract MathObject negative();

    // adds two MathObjects
    public static MathObject add(MathObject obj1, MathObject obj2) {
        if (obj1 instanceof Number) {
            return ((Number) obj1).add(obj2);
        }
        return null;
    }

    // subtracts two MathObjects
    public static MathObject subtract(MathObject obj1, MathObject obj2) {
        if (obj1 instanceof Number) {
            return ((Number) obj1).subtract(obj2);
        }
        return null;
    }

    // multiplies two MathObjects
    public static MathObject multiply(MathObject obj1, MathObject obj2) {
        if (obj1 instanceof Number) {
            return ((Number) obj1).multiply(obj2);
        }
        return null;
    }

    // divides two MathObjects
    public static MathObject divide(MathObject obj1, MathObject obj2) {
        if (obj1 instanceof Number) {
            return ((Number) obj1).divide(obj2);
        }
        return null;
    }

    // applies cosine function to given MathObject
    public static MathObject cos(MathObject object) { // todo: check if result will be a pure integer
        Number num = (Number) object;
        return new Number(Math.cos(num.getValue()));
    }

    // applies inverse cosine function to given MathObject
    public static MathObject acos(MathObject object) {
        Number num = (Number) object;
        return new Number(Math.acos(num.getValue()));
    }

    // applies sine function to given MathObject
    public static MathObject sin(MathObject object) {
        Number num = (Number) object;
        return new Number(Math.sin(num.getValue()));
    }

    // applies inverse sine function to given MathObject
    public static MathObject asin(MathObject object) {
        Number num = (Number) object;
        return new Number(Math.asin(num.getValue()));
    }

    // applies tangent function to given MathObject
    public static MathObject tan(MathObject object) {
        Number num = (Number) object;
        return new Number(Math.tan(num.getValue()));
    }

    // applies inverse tangent function to given MathObject
    public static MathObject atan(MathObject object) {
        Number num = (Number) object;
        return new Number(Math.atan(num.getValue()));
    }

    // toString method for output
    @Override
    public abstract String toString();
}
