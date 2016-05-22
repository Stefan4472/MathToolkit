import java.io.File;
import java.util.HashMap;

/**
 * Created by lhscompsci on 3/24/16.
 */
public class Main {

    public static void main(String[] args) {
        EquationParser parser = new EquationParser();
        System.out.println("Welcome to the Math Toolkit!");
        // check preferences to see if a save file for variables has been set
        File save_file = PersistentData.getStorageFilePath();
        if (save_file == null) {
            // set up variable storage
            
        } else {
            // attempt to read in MathObjects from save file
            HashMap<String, MathObject> variables = new HashMap<>();
            if (PersistentData.importMathObjects(save_file, variables)) {
                System.out.println(variables.size() + " variables imported successfully from " + save_file.getPath());
                parser.setVariables(variables);
            } else {
                System.out.println("Error importing variables from " + save_file.getPath() + ". The file did not exist or could not be opened");
            }
        }
        //String equation = "5+(2+4*(2+3))*(6*8)"; // answer: 1061
        //String equation = "5+3*cos(5-5)+3*sin(zero)";
        String equation = "3^(1+-2)";
        //String equation = "-5*-2";
        System.out.println(parser.evaluateExpression(equation)); // todo: make non-static: variables can be changed
    }
}