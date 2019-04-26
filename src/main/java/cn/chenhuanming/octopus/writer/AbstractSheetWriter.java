package cn.chenhuanming.octopus.writer;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.ConfigFactory;
import cn.chenhuanming.octopus.config.Field;
import cn.chenhuanming.octopus.formatter.Formatter;
import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.DefaultCellPosition;
import cn.chenhuanming.octopus.model.WorkbookContext;
import cn.chenhuanming.octopus.util.CellUtils;
import cn.chenhuanming.octopus.util.ReflectionUtils;
import cn.chenhuanming.octopus.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;

/**
 * Created by chenhuanming on 2017-07-01.
 *
 * @author chenhuanming
 */
@Slf4j
public abstract class AbstractSheetWriter<T> implements SheetWriter<T> {
    protected ConfigFactory configFactory;
    protected HeaderWriter headerWriter;
    protected CellPosition startPoint;

    public AbstractSheetWriter(ConfigFactory configFactory, HeaderWriter headerWriter, CellPosition startPoint) {
        this.configFactory = configFactory;
        this.headerWriter = headerWriter;
        this.startPoint = startPoint;
    }

    @Override
    public CellPosition write(Sheet sheet, Collection<T> data) {
        if (!canWrite(sheet, data)) {
            return CellUtils.POSITION_ZERO_ZERO;
        }

        Config config = configFactory.getConfig();

        Class dataType = data.iterator().next().getClass();
        if (config.getClassType() != dataType) {
            throw new IllegalArgumentException("class of config is " + config.getClassType().getName() + " but type of data is " + dataType.getName());
        }

        CellPosition end = headerWriter.drawHeader(sheet, startPoint, config.getFields());

        int row = end.getRow() + 1;
        int col = getStartColumn();
        WorkbookContext bookResource = new WorkbookContext(sheet.getWorkbook());

        for (T t : data) {
            for (Field field : config.getFields()) {
                col = draw(sheet, row, col, field, t, bookResource);
            }
            col = getStartColumn();
            row++;
        }
        return new DefaultCellPosition(row, end.getCol());
    }

    protected int getStartColumn() {
        return 0;
    }

    private int draw(Sheet sheet, final int row, final int col, Field field, Object o, WorkbookContext bookResource) {
        if (field.isLeaf()) {
            return drawColumn(sheet, row, col, field, o, bookResource);
        }

        Object p = null;
        if (o != null) {
            p = ReflectionUtils.invokeReadMethod(field.getPicker(), o);
        }
        int c = col;
        for (Field child : field.getChildren()) {
            c = draw(sheet, row, c, child, p, bookResource);
        }
        return c;
    }

    protected int drawColumn(Sheet sheet, final int row, final int col, Field field, Object o, WorkbookContext bookResource) {
        if (o == null) {
            return col + 1;
        }
        String value;
        Formatter formatter = field.getFormatter();
        if (formatter != null) {
            value = formatter.format(ReflectionUtils.invokeReadMethod(field.getPicker(), o));
            CellUtils.setCellValue(sheet, row, col, value, bookResource.getCellStyle(field));
            return col + 1;
        }

        formatter = configFactory.getConfig().getFormatterContainer().get(field.getPicker().getReturnType());

        if (field.getPicker().getReturnType() == String.class || formatter == null) {
            value = ReflectionUtils.invokeReadMethod(field.getPicker(), o, field.getDefaultValue());
            CellUtils.setCellValue(sheet, row, col, value, bookResource.getCellStyle(field));
            return col + 1;
        }
        //导出时要么是 Date 类型使用 dateFormatter 要么不是 Date 类型，使用 formatter 那么其实可以只用 formatter 字段表示
        value = formatter.format(ReflectionUtils.invokeReadMethod(field.getPicker(), o));

        if (StringUtils.isEmpty(value)) {
            value = field.getDefaultValue();
        }
        CellUtils.setCellValue(sheet, row, col, value, bookResource.getCellStyle(field));
        return col + 1;

    }

    protected boolean canWrite(Sheet sheet, Collection<T> collection) {
        if (collection != null && collection.size() > 0) {
            return true;
        }
        return false;
    }
}
