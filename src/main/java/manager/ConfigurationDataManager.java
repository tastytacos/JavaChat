package manager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationDataManager {
    private static final String path = System.getProperty("user.dir") + "/src/main/resources/";
    private static final String filename = path + "config.xml";
    private static AbstractDataConverter converter = new XMLToMapConverter();
    private static Map<String, String> xmlTagsMap;


    public static String getValueByXMLTag(String tag){
        try {
            xmlTagsMap = converter.getMapFromFile(filename);
        } catch (IncorrectXMLFormatException e) {
            e.printStackTrace();
        }
        String value = xmlTagsMap.get(tag);
        if (value == null){
            try {
                throw new NoSuchXMLValueException("No " + tag + " value in");
            } catch (NoSuchXMLValueException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

}
