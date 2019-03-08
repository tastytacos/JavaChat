package manager;

import java.util.Map;

abstract class AbstractDataConverter {
    abstract Map<String, String> getMapFromFile(String filename) throws IncorrectXMLFormatException;
}
