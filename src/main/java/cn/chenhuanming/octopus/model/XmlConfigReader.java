package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.util.ColorUtils;
import cn.chenhuanming.octopus.util.ReflectionUtils;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * read config from xml file,construct {@link DefaultConfig}
 *
 * @author chenhuanming
 * Created at 2018/12/10
 */
public class XmlConfigReader extends AbstractXMLConfigReader {

    private byte[] configBytes;

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlConfigReader.class);

    public XmlConfigReader(InputStream is) {
        try {
            configBytes = ByteStreams.toByteArray(is);
            is.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected Config readConfig() {
        Document document;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(configBytes));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        Element root = document.getDocumentElement();
        DefaultConfig config = new DefaultConfig();

        if (!XMLConfig.Root.name.equals(root.getTagName())) {
            throw new IllegalArgumentException("xml config file: must has a root tag named " + XMLConfig.Root.name);
        }
        String className = root.getAttribute(XMLConfig.Root.Attribute.CLASS);
        if (Strings.isNullOrEmpty(className)) {
            throw new IllegalArgumentException("xml config file: tag " + XMLConfig.Root.name + "must has " + XMLConfig.Root.Attribute.CLASS + " attribute");
        }

        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
            config.setClazz(clazz);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }

        NodeList formatNode = root.getElementsByTagName(XMLConfig.Formatter.name);
        config.setCellFormatterMap(readFormatter(formatNode.item(0)));

        Field field = getField(root, clazz, true, config);

        config.setFields(field.getChildren());

        return config;
    }

    private CellFormatterMap readFormatter(Node formatNode) {
        DefaultCellformatter formatter = new DefaultCellformatter();
        String dateFormat = getAttribute(formatNode, XMLConfig.Formatter.Attribute.DATE_FORMAT);
        if (Strings.isNullOrEmpty(dateFormat)) {
            formatter.addFormat(Date.class, new DateCellFormatter("yyyy-MM-dd HH:mm:ss"));
        } else {
            formatter.addFormat(Date.class, new DateCellFormatter(dateFormat));
        }

        if (formatNode.hasChildNodes()) {
            NodeList children = formatNode.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node item = children.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE || !item.getNodeName().equals(XMLConfig.Formatter.Format.name)) {
                    continue;
                }
                String targetClass = getAttribute(item, XMLConfig.Formatter.Format.Attribute.TARGET);
                String formatClass = getAttribute(item, XMLConfig.Formatter.Format.Attribute.CLASS);

                try {
                    Class target = Class.forName(targetClass);
                    Class format = Class.forName(formatClass);
                    formatter.addFormat(target, (CellFormatter) format.newInstance());
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }

            }
        }
        return formatter;
    }

    private Field getField(Node node, Class clazz, boolean keep, Config config) {
        DefaultField field = new DefaultField();
        setBaseConfig(field, node, config);

        //set invoker
        Method invoker = null;
        if (!keep) {
            invoker = ReflectionUtils.getterMethod(clazz, field.getName());
            field.setPicker(invoker);
        }

        Class<?> returnType = keep ? clazz : (invoker == null ? null : invoker.getReturnType());
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node item = children.item(i);
            if (item.getNodeType() != Node.ELEMENT_NODE || (!item.getNodeName().equals(XMLConfig.Field.name) && !item.getNodeName().equals(XMLConfig.Header.name))) {
                continue;
            }
            field.addChildren(getField(children.item(i), returnType, false, config));
        }
        return field;
    }

    private void setBaseConfig(AbstractField field, Node node, Config config) {
        String name = getAttribute(node, XMLConfig.Field.Attribute.NAME);
        if (!Strings.isNullOrEmpty(name)) {
            field.setName(name);
        }
        String desc = getAttribute(node, XMLConfig.Field.Attribute.DESCRIPTION);
        if (!Strings.isNullOrEmpty(desc)) {
            field.setDescription(desc);
        }
        String fontSize = getAttribute(node, XMLConfig.Field.Attribute.FONT_SIZE);
        if (!Strings.isNullOrEmpty(fontSize)) {
            field.setFontSize(Short.parseShort(fontSize));
        }
        String color = getAttribute(node, XMLConfig.Field.Attribute.COLOR);
        if (!Strings.isNullOrEmpty(color)) {
            field.setColor(ColorUtils.hex2Rgb(color));
        }
        String isBold = getAttribute(node, XMLConfig.Field.Attribute.IS_BOLD);
        if (!Strings.isNullOrEmpty(isBold)) {
            field.setBold(Boolean.parseBoolean(isBold));
        }
        String backgroundColor = getAttribute(node, XMLConfig.Field.Attribute.BACKGROUND_COLOR);
        if (!Strings.isNullOrEmpty(backgroundColor)) {
            field.setBackgroundColor(ColorUtils.hex2Rgb(backgroundColor));
        }
        String dateFormat = getAttribute(node, XMLConfig.Field.Attribute.DATE_FORMAT);
        if (!Strings.isNullOrEmpty(dateFormat)) {
            field.setDateFormat(new DateCellFormatter(dateFormat));
        }

        //read formatter
        String formatterStr = getAttribute(node, XMLConfig.Field.Attribute.FORMATTER);
        if (!Strings.isNullOrEmpty(formatterStr)) {
            try {
                Class formatterClass = Class.forName(formatterStr);
                if (!CellFormatter.class.isAssignableFrom(formatterClass)) {
                    LOGGER.warn(formatterStr + " is not subclass of cn.chenhuanming.octopus.model.CellFormatter");
                } else {
                    field.setFormatter((CellFormatter) formatterClass.newInstance());
                }
            } catch (Exception e) {
                LOGGER.warn(formatterStr + " may not have a empty constructor");
            }
        }
    }

}
