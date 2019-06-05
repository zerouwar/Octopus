package cn.chenhuanming.octopus.config;

import cn.chenhuanming.octopus.formatter.DateFormatter;
import cn.chenhuanming.octopus.formatter.DefaultFormatterContainer;
import cn.chenhuanming.octopus.formatter.FormatterContainer;
import cn.chenhuanming.octopus.util.ColorUtils;
import cn.chenhuanming.octopus.util.ReflectionUtils;
import cn.chenhuanming.octopus.util.StringUtils;
import cn.chenhuanming.octopus.util.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
    public Config getConfig() {
        Document document;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            String schemaUri = document.getDocumentElement().getAttribute("xsi:noNamespaceSchemaLocation");
            validateXML(new StreamSource(is), schemaUri);
        } catch (Exception e) {
            throw new IllegalArgumentException("xml file is not valid", e);
        }

        Element root = document.getDocumentElement();

        if (!XmlNode.Root.nodeName.equals(root.getTagName())) {
            throw new IllegalArgumentException("xml config file: must has a root tag named " + XmlNode.Root.nodeName);
        }
        String className = root.getAttribute(XmlNode.Root.Attribute.CLASS);

        Class<?> classType = null;
        try {
            classType = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }

        Node formattersNode = root.getElementsByTagName(XmlNode.Formatters.nodeName).item(0);

        return Config.of(classType, readFormatter(formattersNode), getFields(root, classType));
    }

    private FormatterContainer readFormatter(Node formatNode) {
        DefaultFormatterContainer container = new DefaultFormatterContainer();

        String dateFormat = getAttribute(formatNode, XmlNode.Formatters.Attribute.DATE_FORMAT);
        if (StringUtils.isEmpty(dateFormat)) {
            container.addFormat(Date.class, new DateFormatter("yyyy-MM-dd HH:mm:ss"));
        } else {
            container.addFormat(Date.class, new DateFormatter(dateFormat));
        }

        if (formatNode != null && formatNode.hasChildNodes()) {
            NodeList children = formatNode.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node item = children.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE || !item.getNodeName().equals(XmlNode.Formatters.Formatter.nodeName)) {
                    continue;
                }
                String targetClass = getAttribute(item, XmlNode.Formatters.Formatter.Attribute.TARGET);
                String formatClass = getAttribute(item, XmlNode.Formatters.Formatter.Attribute.CLASS);

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

    private List<Field> getFields(Node node, Class classType) {

        List<Field> children = new ArrayList<>();

        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node item = node.getChildNodes().item(i);
            if (item.getNodeType() != Node.ELEMENT_NODE || (!item.getNodeName().equals(XmlNode.Field.nodeName) && !item.getNodeName().equals(XmlNode.Header.nodeName))) {
                continue;
            }

            Field.FieldBuilder field = Field.builder();

            String name = getAttribute(item, XmlNode.Field.Attribute.NAME);
            ValidationUtils.notEmpty(name, XmlNode.Field.Attribute.NAME);
            field.name(name);

            String description = getAttribute(item, XmlNode.Field.Attribute.DESCRIPTION);
            ValidationUtils.notEmpty(description, XmlNode.Field.Attribute.DESCRIPTION);
            field.description(description);

            setFormatter(field, item);

            Method picker = null;
            if (classType != null) {
                //set picker
                picker = ReflectionUtils.readMethod(classType, name);
                field.picker(picker);

                //set pusher
                Method pusher = ReflectionUtils.writeMethod(classType, name);
                field.pusher(pusher);
            }

            setHeaderCellStyleConfig(field, item);

            if (item.getNodeName().equals(XmlNode.Field.nodeName)) {
                setCellStyleConfig(field, item);
                setImportValidation(field, item);
            } else {
                Class headerType = item.getNodeName().equals(XmlNode.Root.nodeName) ? classType : (picker != null ? picker.getReturnType() : null);
                field.children(getFields(item, headerType));
            }
            children.add(field.build());
        }
        return children;
    }

    private void setFormatter(Field.FieldBuilder field, Node node) {
        String formatterStr = getAttribute(node, XmlNode.Field.Attribute.FORMATTER);
        if (StringUtils.isNotEmpty(formatterStr)) {
            try {
                Class formatterClass = Class.forName(formatterStr);
                if (!cn.chenhuanming.octopus.formatter.Formatter.class.isAssignableFrom(formatterClass)) {
                    throw new IllegalArgumentException(formatterStr + " is not subclass of cn.chenhuanming.octopus.formatter.Formatters");
                } else {
                    field.formatter((cn.chenhuanming.octopus.formatter.Formatter) formatterClass.newInstance());
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(formatterStr + " may not have a default constructor");
            }
        } else {
            String dateFormat = getAttribute(node, XmlNode.Field.Attribute.DATE_FORMAT);
            if (StringUtils.isNotEmpty(dateFormat)) {
                field.formatter(new DateFormatter(dateFormat));
            }
        }
    }

    private void setCellStyleConfig(Field.FieldBuilder field, Node node) {
        FieldCellStyle.FieldCellStyleBuilder builder = FieldCellStyle.builder();

        String fontSize = getAttribute(node, XmlNode.Field.Attribute.FONT_SIZE);
        builder.fontSize(Short.parseShort(StringUtils.defaultIfEmpty(fontSize, "14")));

        String color = getAttribute(node, XmlNode.Field.Attribute.COLOR);
        builder.color(ColorUtils.hex2Rgb(StringUtils.defaultIfEmpty(color, "#000000")));

        String isBold = getAttribute(node, XmlNode.Field.Attribute.IS_BOLD);
        builder.bold(Boolean.parseBoolean(StringUtils.defaultIfEmpty(isBold, "false")));

        String foregroundColor = getAttribute(node, XmlNode.Field.Attribute.FOREGROUND_COLOR);
        builder.foregroundColor(ColorUtils.hex2Rgb(StringUtils.defaultIfEmpty(foregroundColor, null)));

        String border = getAttribute(node, XmlNode.Field.Attribute.BORDER);
        builder.border(super.convertBorder(StringUtils.defaultIfEmpty(border, "0,0,0,0")));

        String borderColor = getAttribute(node, XmlNode.Field.Attribute.BORDER_COLOR);
        builder.borderColor(super.convertBorderColor(StringUtils.defaultIfEmpty(borderColor, "#000000,#000000,#000000,#000000")));

        String width = getAttribute(node, XmlNode.Field.Attribute.WIDTH);
        builder.width(Integer.parseInt(StringUtils.defaultIfEmpty(width, "0")));

        field.fieldCellStyle(builder.build());
    }

    private void setHeaderCellStyleConfig(Field.FieldBuilder field, Node node) {
        FieldCellStyle.FieldCellStyleBuilder builder = FieldCellStyle.builder();

        String fontSize = getAttribute(node, XmlNode.Header.Attribute.HEADER_FONT_SIZE);
        builder.fontSize(Short.parseShort(StringUtils.defaultIfEmpty(fontSize, "15")));

        String color = getAttribute(node, XmlNode.Header.Attribute.HEADER_COLOR);
        builder.color(ColorUtils.hex2Rgb(StringUtils.defaultIfEmpty(color, "#000000")));

        String isBold = getAttribute(node, XmlNode.Header.Attribute.IS_HEADER_BOLD);
        builder.bold(Boolean.parseBoolean(StringUtils.defaultIfEmpty(isBold, "true")));

        String foregroundColor = getAttribute(node, XmlNode.Header.Attribute.HEADER_FOREGROUND_COLOR);
        builder.foregroundColor(ColorUtils.hex2Rgb(StringUtils.defaultIfEmpty(foregroundColor, "#FFFFFF")));

        String border = getAttribute(node, XmlNode.Header.Attribute.HEADER_BORDER);
        builder.border(super.convertBorder(StringUtils.defaultIfEmpty(border, "1,1,1,1")));

        String borderColor = getAttribute(node, XmlNode.Header.Attribute.HEADER_BORDER_COLOR);
        builder.borderColor(super.convertBorderColor(StringUtils.defaultIfEmpty(borderColor, "#000000,#000000,#000000,#000000")));

        //handle width in cellStyle rather than headerCellStyle
        builder.width(0);

        field.headerFieldCellStyle(builder.build());
    }

    private void setImportValidation(Field.FieldBuilder field, Node node) {
        String isBlankable = getAttribute(node, XmlNode.Field.Attribute.IS_BLANKABLE);
        boolean blankable = true;
        List<String> options = null;
        Pattern regex = null;

        if (StringUtils.isNotEmpty(isBlankable)) {
            blankable = Boolean.parseBoolean(isBlankable);
        }

        String regexStr = getAttribute(node, XmlNode.Field.Attribute.REGEX);
        if (!StringUtils.isEmpty(regexStr)) {
            regex = Pattern.compile(regexStr);
        }
        String optionsStr = getAttribute(node, XmlNode.Field.Attribute.OPTIONS);
        if (!StringUtils.isEmpty(optionsStr) && optionsStr.length() >= 2) {
            String[] split = optionsStr.split(StringUtils.OPTION_SPLITTER_VERTICAL);
            options = Arrays.asList(split);
        }

        field.importValidation(ImportValidation.of(blankable, options, regex));
    }

}
