package cn.chenhuanming.octopus.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
public class NotAllowValueException extends ParseException {
    @Getter
    private final List<String> options;

    public NotAllowValueException(List<String> options) {
        this.options = Collections.unmodifiableList(options);
    }
}
