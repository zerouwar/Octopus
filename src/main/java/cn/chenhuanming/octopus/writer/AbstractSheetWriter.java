package cn.chenhuanming.octopus.writer;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.Field;
import cn.chenhuanming.octopus.formatter.Formatter;
import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.CellPositions;
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
    protected Config config;
    protected HeaderWriter headerWriter;
    protected CellPosition startPoint;

    public AbstractSheetWriter(Config config, HeaderWriter headerWriter, CellPosition startPoint) {
        this.config = config;
        this.headerWriter = headerWriter;
        this.startPoint = startPoint;
    }

    /**
     * Writes the content after writing the header.
     *
     * @param sheet           sheet
     * @param data            data
     * @param headerEndPos    header last position
     * @param workbookContext workbook context
     * @return content last position
     */
    abstract CellPosition writeContent(Sheet sheet, Collection<T> data, CellPosition headerEndPos, WorkbookContext workbookContext);

    @Override
    public CellPosition write(Sheet sheet, Collection<T> data) {
        if (data == null || data.size() == 0) {
            return CellPositions.POSITION_ZERO_ZERO;
        }

        Class dataType = data.iterator().next().getClass();
        if (config.getClassType() != dataType) {
            throw new IllegalArgumentException("class of config is " + config.getClassType().getName() + " but type of data is " + dataType.getName());
        }

        WorkbookContext workbookContext = new WorkbookContext(sheet.getWorkbook());

        CellPosition headerEndPos = headerWriter.drawHeader(sheet, startPoint, config.getFields());

        return writeContent(sheet, data, headerEndPos, workbookContext);
    }

    /**
     * Writes field recursively in the DFS(Depth First Search)
     *
     * @param sheet           sheet
     * @param row             row index,starts from 0
     * @param col             column index,starts from 0
     * @param field           field config
     * @param o               data object
     * @param workbookContext workbook context
     * @return next column index,aka col+1
     */
    protected int writeField(Sheet sheet, final int row, final int col, Field field, Object o, WorkbookContext workbookContext) {
        if (field.isLeaf()) {
            writeCell(sheet, row, col, field, o, workbookContext);
            return col + 1;
        }

        Object value = null;
        if (o != null) {
            value = ReflectionUtils.invokeReadMethod(field.getPicker(), o);
        }
        int c = col;
        for (Field child : field.getChildren()) {
            c = writeField(sheet, row, c, child, value, workbookContext);
        }
        return c;
    }

    /**
     * Writes one cell with formatter of field config.
     * It is convenient when you customize sheet content with field config.
     *
     * @param sheet           sheet
     * @param row             row index,starts from 0
     * @param col             column index,starts from 0
     * @param field           field config
     * @param data            data value
     * @param workbookContext workbook context
     */
    protected void writeCell(Sheet sheet, final int row, final int col, Field field, Object data, WorkbookContext workbookContext) {
        if (data == null) {
            return;
        }

        //Apply special formatter if not null
        if (field.getFormatter() != null) {
            String value = field.getFormatter().format(ReflectionUtils.invokeReadMethod(field.getPicker(), data));
            CellUtils.setCellValue(sheet, row, col, value, workbookContext.getCellStyle(field));
            return;
        }

        //Try Applying global formatter of container
        Formatter formatter = config.getFormatterContainer().get(field.getPicker().getReturnType());

        //Set data.toString() if no formatter
        if (formatter == null) {
            String value = ReflectionUtils.invokeReadMethod(field.getPicker(), data, field.getDefaultValue());
            CellUtils.setCellValue(sheet, row, col, value, workbookContext.getCellStyle(field));
            return;
        }

        //Apply formatter and set value
        String value = formatter.format(ReflectionUtils.invokeReadMethod(field.getPicker(), data));
        CellUtils.setCellValue(sheet, row, col, StringUtils.isEmpty(value) ? field.getDefaultValue() : value, workbookContext.getCellStyle(field));
    }
}
