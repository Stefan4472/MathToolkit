import java.util.*;

/**
 * Created by lhscompsci on 3/31/16.
 */
public class EquationParser {

    // list of supported functions, by keyword
    private List<String> functions;
    // map of stored variables with variable ID as key and MathObject as value
    private HashMap<String, MathObject> variables;

    // construct with a hashmap containing stored variables
    public EquationParser(HashMap<String, MathObject> variables) {
        this.variables = variables;
        functions = createFunctionsList();
    }
    // initializes and returns an ArrayList containing the functions
    private static ArrayList<String> createFunctionsList() {
        // list of function keywords that bind to actual functions
        ArrayList<String> functions = new ArrayList<>();
        functions.add("abs");
        functions.add("cos");
        functions.add("acos");
        functions.add("sin");
        functions.add("asin");
        functions.add("tan");
        functions.add("atan");
        functions.add("sec");
        functions.add("asec");
        functions.add("csc");
        functions.add("acsc");
        functions.add("cot");
        functions.add("acot");
        return functions;
    }

    // moves from left to right, evaluating expressions
    // when it runs into a parenthesis, it looks for the whole parenthesis
    // and calls evaluateExpression() on the expression inside the parenthesis
    // The evaluated amount in the parenthesis is then plugged into the equation
    // Runs to the end of the equation
    public String evaluateExpression(String equation) {
        System.out.println(equation);
        for (int i = 0; i < equation.length(); i++) {
            if (equation.charAt(i) == '(') { // found a parenthesis - look for the whole expression inside
                // parenthesis will start at i and end at i + j
                int openings = 1, closings = 0, j = 0;
                while (openings != closings) {
                    j++;
                    if (equation.charAt(i + j) == '(') {
                        openings++;
                    } else if (equation.charAt(i + j) == ')') {
                        closings++;
                    }
                }
                // look behind to see if the parenthesis is preceded by a function call
                // in this case use applyFunction()
                if (i != 0 && functions.contains(getPreviousToken(equation, i - 1))) {
                    String function_name = getPreviousToken(equation, i - 1);
                    System.out.println("Found function " + function_name);
                    equation = equation.substring(0, i - function_name.length()) +
                            applyFunction(function_name, equation.substring(i + 1, i + j)) +
                            equation.substring(i + j + 1);
                } else {
                    equation = equation.substring(0, i) + evaluateExpression(equation.substring(i + 1, i + j)) + equation.substring(i + j + 1);
                }
            }
        }
        System.out.println("Equation is now " + equation);
        return reduceExpression(equation);
    }

    // used to reduce expressions where there are no parenthesis into a single token result
    // splits into tokens and evaluates them
    private String reduceExpression(String expression) {
        LinkedList<String> tokens = new LinkedList<>();
        for (int i = 0; i < expression.length(); i++) {
            String next_token = getToken(expression, i);
            i += next_token.length() - 1;
            tokens.add(next_token);
        }
        return evaluateTokens(tokens);
    }

    // takes a list of tokens and applies operations using order of operations
    private String evaluateTokens(LinkedList<String> tokens) throws ArithmeticException {
        while (tokens.size() > 1) {
            System.out.println("Tokens are " + ListUtil.toString(tokens));
            // handle case of first token being negative
            if (tokens.get(0).equals("-")) {
                tokens.set(1, "-" + tokens.get(1));
                tokens.remove(0);
            }
            if (tokens.size() == 1) {
                return tokens.getFirst();
            } else {
                // order of operations
                int operation_index = 1;
                if (tokens.contains("*")) { // todo: ^
                    operation_index = tokens.indexOf("*");
                } else if (tokens.contains("/")) {
                    operation_index = tokens.indexOf("/");
                } else if (tokens.contains("+")) {
                    operation_index = tokens.indexOf("+");
                } else if (tokens.contains("^")) {
                    operation_index = tokens.indexOf("^");
                } else if (tokens.contains("-")) {
                    operation_index = tokens.indexOf("-");
                } else {
                    throw new ArithmeticException("Expression does not contain a valid operator");
                }
                System.out.println("Operation index is " + operation_index);
                System.out.println("Next token is " + tokens.get(operation_index + 1));
                // look for negative sign after first operation found:
                // remove negative sign and add negative sign to beginning of next-next token
                if (tokens.get(operation_index + 1).equals("-")) {
                    System.out.println("Negative Found");
                    tokens.set(operation_index + 1, "-" + tokens.get(operation_index + 2));
                    tokens.remove(operation_index + 2);
                }
                System.out.println("Tokens are now " + ListUtil.toString(tokens));
                String result = applyOperation(tokens.remove(operation_index - 1), tokens.remove(operation_index - 1), tokens.remove(operation_index - 1));
                System.out.println("Result is " + result);
                // should be in form:
                // number, operator, number, operator, number, etc.
                tokens.add(operation_index - 1, result);
            }
        }
        System.out.println("Result is " + tokens.getFirst());
        return tokens.getFirst();
    }

    // applies the given operation to two tokens
    private String applyOperation(String token1, String operation, String token2) {
        MathObject obj_1, obj_2;
        boolean obj1_negative = false, obj2_negative = false;
        if (token1.charAt(0) == '-') { // todo: handle negatives
            token1 = token1.substring(1);
            obj1_negative = true;
        }
        if (token2.charAt(0) == '-') {
            token2 = token2.substring(1);
            obj2_negative = true;
        }
        if (variables.containsKey(token1)) {
            obj_1 = variables.get(token1);
        } else {
            obj_1 = MathObject.parseMathObject(token1);
        }
        if (variables.containsKey(token2)) {
            obj_2 = variables.get(token2);
        } else {
            obj_2 = MathObject.parseMathObject(token2);
        }
        if (obj1_negative) {
            obj_1 = obj_1.negative();
        }
        if (obj2_negative) {
            obj_2 = obj_2.negative();
        }
        if (operation.equals("+")) {
            return (obj_1.add(obj_2)).toString();
        } else if (operation.equals("-")) {
            return (obj_1.subtract(obj_2)).toString();
        } else if (operation.equals("*")) {
            return (obj_1.multiply(obj_2)).toString();
        } else if (operation.equals("/")) {
            return (obj_1.divide(obj_2)).toString();
        } else if (operation.equals("^")) { // todo: check if this is correct: 3^-1 = 0 ??
            return (obj_1.powerOf(obj_2)).toString();
        } else { // todo: throw exception?
            System.out.println("Invalid Operator " + operation);
            return "";
        }
    }

    // applies function
    // cos(3+4) -> function = "cos" args = "3+4"
    // returns token of evaluated function
    private String applyFunction(String function, String content) {
        // break apart any comma-separated arguments, evaluating each one in succession
        // and parsing to MathObject
        // this ensures each argument is simplified and allows nested operations to occur
        List<MathObject> args = new ArrayList();
        while (content.contains(",")) {
            String next_arg = evaluateExpression(content.substring(0, content.indexOf(",")));
            if (variables.containsKey(next_arg)) {
                args.add(variables.get(next_arg));
            } else {
                args.add(MathObject.parseMathObject(next_arg));
            }
            content = content.substring(content.indexOf(",") + 1);
        }
        if (variables.containsKey(content)) {
            args.add(variables.get(content)); // todo: messy code
        } else {
            args.add(MathObject.parseMathObject(evaluateExpression(content)));
        }
        // apply the function to the arguments
        switch (function) {
            case "abs":
                return (args.get(0).absOrMag()).toString();
            case "cos":
                return (MathObject.cos(args.get(0))).toString();
            case "acos":
                return (MathObject.acos(args.get(0))).toString();
            case "sin":
                return (MathObject.sin(args.get(0))).toString();
            case "asin":
                return (MathObject.asin(args.get(0))).toString();
            case "tan":
                return (MathObject.tan(args.get(0))).toString();
            case "atan":
                return (MathObject.atan(args.get(0))).toString();
            case "sec":
                return ((new Number(1.0)).divide(MathObject.cos(args.get(0)))).toString();
            case "asec":
                return ((new Number(1.0)).divide(MathObject.acos(args.get(0)))).toString();
            case "csc":
                return ((new Number(1.0)).divide(MathObject.sin(args.get(0)))).toString();
            case "acsc":
                return ((new Number(1.0)).divide(MathObject.asin(args.get(0)))).toString();
            case "cot":
                return ((new Number(1.0)).divide(MathObject.tan(args.get(0)))).toString();
            case "acot":
                return ((new Number(1.0)).divide(MathObject.atan(args.get(0)))).toString();
            case "setEqual":
                // create or set variable

        }
        return "";
    }

    // starting at startIndex of equation parses out
    // the next complete token and returns it
    // if lookBehind is true, it will look backwards (the token however will not be backwards)
    // if lookBehind is false, it will look forwards =
    private String getToken(String equation, int startIndex) { // todo: negative numbers
        String token = Character.toString(equation.charAt(startIndex));
        // establish whether we will be parsing a number or not
        boolean is_number = isPartOfNumber(equation.charAt(startIndex));
        for (int j = startIndex + 1; j < equation.length(); j++) {
            if (equation.charAt(j) == '+' || equation.charAt(j) == '-' || equation.charAt(j) == '*'
                    || equation.charAt(j) == '/' || equation.charAt(j) == '*') {
                return token; // stop if an operator has been found
            } else if (isPartOfNumber(equation.charAt(j)) == is_number && equation.charAt(j) != '(') { // continue collecting token
                token += equation.charAt(j);
                if (functions.contains(token)) {
                    return token;
                }
            } else {
                return token;
            }
        }
        return token;
    }

    // starting at startIndex of equation, looks behind and parses out
    // the previous complete token and returns it
    private String getPreviousToken(String equation, int startIndex) { // todo: return instantly when you hit an operator
        String token = Character.toString(equation.charAt(startIndex));
        // establish whether we will be parsing a number or not
        boolean is_number = isPartOfNumber(equation.charAt(startIndex));
        for (int j = startIndex - 1; j >= 0; j--) {
            if (equation.charAt(j) == '+' || equation.charAt(j) == '-' || equation.charAt(j) == '*'
                    || equation.charAt(j) == '/' || equation.charAt(j) == '*') {
                return token; // stop if an operator has been found
            } else if (isPartOfNumber(equation.charAt(j)) == is_number && equation.charAt(j) != '(') { // continue collecting token
                token = equation.charAt(j) + token;
                if (functions.contains(token)) { // todo
                    return token;
                }
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
