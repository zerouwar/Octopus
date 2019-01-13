package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.model.formatter.Formatter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author chenhuanming
 * Created at 2018/12/15
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class DefaultField extends FieldStyle implements Field {

    protected String name;
    protected String description;
    protected String defaultValue;
    protected Formatter<Date> dateFormat;
    protected Formatter formatter;

    protected Method picker;
    protected Method pusher;
    protected boolean blankable;
    protected List<String> options;
    protected Pattern regex;
    protected List<Field> children;

    public DefaultField() {
        description = "";
        defaultValue = null;
        blankable = true;
        children = new ArrayList<>();
    }

    public DefaultField(List<Field> children) {
        this();
        this.setChildren(children);
    }

    @Override
    public boolean isLeaf() {
        return getChildren() == null || getChildren().size() == 0;
    }

    public DefaultField addChildren(Field field) {
        getChildren().add(field);
        return this;
    }
}
