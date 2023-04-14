package xml;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Stack;

/**
 * SAX方式解析XML
 */
public class SaxXML {
    public Stack<String> stack = new Stack<String>();

    public SaxXML(){
        super();
    }
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        MyXMLReader myXMLReader = new MyXMLReader();
        saxParser.parse("javaTestTool/src/xml/test.xml", myXMLReader);

        System.out.println("共有" + myXMLReader.getAreaList().size() + "个地址信息");
        System.out.println(myXMLReader.getAreaList().toString());
    }

}
