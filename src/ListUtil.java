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
}
