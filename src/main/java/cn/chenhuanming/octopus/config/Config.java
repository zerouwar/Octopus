package cn.chenhuanming.octopus.config;

import cn.chenhuanming.octopus.formatter.FormatterContainer;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/7
 */
@Value(staticConstructor = "of")
public class Config {
    @NonNull
    private Class classType;
    @NonNull
    private FormatterContainer formatterContainer;
    @NonNull
    private List<Field> fields;
}
