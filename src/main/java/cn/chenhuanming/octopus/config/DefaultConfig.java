package cn.chenhuanming.octopus.config;

import cn.chenhuanming.octopus.formatter.FormatterContainer;
import lombok.Data;

import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/7
 */
@Data
public class DefaultConfig implements Config {
    private Class classType;
    private FormatterContainer formatterContainer;
    private List<Field> fields;
}
