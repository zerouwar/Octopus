package cn.chenhuanming.octopus.config;

import lombok.Data;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
@Data
public class ImportValidation {
    protected boolean blankable;
    protected List<String> options;
    protected Pattern regex;
}
