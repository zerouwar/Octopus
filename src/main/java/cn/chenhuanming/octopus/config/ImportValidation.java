package cn.chenhuanming.octopus.config;

import lombok.Value;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
@Value(staticConstructor = "of")
public class ImportValidation {
    protected boolean blankable;
    protected List<String> options;
    protected Pattern regex;
}
