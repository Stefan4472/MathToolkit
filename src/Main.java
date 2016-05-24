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
            if (user_input.equals("help")) {
                runHelp();
            } else if (user_input.equals("exit")) {
                // write variables to save file
                if (saveVariables) {
                    PersistentData.exportMathObjects(save_file, parser);
                }
                System.out.println("Exiting program...");
                System.exit(0);
            } else if (user_input.equals("config")) {
                runConfig();
            } else if (user_input.equals("about")) {
                printAbout();
            } else {
                try {
                    String result = parser.evaluateExpression(user_input);
                    System.out.print(">> " + result + "\n");
                } catch (Exception e) {
                    System.out.println(">> An error occurred");
                    e.printStackTrace();
                }

            }
        }
        // todo: issues with user input "y" "n", numberformatexceptions in Number and Vector classes
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
            String user_input = scanner.nextLine();
            if (user_input.equals("y")) {
                save_file = createSaveFile();
                PersistentData.setStorageFilePath(save_file);
                saveVariables = true;
            } else {
                System.out.println("A save file can always be specified at startup or using the command <config>");
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
                String user_input = scanner.nextLine();
                if (user_input.equals("y")) {
                    save_file = createSaveFile();
                    PersistentData.setStorageFilePath(save_file);
                    saveVariables = true;
                } else {
                    System.out.println("A save file can always be specified at startup or using the command <config>");
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
            save_file = new File(scanner.nextLine());
            if (save_file.exists()) {
                System.out.print("The specified file already exists. The file may be overwritten if you continue. Use anyway? (y/n)");
                String user_input = scanner.nextLine();
                if (user_input.equals("n")) {
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

    private static void runHelp() {
        System.out.print(
                "Available Commands:\n" +
                        "\thelp      Lists available commands and provides access to documentation\n" +
                        "\tconfig    Allows user to view currently stored variables and configure the file where variables are saved\n" +
                        "\tabout     Prints information about the program\n" +
                        "\texit      Saves variables if a savefile is specified and exits the program\n\n" +
                        "Commands are case-sensitive. Read documentation? (y/n) "
        );
        String user_input = scanner.nextLine();
        if (user_input.equals("y")) {
            System.out.print(
                    "\n\nUsing the Toolkit:\n" +
                            "\tOnce the toolkit is running it will accept expressions or equations to evaluate. Simply\n" +
                            "\tenter the equation/expression and the program will output the result, or an error message\n" +
                            "\tif there was an issue evaluating the given expression. Expressions should follow standard\n" +
                            "\trules and are evaluated using Order of Operations.\n\n" +
                            "\tWhen exiting the program, please use the <exit> command. This ensures variables are saved before\n" +
                            "\tthe program closes.\n\n" +
                    "Using Variables:\n" +
                            "\tTo create a variable, enter [variable name] = [expression] while the program is running.\n" +
                            "\tThe program will evaluate the expression and assign the value to an object that will be\n" +
                            "\tstored under [variable name]. To access the value of the variable, simply use [variable name]\n" +
                            "\tin your expressions in the calculator.\n\n" +
                            "\tVariables can be saved to an external file so that they can be imported when the program runs again.\n" +
                            "\tThe first time the program runs, it prompts the user to specify a file name. The program will then create\n" +
                            "\ta file where it will store the variables. Storage is easy to understand: variables are stored in the form\n" +
                            "\t[variable name]=[value]. You may in fact edit the file yourself, but be careful to follow the form correctly.\n" +
                            "\tThe location of the save file can always be changed by using the <config> command and following the prompts.\n\n" +
                    "Using Numbers and Vectors:\n" +
                            "\tNumbers can be used by simply entering the value of a number: e.g. '123'\n" +
                            "\tNumbers can also be notated with the prefix 'Number(' and the suffix ')' e.g. 'Number(123)'\n" +
                            "\tVectors are notated with the prefix 'Vector(' and the suffix ')' and contain comma-separated\n\tvalues with the elements e.g. 'Vector(1,2,3)'\n"
            );
        } else {
            System.out.println("Waiting on equation or command...");
        }
    }

    private static void printAbout() {
        System.out.print(
                "                      MathToolkit by Stefan Kussmaul, May 2016\n" +
                        "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n" +
                        "The MathToolkit is an easy-to-use calculator that implements object-oriented design.\n" +
                        "  The Toolkit provides standard scalar functions as well as more advanced features \n" +
                        "such as Vectors.  It also provides functionality to define, store, and use variables\n" +
                        "                                in the calculator.\n\n" +
                        "       The MathToolkit is a free and open-source project. Code can be found at\n" +
                        "                    https://github.com/Stefan4472/MathToolkit.\n\n"
        );
    }

    private static void runConfig() {
        File save_file = PersistentData.getStorageFilePath();
        if (save_file == null) {
            System.out.println("There is no save file currently in use. That means any created variables will be lost when the program exits.\n");
            System.out.print("Create save file? (y/n) ");
            String user_input = scanner.nextLine();
            if (user_input.equals("y")) {
                createSaveFile();
            }
        } else {
            System.out.print("Current save file is " + save_file.getPath() + ". Create new save file? (y/n) ");
            String user_input = scanner.nextLine();
            if (user_input.equals("y")) {
                createSaveFile();
            }
        }
    }
}