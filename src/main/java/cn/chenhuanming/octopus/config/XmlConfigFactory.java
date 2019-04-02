package cn.chenhuanming.octopus.config;

import cn.chenhuanming.octopus.formatter.DateFormatter;
import cn.chenhuanming.octopus.formatter.DefaultFormatterContainer;
import cn.chenhuanming.octopus.formatter.FormatterContainer;
import cn.chenhuanming.octopus.util.ColorUtils;
import cn.chenhuanming.octopus.util.ReflectionUtils;
import cn.chenhuanming.octopus.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
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
 * read config from xml file,construct {@link Config}
 *
 * @author chenhuanming
 * Created at 2018/12/10
 */
@Slf4j
public class XmlConfigFactory extends AbstractXMLConfigFactory {

    public XmlConfigFactory(InputStream is) {
        super(is);
    }

    @Override
    protected Config readConfig() {
        Document document;
        try {
            validateXML(new StreamSource(is));
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        } catch (Exception e) {
            throw new IllegalArgumentException("xml file is not valid", e);
        }

        Element root = document.getDocumentElement();
        Config config = new Config();

        if (!XMLConstant.Root.name.equals(root.getTagName())) {
            throw new IllegalArgumentException("xml config file: must has a root tag named " + XMLConstant.Root.name);
        }
        String className = root.getAttribute(XMLConstant.Root.Attribute.CLASS);
        if (StringUtils.isEmpty(className)) {
            throw new IllegalArgumentException("xml config file: tag " + XMLConstant.Root.name + "must has " + XMLConstant.Root.Attribute.CLASS + " attribute");
        }

        Class<?> classType = null;
        try {
            classType = Class.forName(className);
            config.setClassType(classType);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }

        Node formattersNode = root.getElementsByTagName(XMLConstant.Formatters.name).item(0);
        config.setFormatterContainer(readFormatter(formattersNode));

        Field field = getField(root, classType);

        config.setFields(field.getChildren());

        return config;
    }

    private FormatterContainer readFormatter(Node formatNode) {
        DefaultFormatterContainer container = new DefaultFormatterContainer();

        String dateFormat = getAttribute(formatNode, XMLConstant.Formatters.Attribute.DATE_FORMAT);
        if (StringUtils.isEmpty(dateFormat)) {
            container.addFormat(Date.class, new DateFormatter("yyyy-MM-dd HH:mm:ss"));
        } else {
            container.addFormat(Date.class, new DateFormatter(dateFormat));
        }

        if (formatNode != null && formatNode.hasChildNodes()) {
            NodeList children = formatNode.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node item = children.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE || !item.getNodeName().equals(XMLConstant.Formatters.Formatter.name)) {
                    continue;
                }
                String targetClass = getAttribute(item, XMLConstant.Formatters.Formatter.Attribute.TARGET);
                String formatClass = getAttribute(item, XMLConstant.Formatters.Formatter.Attribute.CLASS);

                try {
                    Class target = Class.forName(targetClass);
                    Class format = Class.forName(formatClass);
                    container.addFormat(target, (cn.chenhuanming.octopus.formatter.Formatter) format.newInstance());
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }

            }
        }
        return container;
    }

    private Field getField(Node node, Class classType) {
        Field field = new Field();

        setBaseConfig(field, node);

        setCellStyleConfig(field, node);

        setHeaderCellStyleConfig(field, node);

        setInvoker(field, classType);

        if (node.getNodeName().equals(XMLConstant.Field.name)) {
            setImportValidation(field, node);
        }

        NodeList children = node.getChildNodes();

        Class headerType = node.getNodeName().equals(XMLConstant.Root.name) ? classType : (field.getPicker() != null ? field.getPicker().getReturnType() : null);
        for (int i = 0; i < children.getLength(); i++) {
            Node item = children.item(i);
            if (item.getNodeType() != Node.ELEMENT_NODE || (!item.getNodeName().equals(XMLConstant.Field.name) && !item.getNodeName().equals(XMLConstant.Header.name))) {
                continue;
            }
            field.addChildren(getField(children.item(i), headerType));
        }
        return field;
    }

    private void setBaseConfig(Field field, Node node) {
        String name = getAttribute(node, XMLConstant.Field.Attribute.NAME);
        if (!StringUtils.isEmpty(name)) {
            field.setName(name);
        }
        String desc = getAttribute(node, XMLConstant.Field.Attribute.DESCRIPTION);
        if (!StringUtils.isEmpty(desc)) {
            field.setDescription(desc);
        }

        String dateFormat = getAttribute(node, XMLConstant.Field.Attribute.DATE_FORMAT);
        if (!StringUtils.isEmpty(dateFormat)) {
            field.setDateFormat(new DateFormatter(dateFormat));
        }

        //read formatter
        String formatterStr = getAttribute(node, XMLConstant.Field.Attribute.FORMATTER);
        if (!StringUtils.isEmpty(formatterStr)) {
            try {
                Class formatterClass = Class.forName(formatterStr);
                if (!cn.chenhuanming.octopus.formatter.Formatter.class.isAssignableFrom(formatterClass)) {
                    log.error(formatterStr + " is not subclass of cn.chenhuanming.octopus.formatter.Formatters");
                } else {
                    field.setFormatter((cn.chenhuanming.octopus.formatter.Formatter) formatterClass.newInstance());
                }
            } catch (Exception e) {
                log.warn(formatterStr + " may not have a default constructor");
            }
        }
    }

    private void setCellStyleConfig(Field field, Node node) {
        FieldCellStyle.FieldCellStyleBuilder builder = FieldCellStyle.builder();

        String fontSize = getAttribute(node, XMLConstant.Field.Attribute.FONT_SIZE);
        builder.fontSize(Short.parseShort(StringUtils.defaultIfEmpty(fontSize, "14")));

        String color = getAttribute(node, XMLConstant.Field.Attribute.COLOR);
        builder.color(ColorUtils.hex2Rgb(StringUtils.defaultIfEmpty(color, "#000000")));

        String isBold = getAttribute(node, XMLConstant.Field.Attribute.IS_BOLD);
        builder.bold(Boolean.parseBoolean(StringUtils.defaultIfEmpty(isBold, "false")));

        String foregroundColor = getAttribute(node, XMLConstant.Field.Attribute.FOREGROUND_COLOR);
        builder.foregroundColor(ColorUtils.hex2Rgb(StringUtils.defaultIfEmpty(foregroundColor, null)));

        String border = getAttribute(node, XMLConstant.Field.Attribute.BORDER);
        builder.border(convertBorder(StringUtils.defaultIfEmpty(border, "0,0,0,0")));

        String borderColor = getAttribute(node, XMLConstant.Field.Attribute.BORDER_COLOR);
        builder.borderColor(convertBorderColor(StringUtils.defaultIfEmpty(borderColor, "#000000,#000000,#000000,#000000")));

        field.setFieldCellStyle(builder.build());
    }

    private void setHeaderCellStyleConfig(Field field, Node node) {
        FieldCellStyle.FieldCellStyleBuilder builder = FieldCellStyle.builder();

        String fontSize = getAttribute(node, XMLConstant.Header.Attribute.HEADER_FONT_SIZE);
        builder.fontSize(Short.parseShort(StringUtils.defaultIfEmpty(fontSize, "15")));

        String color = getAttribute(node, XMLConstant.Header.Attribute.HEADER_COLOR);
        builder.color(ColorUtils.hex2Rgb(StringUtils.defaultIfEmpty(color, "#000000")));

        String isBold = getAttribute(node, XMLConstant.Header.Attribute.IS_HEADER_BOLD);
        builder.bold(Boolean.parseBoolean(StringUtils.defaultIfEmpty(isBold, "true")));

        String foregroundColor = getAttribute(node, XMLConstant.Header.Attribute.HEADER_FOREGROUND_COLOR);
        builder.foregroundColor(ColorUtils.hex2Rgb(StringUtils.defaultIfEmpty(foregroundColor, "#FFFFFF")));

        String border = getAttribute(node, XMLConstant.Header.Attribute.HEADER_BORDER);
        builder.border(convertBorder(StringUtils.defaultIfEmpty(border, "1,1,1,1")));

        String borderColor = getAttribute(node, XMLConstant.Header.Attribute.HEADER_BORDER_COLOR);
        builder.borderColor(convertBorderColor(StringUtils.defaultIfEmpty(borderColor, "#000000,#000000,#000000,#000000")));

        field.setHeaderFieldCellStyle(builder.build());
    }

    private void setInvoker(Field field, Class classType) {
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

    private void setImportValidation(Field field, Node node) {
        ImportValidation validation = new ImportValidation();
        String isBlankable = getAttribute(node, XMLConstant.Field.Attribute.IS_BLANKABLE);
        if (!StringUtils.isEmpty(isBlankable)) {
            validation.setBlankable(Boolean.parseBoolean(isBlankable));
        }

        String regex = getAttribute(node, XMLConstant.Field.Attribute.REGEX);
        if (!StringUtils.isEmpty(regex)) {
            validation.setRegex(Pattern.compile(regex));
        }
        String options = getAttribute(node, XMLConstant.Field.Attribute.OPTIONS);
        if (!StringUtils.isEmpty(options) && options.length() >= 2) {
            String[] split = options.split(SPLITTER);
            validation.setOptions(Arrays.asList(split));
        }

        field.setImportValidation(validation);
    }

}
