package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.model.formatter.DateFormatter;
import cn.chenhuanming.octopus.model.formatter.DefaultFormatterContainer;
import cn.chenhuanming.octopus.model.formatter.FormatterContainer;
import cn.chenhuanming.octopus.util.ColorUtils;
import cn.chenhuanming.octopus.util.ReflectionUtils;
import cn.chenhuanming.octopus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * read config from xml file,construct {@link DefaultConfig}
 *
 * @author chenhuanming
 * Created at 2018/12/10
 */
public class XmlConfigReader extends AbstractXMLConfigReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlConfigReader.class);

    public XmlConfigReader(InputStream is) {
        super(is);
    }

    @Override
    protected Config readConfig() {
        Document document;
        try {
            validateXML(new StreamSource(is));
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        Element root = document.getDocumentElement();
        DefaultConfig config = new DefaultConfig();

        if (!XMLConfig.Root.name.equals(root.getTagName())) {
            throw new IllegalArgumentException("xml config file: must has a root tag named " + XMLConfig.Root.name);
        }
        String className = root.getAttribute(XMLConfig.Root.Attribute.CLASS);
        if (StringUtils.isEmpty(className)) {
            throw new IllegalArgumentException("xml config file: tag " + XMLConfig.Root.name + "must has " + XMLConfig.Root.Attribute.CLASS + " attribute");
        }

        Class<?> classType = null;
        try {
            classType = Class.forName(className);
            config.setClassType(classType);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }

        Node formattersNode = root.getElementsByTagName(XMLConfig.Formatters.name).item(0);
        config.setFormatterContainer(readFormatter(formattersNode));

        Field field = getField(root, classType);

        config.setFields(field.getChildren());

        return config;
    }

    private FormatterContainer readFormatter(Node formatNode) {
        DefaultFormatterContainer container = new DefaultFormatterContainer();

        String dateFormat = getAttribute(formatNode, XMLConfig.Formatters.Attribute.DATE_FORMAT);
        if (StringUtils.isEmpty(dateFormat)) {
            container.addFormat(Date.class, new DateFormatter("yyyy-MM-dd HH:mm:ss"));
        } else {
            container.addFormat(Date.class, new DateFormatter(dateFormat));
        }

        if (formatNode != null && formatNode.hasChildNodes()) {
            NodeList children = formatNode.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node item = children.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE || !item.getNodeName().equals(XMLConfig.Formatters.Formatter.name)) {
                    continue;
                }
                String targetClass = getAttribute(item, XMLConfig.Formatters.Formatter.Attribute.TARGET);
                String formatClass = getAttribute(item, XMLConfig.Formatters.Formatter.Attribute.CLASS);

                try {
                    Class target = Class.forName(targetClass);
                    Class format = Class.forName(formatClass);
                    container.addFormat(target, (cn.chenhuanming.octopus.model.formatter.Formatter) format.newInstance());
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }

            }
        }
        return container;
    }

    private Field getField(Node node, Class classType) {
        DefaultField field = new DefaultField();

        setBaseConfig(field, node);

        setCellStyleConfig(field, node);

        setHeaderCellStyleConfig(field, node);

        setInvoker(field, classType);

        if (node.getNodeName().equals(XMLConfig.Field.name)) {
            setImportValidation(field, node);
        }

        NodeList children = node.getChildNodes();

        Class headerType = node.getNodeName().equals(XMLConfig.Root.name) ? classType : (field.getPicker() != null ? field.getPicker().getReturnType() : null);
        for (int i = 0; i < children.getLength(); i++) {
            Node item = children.item(i);
            if (item.getNodeType() != Node.ELEMENT_NODE || (!item.getNodeName().equals(XMLConfig.Field.name) && !item.getNodeName().equals(XMLConfig.Header.name))) {
                continue;
            }
            field.addChildren(getField(children.item(i), headerType));
        }
        return field;
    }

    private void setBaseConfig(DefaultField field, Node node) {
        String name = getAttribute(node, XMLConfig.Field.Attribute.NAME);
        if (!StringUtils.isEmpty(name)) {
            field.setName(name);
        }
        String desc = getAttribute(node, XMLConfig.Field.Attribute.DESCRIPTION);
        if (!StringUtils.isEmpty(desc)) {
            field.setDescription(desc);
        }

        String dateFormat = getAttribute(node, XMLConfig.Field.Attribute.DATE_FORMAT);
        if (!StringUtils.isEmpty(dateFormat)) {
            field.setDateFormat(new DateFormatter(dateFormat));
        }

        //read formatter
        String formatterStr = getAttribute(node, XMLConfig.Field.Attribute.FORMATTER);
        if (!StringUtils.isEmpty(formatterStr)) {
            try {
                Class formatterClass = Class.forName(formatterStr);
                if (!cn.chenhuanming.octopus.model.formatter.Formatter.class.isAssignableFrom(formatterClass)) {
                    LOGGER.error(formatterStr + " is not subclass of cn.chenhuanming.octopus.model.formatter.Formatters");
                } else {
                    field.setFormatter((cn.chenhuanming.octopus.model.formatter.Formatter) formatterClass.newInstance());
                }
            } catch (Exception e) {
                LOGGER.warn(formatterStr + " may not have a default constructor");
            }
        }
    }

    private void setCellStyleConfig(DefaultField field, Node node) {
        String fontSize = getAttribute(node, XMLConfig.Field.Attribute.FONT_SIZE);
        if (!StringUtils.isEmpty(fontSize)) {
            field.setFontSize(Short.parseShort(fontSize));
        }
        String color = getAttribute(node, XMLConfig.Field.Attribute.COLOR);
        if (!StringUtils.isEmpty(color)) {
            field.setColor(ColorUtils.hex2Rgb(color));
        }
        String isBold = getAttribute(node, XMLConfig.Field.Attribute.IS_BOLD);
        if (!StringUtils.isEmpty(isBold)) {
            field.setBold(Boolean.parseBoolean(isBold));
        }
        String foregroundColor = getAttribute(node, XMLConfig.Field.Attribute.FOREGROUND_COLOR);
        if (!StringUtils.isEmpty(foregroundColor)) {
            field.setForegroundColor(ColorUtils.hex2Rgb(foregroundColor));
        }
        String border = getAttribute(node, XMLConfig.Field.Attribute.BORDER);
        if (!StringUtils.isEmpty(border)) {
            field.setBorder(convertBorder(border));
        }
        String borderColor = getAttribute(node, XMLConfig.Field.Attribute.BORDER_COLOR);
        if (!StringUtils.isEmpty(borderColor)) {
            field.setBorderColor(convertBorderColor(borderColor));
        }
    }

    private void setHeaderCellStyleConfig(DefaultField field, Node node) {
        String fontSize = getAttribute(node, XMLConfig.Header.Attribute.HEADER_FONT_SIZE);
        if (!StringUtils.isEmpty(fontSize)) {
            field.setHeaderFontSize(Short.parseShort(fontSize));
        }
        String color = getAttribute(node, XMLConfig.Header.Attribute.HEADER_COLOR);
        if (!StringUtils.isEmpty(color)) {
            field.setHeaderColor(ColorUtils.hex2Rgb(color));
        }
        String isBold = getAttribute(node, XMLConfig.Header.Attribute.IS_HEADER_BOLD);
        if (!StringUtils.isEmpty(isBold)) {
            field.setHeaderBold(Boolean.parseBoolean(isBold));
        }
        String foregroundColor = getAttribute(node, XMLConfig.Header.Attribute.HEADER_FOREGROUND_COLOR);
        if (!StringUtils.isEmpty(foregroundColor)) {
            field.setHeaderForegroundColor(ColorUtils.hex2Rgb(foregroundColor));
        }
        String border = getAttribute(node, XMLConfig.Header.Attribute.HEADER_BORDER);
        if (!StringUtils.isEmpty(border)) {
            field.setHeaderBorder(convertBorder(border));
        }
        String borderColor = getAttribute(node, XMLConfig.Header.Attribute.HEADER_BORDER_COLOR);
        if (!StringUtils.isEmpty(borderColor)) {
            field.setHeaderBorderColor(convertBorderColor(borderColor));
        }
    }

    private void setInvoker(DefaultField field, Class classType) {
        if (classType == null || StringUtils.isEmpty(field.getName())) {
            return;
        }
        //set picker
        Method picker = ReflectionUtils.readMethod(classType, field.getName());
        field.setPicker(picker);

        //set pusher
        Method pusher = ReflectionUtils.writeMethod(classType, field.getName());
        field.setPusher(pusher);
    }

    private void setImportValidation(DefaultField field, Node node) {
        String isBlankable = getAttribute(node, XMLConfig.Field.Attribute.IS_BLANKABLE);
        if (!StringUtils.isEmpty(isBlankable)) {
            field.setBlankable(Boolean.parseBoolean(isBlankable));
        }

        String regex = getAttribute(node, XMLConfig.Field.Attribute.REGEX);
        if (!StringUtils.isEmpty(regex)) {
            field.setRegex(Pattern.compile(regex));
        }
        String options = getAttribute(node, XMLConfig.Field.Attribute.OPTIONS);
        if (!StringUtils.isEmpty(options) && options.length() >= 2) {
            String[] split = options.split(SPLITTER);
            field.setOptions(Arrays.asList(split));
        }
    }

}
