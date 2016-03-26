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
        System.out.println(evaluateExpression(equation, operations));
    }

    // moves from left to right, evaluating expressions
    // when it runs into a parenthesis, it looks for the whole parenthesis
    // and calls evaluateParenthesis() on the expression inside the parenthesis
    // The evaluated amount in the parenthesis is then plugged into the equation
    // Runs to the end of the equation
    private static String evaluateExpression(String equation, HashMap<String, Integer> operations) {
        System.out.println(equation);
        LinkedList<String> tokens = new LinkedList<>();
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
                if (tokens.size() == 0) {
                    return evaluateExpression(equation.substring(i + 1, i + j), operations);
                } else {
                    String binding_operation = tokens.pollLast();
                    String current_result = evaluateTokens(tokens, operations);
                    System.out.println("Current Result = " + current_result + " and binding operation = " + binding_operation);
                    //i += j;
                    // todo: might not go to end of equation
                    return applyOperation(current_result, binding_operation, evaluateExpression(equation.substring(i + 1, i + j), operations), operations);
                }
            } else if (equation.charAt(i) == ' ') { // ignore spaces

            } else { // parse out numbers and operators
                tokens.add(getToken(equation, i));
                System.out.println("Token is: " + tokens.getLast());
                i += tokens.getLast().length() - 1;
            }
        }
        return evaluateTokens(tokens, operations);
    }

    private static String evaluateTokens(LinkedList<String> tokens, HashMap<String, Integer> operations) {
        // should be in form:
        // number, operator, number, operator, number, etc.
        while (tokens.size() > 1) {
            System.out.println("In loop and Tokens.size() = " + tokens.size());
            tokens.addFirst(applyOperation(tokens.pollFirst(), tokens.pollFirst(), tokens.pollFirst(), operations));
            System.out.println("Operation applied and Tokens.size() = " + tokens.size());
        }
        System.out.println("Result is " + tokens.getFirst());
        return tokens.getFirst();
    }

    private static String applyOperation(String token1, String operation, String token2, HashMap<String, Integer> operations) {
        double num_1 = 0, num_2 = 0;
        try { // todo: check constants
            num_1 = Double.parseDouble(token1);
            num_2 = Double.parseDouble(token2);
        } catch (NumberFormatException e) {
            throw e;
        }
        switch (operations.get(operation)) {
            case ADD_OPERATION:
                return Double.toString(num_1 + num_2);
            case SUBTRACT_OPERATION:
                return Double.toString(num_1 - num_2);
            case MULTIPLY_OPERATION:
                return Double.toString(num_1 * num_2);
            case DIVIDE_OPERATION:
                return Double.toString(num_1 / num_2);
            case EXPONENT_OPERATION:
                return Double.toString(Math.pow(num_1, num_2));
            default:
                return "0";
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