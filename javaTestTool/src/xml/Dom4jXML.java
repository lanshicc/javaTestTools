package xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * dom4j 解析XML
 */
public class Dom4jXML {
    public static void main(String[] args) throws DocumentException {
        ArrayList<Area> areaList = new ArrayList<>();
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File("javaTestTool/src/xml/test.xml"));

        Element rootElement = document.getRootElement();
        Iterator iterator = rootElement.elementIterator();
        while (iterator.hasNext()) {
            Element info = (Element) iterator.next();
            Area area = new Area();
            // 获取属性
            List<Attribute> attributes = info.attributes();
            for (Attribute attribute : attributes) {
                String name = attribute.getName();
                String value = attribute.getValue();
                System.out.println(name + "=" + value);
            }

            // 获取方法
            Iterator infoIterator = info.elementIterator();
            while(infoIterator.hasNext()) {
                Element childElement = (Element) infoIterator.next();
                System.out.println(childElement.getName() + ":" + childElement.getText());

                if (childElement.getName().equals("NO")) {
                    area.setNo(childElement.getText());
                } else if (childElement.getName().equals("ADDRESS")) {
                    area.setAddress(childElement.getText());
                }
            }
            areaList.add(area);
        }
        System.out.println(areaList.toString());
    }
}
