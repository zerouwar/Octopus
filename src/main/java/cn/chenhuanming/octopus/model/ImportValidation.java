package cn.chenhuanming.octopus.model;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
public interface ImportValidation {
    boolean isBlankable();

    Pattern getRegex();

    List<String> getOptions();
}
