package xml;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * 使用DOM方法解析xml
 */
public class DomXML {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        File file = new File("javaTestTool/src/xml/test.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document doc = documentBuilder.parse(file);
        NodeList nodeList = doc.getElementsByTagName("INFO");
        for (int i = 0; i < nodeList.getLength(); i++) {
            System.out.println("编号:" + doc.getElementsByTagName("NO").item(i).getFirstChild().getNodeValue());
            System.out.println("地址:" + doc.getElementsByTagName("ADDRESS").item(i).getFirstChild().getNodeValue());;
        }
    }
}
