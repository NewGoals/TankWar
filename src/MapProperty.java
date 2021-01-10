import java.io.*;
import java.util.Properties;
import java.util.regex.Pattern;

public class MapProperty {

    private static Properties properties;

    static {
        InputStream inputStream = null;
        try {
            File file = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "map.properties");
            inputStream = new FileInputStream(file);
            properties = new Properties();
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getString(String key) {
        return properties.get(key).toString();
    }

    public static int getInt(String Key) {
        String value = getString(Key);
        if (Pattern.matches("[0-9]+", value)) {
            return Integer.parseInt(value);
        } else {
            throw new ClassCastException("[" + value + "] is not a number! Check the property: [" + Key + "]");
        }
    }

}
