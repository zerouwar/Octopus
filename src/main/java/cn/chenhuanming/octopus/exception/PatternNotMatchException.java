package cn.chenhuanming.octopus.exception;

import cn.chenhuanming.octopus.model.ModelEntityWithMethodHandle;
import lombok.Getter;

/**
 * pattern不匹配
 * @author chenhuanming
 */
@Getter
public class PatternNotMatchException extends ExcelImportException {
    private String pattern;

    public PatternNotMatchException(ModelEntityWithMethodHandle handle) {
        super(handle.getPattern().get().pattern()+"is not match!", handle);
        this.pattern = handle.getPattern().get().pattern();
    }
}
