package plainsimple;
import java.util.HashMap;

/**
 * Created by lhscompsci on 3/24/16.
 */
public class Main {

    public static void main(String[] args) {
        //String equation = "5+(2+4*(2+3))*(6*8)"; // answer: 1061
        String equation = "5+3*cos(5-5)+3*sin(zero)";
        //String equation = "3^(1+2)";
        HashMap<String, MathObject> variables = new HashMap<>();
        variables.put("zero", new Number(0));
        EquationParser parser = new EquationParser(variables);
        System.out.println(parser.evaluateExpression(equation)); // todo: make non-static: variables can be changed
    }
}