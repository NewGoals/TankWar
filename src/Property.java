import java.io.*;
import java.util.Properties;
import java.util.regex.Pattern;

public class Property {

    private static Properties properties;

    static {
        InputStream inputStream = null;
        try {
            inputStream = Property.class.getClassLoader().getResourceAsStream("system.properties");
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
