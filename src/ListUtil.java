import java.util.HashMap;
import java.util.List;

/**
 * Created by lhscompsci on 5/18/16.
 */
public class ListUtil {

    public static String toString(List<String> list) {
        String as_string = "";
        for (String element : list) {
            as_string += element + " ";
        }
        return as_string;
    }

    public static String toString(HashMap<String, MathObject> hashMap) {
        String as_string = "";
        for (String key : hashMap.keySet()) {
            as_string += key + ": " + hashMap.get(key).toString() + "\n";
        }
        return as_string;
    }
}
