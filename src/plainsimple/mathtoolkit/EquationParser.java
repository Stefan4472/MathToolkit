import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lhscompsci on 3/31/16.
 */
public class EquationParser {

    private static List<String> operations = createOperationsMap();
    private static HashMap<String, Integer> functions = createFunctionsmap();

    private static final int ADD_OPERATION = 1;
    private static final int SUBTRACT_OPERATION = 2;
    private static final int MULTIPLY_OPERATION = 3;
    private static final int DIVIDE_OPERATION = 4;
    private static final int EXPONENT_OPERATION = 5;

    private static final int ABS_OPERATION = 1;
    private static final int COS_OPERATION = 2;

    // initializes and returns a HashMap containing the operations
    private static ArrayList<String> createOperationsMap() {
        // list of regex patterns to look for/match
        ArrayList<String> operations = new ArrayList<>();
        operations.add("+");
        operations.add("-");
        operations.add("*");
        operations.add("/");
        operations.add("^");
        return operations;
    }

    // initializes and returns a HashMap containing the functions
    private static HashMap<String, Integer> createFunctionsmap() {
        // list of regex patters to look for/match
        HashMap<String, Integer> functions = new HashMap<>();
        functions.put("abs(.+)", ABS_OPERATION);
        functions.put("cos(.+)", COS_OPERATION);
        return functions;
    }

    // moves from left to right, evaluating expressions
    // when it runs into a parenthesis, it looks for the whole parenthesis
    // and calls evaluateParenthesis() on the expression inside the parenthesis
    // The evaluated amount in the parenthesis is then plugged into the equation
    // Runs to the end of the equation
    public static String evaluateExpression(String equation) {
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
                // parenthesis starts at i and ends at i + j
                equation = equation.substring(0, i) + evaluateExpression(equation.substring(i + 1, i + j)) + equation.substring(i + j + 1);
            }
        }
        System.out.println("Equation is now " + equation);
        return reduceExpression(equation);
    }

    private static String reduceExpression(String expression) {
        LinkedList<String> tokens = new LinkedList<>();
        for (int i = 0; i < expression.length(); i++) {
            String next_token = getToken(expression, i);
            i += next_token.length() - 1;
            tokens.add(next_token);
        }
        return evaluateTokens(tokens);
    }

    private static String evaluateTokens(LinkedList<String> tokens) {
        // should be in form:
        // number, operator, number, operator, number, etc.
        while (tokens.size() > 1) {
            // order of operations
            int operation_index = 1;
            if (tokens.contains("*")) { // todo: ^
                operation_index = tokens.indexOf("*");
            } else if (tokens.contains("/")) {
                operation_index = tokens.indexOf("/");
            } else if (tokens.contains("+")) {
                operation_index = tokens.indexOf("+");
            } else if (tokens.contains("-")) {
                operation_index = tokens.indexOf("-");
            } // todo: else: invalid operator
            tokens.add(operation_index - 1, applyOperation(tokens.remove(operation_index - 1), tokens.remove(operation_index - 1), tokens.remove(operation_index - 1)));
        }
        System.out.println("Result is " + tokens.getFirst());
        return tokens.getFirst();
    }

    private static String applyOperation(String token1, String operation, String token2) {
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

    // applies function
    // cos(3+4) -> function = "cos" args = "3+4"
    // returns token of evaluated function
    private static String applyFunction(String function, String args) {
        return "";
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
