package xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class MyXMLReader extends DefaultHandler {

    String value = null;
    Area area = null;

    private ArrayList<Area> areaList = new ArrayList<Area>();

    public ArrayList<Area> getAreaList() {
        return areaList;
    }

    // XML 解析开始
    public void startDocument() throws SAXException {
        super.startDocument();
        System.out.println("xml 解析开始");
    }

    public void endDocument() throws SAXException {
        super.endDocument();
        System.out.println("xml 解析结束");
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        System.out.println(qName);
        if (qName.equals("INFO")) {
            area = new Area();

            // 获取属性值
            /*for (int i = 0; i < attributes.getLength(); i++) {
                System.out.println(attributes.getQName(i) + "---" + attributes.getValue(i));

                if (attributes.getQName(i).equals("NO")) {
                    area.setNo(attributes.getValue(i));
                }

            }*/
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equals("INFO")) {
            areaList.add(area);
            area = null;
        } else if (qName.equals("ADDRESS")) {
            area.setAddress(value);
        } else if (qName.equals("NO")) {
            area.setNo(value);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        // 获取节点值数组
        value = new String(ch, start, length);
        if (!value.trim().equals("")) {
            System.out.println("节点值:" + value);
        }
    }

}
