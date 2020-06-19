import java.util.HashSet;

/**
 * @author: theTian
 * @date: 2020/6/17 16:57
 */
public class Test {
    @org.junit.Test
    public void t(){
        HashSet<String> objects = new HashSet<>();
        objects.add("123");
        objects.add("123");
        objects.add("123");
        System.out.println(objects.size());
    }
}
