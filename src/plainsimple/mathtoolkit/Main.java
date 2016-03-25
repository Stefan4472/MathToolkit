import com.sun.org.apache.xpath.internal.operations.Div;

import java.util.*;

/**
 * Created by lhscompsci on 3/24/16.
 */
public class Main {

    private static final int ADD_OPERATION = 1;
    private static final int SUBTRACT_OPERATION = 2;
    private static final int MULTIPLY_OPERATION = 3;
    private static final int DIVIDE_OPERATION = 4;
    private static final int EXPONENT_OPERATION = 5;

    public static void main(String[] args) {
        String equation = "(2+4*(2+3))*(6*8)"; // answer: 1056
        // list of regex patterns to look for/match
        HashMap<String, Integer> operations = new HashMap<>();
        operations.put("+", ADD_OPERATION);
        operations.put("-", SUBTRACT_OPERATION);
        operations.put("*", MULTIPLY_OPERATION);
        operations.put("/", DIVIDE_OPERATION);
        operations.put("^", EXPONENT_OPERATION);
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
            } else if (equation.charAt(i) == ' ') { // ignore spaces

            } else { // parse out numbers and operators
                String token = getToken(equation, i);
                System.out.println("Token is: " + token);
                i += token.length() - 1;
            }
        }
    }

    // starting at startIndex of equation, looks ahead and parses out
    // the next complete token and returns it
    private static String getToken(String equation, int startIndex) {
        String token = "";
        // establish whether we will be parsing a number or not
        boolean is_number = isPartOfNumber(equation.charAt(startIndex));
        for (int j = startIndex; j < equation.length(); j++) {
            if (isPartOfNumber(equation.charAt(j)) == is_number && equation.charAt(j) != '(') { // continue collecting token
                token += equation.charAt(j);
            } else {
                return token;
            }
        }
        return token;
    }

    // returns whether char is a digit or decimal point
    private static boolean isPartOfNumber(char c) {
        return (c >= '0' && c <= '9') || c == '.';
    }
}