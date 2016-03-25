import java.util.*;

/**
 * Created by lhscompsci on 3/24/16.
 */
public class Main {


    public static void main(String[] args) {
        String equation = "(2+4*(2+3))*(6*8)"; // answer: 1056
        // list of regex patterns to look for/match
        List<String> operators = new ArrayList<String>();
        operators.add("+");
        operators.add("-");
        evaluateExpression(equation);
    }

    // moves from left to right, evaluating expressions
    // when it runs into a parenthesis, it looks for the whole parenthesis
    // and calls evaluateParenthesis() on the expression inside the parenthesis
    // The evaluated amount in the parenthesis is then plugged into the equation
    // Runs to the end of the equation
    private static void evaluateExpression(String equation) {
        System.out.println(equation);
        for (int i = 0; i < equation.length(); i++) {
            if (equation.charAt(i) == '(') { // found a parenthesis - look for the whole expression inside
                int openings = 1, closings = 0, j = 0;
                while (openings != closings) {
                    j++;
                    if (equation.charAt(i + j) == '(') {
                        openings++;
                    } else if (equation.charAt(i + j) == ')') {
                        closings++;
                    }
                }
                evaluateExpression(equation.substring(i + 1, i + j));
                i += j;
            }
        }
    }

    // returns result of stuff within least deep parenthesis
    private static double evaluateParenthesis(String expression) {
        System.out.println(expression);
        return 0;
    }
}
