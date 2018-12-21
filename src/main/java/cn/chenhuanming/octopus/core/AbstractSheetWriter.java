package cn.chenhuanming.octopus.core;


import cn.chenhuanming.octopus.exception.DrawSheetException;
import cn.chenhuanming.octopus.model.*;
import cn.chenhuanming.octopus.util.CellUtils;
import cn.chenhuanming.octopus.util.ReflectionUtils;
import com.google.common.base.Strings;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;

/**
 * Created by chenhuanming on 2017-07-01.
 *
 * @author chenhuanming
 */
public abstract class AbstractSheetWriter<T> implements SheetWriter<T> {
    protected ConfigReader configReader;
    protected HeaderWriter headerWriter;
    protected CellPosition startPoint;

    public AbstractSheetWriter(ConfigReader configReader, HeaderWriter headerWriter, CellPosition startPoint) {
        this.configReader = configReader;
        this.headerWriter = headerWriter;
        this.startPoint = startPoint;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSheetWriter.class);

    @Override
    public CellPosition write(Sheet sheet, Collection<T> data) throws DrawSheetException {
        if (!canWrite(sheet, data)) {
            return CellUtils.POSITION_ZERO_ZERO;
        }

        Config config = configReader.getConfig();

        Class dataType = data.iterator().next().getClass();
        if (config.getClazz() != dataType) {
            throw new IllegalArgumentException("class of config is " + config.getClazz().getName() + " but type of data is " + dataType.getName());
        }

        CellPosition end = headerWriter.drawHeader(sheet, startPoint, config.getFields());

        int row = end.getRow() + 1;
        int col = getStartColnum();

        for (T t : data) {
            for (Field field : config.getFields()) {
                col = draw(sheet, row, col, field, t);
            }
            col = getStartColnum();
            row++;
        }
        return new DefaultCellPosition(row, end.getCol());
    }

    protected int getStartColnum() {
        return 0;
    }

    protected int draw(Sheet sheet, final int row, final int col, Field field, Object o) {
        if (field.isLeaf()) {
            String value = field.getDefaultValue();
            if (o != null) {

                if (field.getFormatter() != null) {
                    value = field.getFormatter().format(ReflectionUtils.invokeGetter(field.getPicker(), o));
                    CellUtils.setCellValue(sheet, row, col, value, field.getCellStyle(sheet.getWorkbook()));
                    return col + 1;
                }

                CellFormatter cellFormatter = configReader.getConfig().getCellFormatterMap().get(field.getPicker().getReturnType());

                if (field.getPicker().getReturnType() == String.class || cellFormatter == null) {
                    value = ReflectionUtils.invokeGetter(field.getPicker(), o, field.getDefaultValue());
                    CellUtils.setCellValue(sheet, row, col, value, field.getCellStyle(sheet.getWorkbook()));
                    return col + 1;
                }
                if (field.getPicker().getReturnType() == Date.class && field.getDateFormat() != null) {
                    value = field.getDateFormat().format((Date) ReflectionUtils.invokeGetter(field.getPicker(), o));
                    if (Strings.isNullOrEmpty(value)) {
                        value = field.getDefaultValue();

                    }
                    CellUtils.setCellValue(sheet, row, col, value, field.getCellStyle(sheet.getWorkbook()));
                    return col + 1;
                }

                value = cellFormatter.format(ReflectionUtils.invokeGetter(field.getPicker(), o));
                if (Strings.isNullOrEmpty(value)) {
                    value = field.getDefaultValue();
                }
                CellUtils.setCellValue(sheet, row, col, value, field.getCellStyle(sheet.getWorkbook()));
                return col + 1;

            }
            return col + 1;
        }

        Object p = null;
        if (o != null) {
            p = ReflectionUtils.invokeGetter(field.getPicker(), o);
        }
        int c = col;
        for (Field child : field.getChildren()) {
            c = draw(sheet, row, c, child, p);
        }
        return c;
    }

    protected boolean canWrite(Sheet sheet, Collection<T> collection) {
        if (collection != null && collection.size() > 0) {
            return true;
        }
        return false;
    }
}
