package cn.chenhuanming.octopus.exception;

import cn.chenhuanming.octopus.model.ModelEntityWithMethodHandle;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;

/**
 * pattern不匹配
 * @author chenhuanming
 */
@Getter
public class PatternNotMatchException extends ExcelImportException {
    private String pattern;

    public PatternNotMatchException(ModelEntityWithMethodHandle handle, Cell cell) {
        super(handle.getPattern().get().pattern()+"is not match!", handle,cell);
        this.pattern = handle.getPattern().get().pattern();
    }
}
