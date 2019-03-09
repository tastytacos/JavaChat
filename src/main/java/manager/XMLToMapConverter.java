package manager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class XMLToMapConverter extends AbstractDataConverter {
    private Map<String, String> map = new HashMap<>();
    private String rootTag = "configuration";


    @Override
    Map<String, String> getMapFromFile(String filename) throws IncorrectXMLFormatException {
        Document document = null;
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = documentBuilder.parse(filename);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NodeList nodes = document.getElementsByTagName(rootTag);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node root = nodes.item(i);
            if (root.getNodeType() == Node.ELEMENT_NODE) {
                NodeList tags = root.getChildNodes();
                for (int j = 0; j < tags.getLength(); j++) {
                    Node element = tags.item(j);
                    if (element.getNodeType() == Node.ELEMENT_NODE) {
                        Element tag = (Element) element;
                        if (hasChildElements(tag)) {
                            throw new IncorrectXMLFormatException("The tag " + tag.getTagName() + " has child nodes");
                        }
                        map.put(tag.getTagName(), tag.getTextContent());
                    }
                }
            }
        }
        return map;
    }

    private boolean hasChildElements(Element el) {
        NodeList children = el.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE)
                return true;
        }
        return false;
    }
}
