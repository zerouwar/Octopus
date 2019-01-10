package cn.chenhuanming.octopus.model;


import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author chenhuanming
 * Created at 2018/12/17
 */
public abstract class AbstractXMLConfigReader extends CachedConfigReader {


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
                /**
                 * like Field.Attribute,including:
                 * String NAME = "name";
                 * String DESCRIPTION = "description";
                 * String FONT_SIZE = "fontSize";
                 * String COLOR = "color";
                 * String IS_BOLD = "isBold";
                 * String BACKGROUND_COLOR = "backgroundColor";
                 * String DATE_FORMAT = "dateFormat";
                 * String FORMATTER = "formatter";
                 */
            }
        }

        interface Field {
            String name = "Field";

            interface Attribute {
                String NAME = "name";
                String DESCRIPTION = "description";
                String FONT_SIZE = "fontSize";
                String COLOR = "color";
                String IS_BOLD = "isBold";
                String BACKGROUND_COLOR = "backgroundColor";
                String DATE_FORMAT = "dateFormat";
                String FORMATTER = "formatter";
                String IS_BLANKABLE = "isBlankable";
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
                String DATE_FORMAT = "dateFormat";
            }
        }
    }
}
