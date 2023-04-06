import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaobinhan
 */
public class JavaTest {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        System.out.println(list.get(1));
        System.out.println("test");

        String entryTime = "2022-10";
        Integer entryYear = Integer.valueOf(entryTime.substring(0, entryTime.indexOf("-")));
        Integer entryMonth = Integer.valueOf(entryTime.substring(entryTime.indexOf("-") + 1));
        System.out.println(entryYear + "-----" +entryMonth);

    }
}
