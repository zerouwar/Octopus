package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.OutputField;
import cn.chenhuanming.octopus.model.OutputModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhuanming on 2017-07-01.
 *
 * @author chenhuanming
 */
public class SimpleOutputModelGenerator implements OutputModelGenerator {
    private Element root;
    private final String CONFIG_ROOT_NAME = "OutputModel";
    private final String CONFIG_FlELD_NAME = "Field";

    @Override
    public OutputModel generate(InputStream is) {

        Document document;

        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        root = document.getDocumentElement();

        OutputModel outputModel = initRoot();
        ArrayList<OutputField> fields = new ArrayList<>();
        outputModel.setFields(fields);

        initField(outputModel.getFields(),root);
        outputModel.calculateSize();

        return outputModel;
    }

    private OutputModel initRoot(){
        //read Tag OutputModel from config xml
            if(root==null||!root.getTagName().equals(CONFIG_ROOT_NAME))
                throw new IllegalArgumentException("excel config xml must has tag OutputModel as document element");
            if(!root.hasAttribute("class"))
                throw new IllegalArgumentException("tag OutputModel must has attribute `class` in excel config xml");
            Node node = root.getAttributes().getNamedItem("class");
        try {
            return new OutputModel(Class.forName(node.getNodeValue()));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(node.getNodeValue()+" didn't be found");
        }
    }

    private void initField(List<OutputField> fields, Node node){
        //read all Tar Field from config xml
        NodeList fieldsNodes = node.getChildNodes();
        if(fieldsNodes==null||fieldsNodes.getLength()<=0)
            throw new IllegalArgumentException("excel config xml must has one more tag Field");
        for (int i = 0; i < fieldsNodes.getLength(); i++) {
            Node field = fieldsNodes.item(i);
            if(field.getNodeType()!=Node.ELEMENT_NODE||!field.getNodeName().equals(CONFIG_FlELD_NAME))
                continue;
            if(!field.hasAttributes())
                throw new IllegalArgumentException("Field must have attribute name and description in excel config xml");
            Node name = field.getAttributes().getNamedItem("name");
            if(name==null)
                throw new IllegalArgumentException("Field must have attribute name in excel config xml");
            Node description = field.getAttributes().getNamedItem("description");
            if(description==null)
                throw new IllegalArgumentException("Field must have attribute description in excel config xml");
            OutputField outputFiled = new OutputField(name.getNodeValue(),description.getNodeValue());
            if(field.hasChildNodes()){
                outputFiled.setFields(new ArrayList<>());
                initField(outputFiled.getFields(),field);
            }

            fields.add(outputFiled);
        }
    }

}
