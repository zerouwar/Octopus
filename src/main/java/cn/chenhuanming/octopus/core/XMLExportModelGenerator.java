package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.ExportField;
import cn.chenhuanming.octopus.model.ExportModel;
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
public class XMLExportModelGenerator implements ExportModelGenerator {
    private InputStream is;
    private Element root;
    private final String CONFIG_ROOT_NAME = "ExportModel";
    private final String CONFIG_FlELD_NAME = "Field";

    public XMLExportModelGenerator(InputStream is) {
        this.is = is;
    }

    @Override
    public ExportModel generate() {

        Document document;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        root = document.getDocumentElement();

        ExportModel exportModel = initRoot();
        ArrayList<ExportField> fields = new ArrayList<>();
        exportModel.setFields(fields);

        initField(exportModel.getFields(),root);
        exportModel.calculateSize();

        return exportModel;
    }

    private ExportModel initRoot(){
        //read Tag ExportModel from config xml
            if(root==null||!root.getTagName().equals(CONFIG_ROOT_NAME))
                throw new IllegalArgumentException("excel config xml must has tag ExportModel as document element");
            if(!root.hasAttribute("class"))
                throw new IllegalArgumentException("tag ExportModel must has attribute `class` in excel config xml");
            Node node = root.getAttributes().getNamedItem("class");
        try {
            return new ExportModel(Class.forName(node.getNodeValue()));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(node.getNodeValue()+" didn't be found");
        }
    }

    private void initField(List<ExportField> fields, Node node){
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
            ExportField exportFiled = new ExportField(name.getNodeValue(),description.getNodeValue());
            if(field.hasChildNodes()){
                exportFiled.setFields(new ArrayList<>());
                initField(exportFiled.getFields(),field);
            }

            fields.add(exportFiled);
        }
    }

}
