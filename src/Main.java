import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by lhscompsci on 3/24/16.
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static boolean saveVariables = false;

    public static void main(String[] args) {
        EquationParser parser = new EquationParser();
        System.out.println("Welcome to the Math Toolkit!\n");
        File save_file = importSavedVariables(parser);
        System.out.println("Waiting on expression or command...");
        while (true) {
            String user_input = scanner.nextLine();
            if (user_input.startsWith("help")) {

            } else if (user_input.startsWith("exit")) {
                // write variables to save file
                if (saveVariables) {
                    PersistentData.exportMathObjects(save_file, parser);
                }
            } else if (user_input.startsWith("config")) {

            } else {
                String result = parser.evaluateExpression(user_input);
                System.out.print(">> " + result + "\n");

            }
        }
        // todo: Look at arithmetic issues, check lots of digits being created
        //String equation = "5+(2+4*(2+3))*(6*8)"; // answer: 1061
        //String equation = "5+3*cos(5-5)+3*sin(zero)";
        //String equation = "3^(1+-2)";
        //String equation = "-5*-2";
        //String equation = "b+4";
        //System.out.println(parser.evaluateExpression(equation));
    }

    // check preferences to see if a save file for variables has been set
    // if so, attempt to read in the variables
    // if not, try to set up a file
    private static File importSavedVariables(EquationParser toInitialize) {
        File save_file = PersistentData.getStorageFilePath();
        if (save_file == null) {
            // set up variable storage
            System.out.print("No save file was found. Create a save file to store any created variables? (y/n)");
            if (scanner.next().equals("y")) {
                save_file = createSaveFile();
                PersistentData.setStorageFilePath(save_file);
                saveVariables = true;
            } else {
                System.out.println("A save file can always be specified at startup or using the command "); // todo: command
            }
        }
        if (save_file != null) {
            // attempt to read in MathObjects from save file
            if (PersistentData.importMathObjects(save_file, toInitialize)) {
                System.out.println(toInitialize.getVariables().size() + " variables imported successfully from " + save_file.getPath());
                saveVariables = true;
            } else {
                System.out.println("Error importing variables from " + save_file.getPath() + ". The file did not exist or could not be opened");
                System.out.print("Create new save file? (y/n)");
                if (scanner.next().equals("y")) {
                    save_file = createSaveFile();
                    saveVariables = true;
                } else {
                    System.out.println("A save file can always be specified at startup or using the command ");
                    saveVariables = false;
                }
            }
        }
        return save_file;
    }

    // prompts user to create or set a file to save variables to
    private static File createSaveFile() {
        File save_file;
        boolean keep_looping;
        do {
            System.out.print("Enter file path to create new save file: ");
            save_file = new File(scanner.next());
            if (save_file.exists()) {
                System.out.print("The specified file already exists. The file may be overwritten if you continue. Use anyway? (y/n)");
                if (scanner.next().equals("n")) {
                    keep_looping = true;
                } else {
                    keep_looping = false;
                }
            } else {
                keep_looping = false;
            }
        } while (keep_looping);
        return save_file;
    }
}