package cn.chenhuanming.octopus.core;

/**
 * Created by chenhuanming on 2017-07-03.
 *
 * @author chenhuanming
 */
public abstract class AbstractExcelWriter<T> implements ExcelWriter<T> {
    protected final SheetWriter<T> sheetWriter;

    public AbstractExcelWriter(SheetWriter<T> sheetWriter) {
        if(sheetWriter==null)
            throw new IllegalStateException("sheetWriter can not be null!");
        this.sheetWriter = sheetWriter;
    }

}
