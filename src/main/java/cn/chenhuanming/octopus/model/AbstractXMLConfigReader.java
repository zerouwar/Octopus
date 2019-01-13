package cn.chenhuanming.octopus.model;


import cn.chenhuanming.octopus.util.ColorUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.util.IOUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author chenhuanming
 * Created at 2018/12/17
 */
public abstract class AbstractXMLConfigReader extends CachedConfigReader {

    protected final ByteArrayInputStream is;

    protected static final String SPLITTER = "\\|";

    public AbstractXMLConfigReader(InputStream is) {
        try {
            this.is = new ByteArrayInputStream(IOUtils.toByteArray(is));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static final String SCHEMA_URI = "https://raw.githubusercontent.com/zerouwar/my-maven-repo/master/cn/chenhuanming/octopus/1.0.0/octopus.xsd";

    protected void validateXML(Source source) throws Exception {
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new URL(SCHEMA_URI));
        Validator validator = schema.newValidator();
        validator.validate(source);
        is.reset();
    }

    protected String getAttribute(Node node, String name) {
        if (node == null) {
            return null;
        }
        NamedNodeMap attributes = node.getAttributes();
        Node item = attributes.getNamedItem(name);
        if (item == null) {
            return null;
        }
        return item.getNodeValue();
    }

    protected BorderStyle[] convertBorder(String border) {
        BorderStyle[] result = new BorderStyle[4];
        String[] split = border.split(",");
        for (int i = 0; i < split.length; i++) {
            short val = Short.parseShort(split[i]);
            BorderStyle style = BorderStyle.valueOf(val);
            result[i] = style;
        }
        return result;
    }

    protected Color[] convertBorderColor(String borderColor) {
        Color[] result = new Color[4];
        String[] split = borderColor.split(",");
        for (int i = 0; i < split.length; i++) {
            Color color = ColorUtils.hex2Rgb(split[i]);
            result[i] = color;
        }
        return result;
    }

    /**
     * xml config constant
     */
    protected interface XMLConfig {

        interface Root {
            String name = "Root";

            interface Attribute {
                String CLASS = "class";
            }
        }

        interface Header {
            String name = "Header";

            interface Attribute {
                String NAME = "name";
                String DESCRIPTION = "description";
                String HEADER_FONT_SIZE = "header-font-size";
                String HEADER_COLOR = "header-color";
                String IS_HEADER_BOLD = "header-is-bold";
                String HEADER_FOREGROUND_COLOR = "header-foreground-color";
                String HEADER_BORDER = "header-border";
                String HEADER_BORDER_COLOR = "header-border-color";
            }
        }

        interface Field {
            String name = "Field";

            interface Attribute extends Header.Attribute {
                String FONT_SIZE = "font-size";
                String COLOR = "color";
                String IS_BOLD = "is-bold";
                String FOREGROUND_COLOR = "foreground-color";
                String BORDER = "border";
                String BORDER_COLOR = "border-color";

                String DATE_FORMAT = "date-format";
                String FORMATTER = "formatter";
                String IS_BLANKABLE = "is-blankable";
                String REGEX = "regex";
                String OPTIONS = "options";
            }
        }

        interface Formatters {
            String name = "Formatters";

            interface Formatter {
                String name = "Formatter";

                interface Attribute {
                    String TARGET = "target";
                    String CLASS = "class";
                }
            }

            interface Attribute {
                String DATE_FORMAT = "date-format";
            }
        }
    }
}
