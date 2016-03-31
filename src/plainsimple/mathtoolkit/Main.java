/**
 * Created by lhscompsci on 3/24/16.
 */
public class Main {

    public static void main(String[] args) {
        String equation = "5+(2+4*(2+3))*(6*8)"; // answer: 1061
        System.out.println(EquationParser.evaluateExpression(equation));
    }
}
