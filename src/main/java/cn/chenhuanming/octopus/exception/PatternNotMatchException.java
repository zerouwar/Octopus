package cn.chenhuanming.octopus.exception;

import lombok.Getter;

import java.util.regex.Pattern;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
public class PatternNotMatchException extends ParseException {
    @Getter
    private final Pattern pattern;

    public PatternNotMatchException(Pattern pattern) {
        this.pattern = pattern;
    }
}
