package xml;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDOM方法解析XML
 */
public class JdomXML {

    private static ArrayList<Area> areaList = new ArrayList<Area>();

    public static void main(String[] args) throws IOException, JDOMException {
        SAXBuilder saxBuilder = new SAXBuilder();
        InputStream in;

        in = new FileInputStream("javaTestTool/src/xml/test.xml");
        InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");

        // 获取文档
        Document document = saxBuilder.build(inputStreamReader);
        // 获取根节点
        Element rootElement = document.getRootElement();
        List<Element> infoList = rootElement.getChildren();

        for (Element info : infoList) {
            Area area = new Area();
            // 获取属性列表
            List<Attribute> attributes = info.getAttributes();
            for (Attribute attribute : attributes) {
                String name = attribute.getName();
                String value = attribute.getValue();
                System.out.println(name + ":" + value);
            }

            // 获取节点名和节点值
            List<Element> children = info.getChildren();
            for (Element child : children) {
                if (child.getName().equals("NO")) {
                    area.setNo(child.getValue());
                } else if (child.getName().equals("ADDRESS")) {
                    area.setAddress(child.getValue());
                }
            }
            areaList.add(area);

        }
        System.out.println(areaList.toString());
    }
}
