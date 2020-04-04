package com.custom.conf;

import com.custom.model.Configuration;
import com.custom.model.MappedStatment;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @Author: 刘劲
 * @Date: 2020/3/28 22:37
 */
public class XmlMapperBuilder {

    private Configuration configuration;

    public XmlMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parseConfig(InputStream inputStream) throws DocumentException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");
        getMapperStatement(rootElement, namespace, "//select");
        getMapperStatement(rootElement, namespace, "//update");
        getMapperStatement(rootElement, namespace, "//insert");
        getMapperStatement(rootElement, namespace, "//delete");
    }

    private void getMapperStatement(Element rootElement, String namespace, String tag){
        List<Element> elements = rootElement.selectNodes(tag);
        Map<String, MappedStatment> configurationStatment = configuration.getStatment();
        for (Element e : elements){
            String id = e.attributeValue("id");
            String resultType = e.attributeValue("resultType");
            String parammeterType = e.attributeValue("parammeterType");
            String sql = e.getTextTrim().toLowerCase();
            MappedStatment statment = new MappedStatment();
            statment.setId(id);
            statment.setCommandType(getCommanType(sql));
            statment.setResultType(resultType);
            statment.setParameterType(parammeterType);
            statment.setSql(sql);
            configurationStatment.put(namespace + "." + id, statment);
        }
    }

    private String getCommanType(String sql){
        if (StringUtils.isEmpty(sql)){
            return "";
        }
        if (sql.startsWith("select")){
            return  "select";
        }
        if (sql.startsWith("update")){
            return "update";
        }
        if (sql.startsWith("insert")){
            return "insert";
        }
        if (sql.startsWith("delete")){
            return "delete";
        }
        return "";
    }

}
