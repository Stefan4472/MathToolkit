import java.io.*;
import java.util.HashMap;
import java.util.prefs.Preferences;

/**
 * Class with helper methods for reading and writing the persistent data
 * used by the program. Contains methods for getting and setting the last-
 * used file path where data is stored and methods for reading and writing
 * MathObjects to storage files.
 */
public class PersistentData {

    // returns the filepath of the file containing stored MathObjects
    // returns null if no filepath is stored
    public static File getStorageFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        String file_path = prefs.get("filePath", null);
        if (file_path != null) {
            return new File(file_path);
        } else {
            return null;
        }
    }

    // sets the filepath of the file containing stored MathObjects
    // if file==null the stored filepath will be cleared from memory
    public static void setStorageFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());
        } else {
            prefs.remove("filePath");
        }
    }

    // reads in MathObjects from specified file and puts them in hashmap of given EquationParser
    public static boolean importMathObjects(File file, EquationParser parser) {
        String line = "";
        try {
            FileReader file_reader = new FileReader(file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            while((line = buffered_reader.readLine()) != null) {
                try {
                    parser.evaluateExpression(line);
                } catch (Exception e) {}
            }
            buffered_reader.close();
            return true;
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // writes MathObjects to given file
    public static boolean exportMathObjects(File file, EquationParser parser) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            HashMap<String, MathObject> variables = parser.getVariables();
            for (String key : variables.keySet()) {
                MathObject variable = variables.get(key);
                bufferedWriter.write(variable.getId() + "=" + variable.toString() + "\n");
            }
            bufferedWriter.close();
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
