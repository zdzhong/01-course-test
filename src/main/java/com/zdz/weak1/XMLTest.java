package com.zdz.weak1;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLTest {
    public static void main(String[] args) {
        // 创建DocumentBuilderFactory 对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("D:/Users/learn/spring/01-course-test/src/main/java/com/zdz/weak1/xml/student.xml");
            NodeList nodeList = document.getElementsByTagName("student");
            node(nodeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void node(NodeList nodeList) {
        for (int i = 0;i < nodeList.getLength();i++){
            Node node = nodeList.item(i);
            NodeList childNodes = node.getChildNodes();
            for (int j = 0;j < childNodes.getLength();j++){
                if(childNodes.item(j).getNodeType() == Node.ELEMENT_NODE){
                    System.out.print(childNodes.item(j).getNodeName() + ":");
                    System.out.println(childNodes.item(j).getFirstChild().getNodeValue());
                }
            }
        }
    }
}
