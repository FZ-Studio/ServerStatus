public class ConfigTest {
    public static void main(String[] args) {
        String str = "localhost";
        int x = str.lastIndexOf(":");
        //System.out.println(str.substring(x+1));
        System.out.println(str.substring(0, x));
    }
}
