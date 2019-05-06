package cn.chenhuanming.octopus.reader;

import cn.chenhuanming.octopus.config.ConfigFactory;
import cn.chenhuanming.octopus.config.Field;
import cn.chenhuanming.octopus.exception.ParseException;
import cn.chenhuanming.octopus.formatter.Formatter;
import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.util.CellUtils;
import cn.chenhuanming.octopus.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Date;

/**
 * @author chenhuanming
 * Created at 2019-01-06
 */
@Slf4j
public class DefaultSheetReader<T> extends AbstractSheetReader<T> {

    public DefaultSheetReader(Sheet sheet, ConfigFactory configFactory, CellPosition startPoint) {
        super(sheet, configFactory, startPoint);
    }

    @Override
    int read(int row, int col, Field field, Object o) {
        if (field.isLeaf()) {

            try {
                Cell cell = sheet.getRow(row).getCell(col);
                String str;
                if (CellUtils.isDate(cell)) {
                    Formatter<Date> dateFormatter = field.getDateFormat();
                    if (dateFormatter == null) {
                        dateFormatter = configFactory.getConfig().getFormatterContainer().get(Date.class);
                    }
                    str = dateFormatter.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                } else {
                    str = CellUtils.getCellValue(sheet, row, col, field.getDefaultValue());
                }

                setValue(str, field, o);
            } catch (ParseException e) {
                failWhenParse(row, col, field, e);
            }

            return col + 1;
        }

        Object instance = ReflectionUtils.newInstance(field.getPusher().getParameterTypes()[0]);
        for (Field child : field.getChildren()) {
            if (instance != null) {
                col = read(row, col, child, instance);
                try {
                    field.getPusher().invoke(o, instance);
                } catch (Exception e) {
                    log.error("failed to set " + instance + " into " + o, e);
                }
            }
        }
        return col;
    }

    protected void failWhenParse(int row, int col, final Field field, ParseException e) {
        log.error("failed to read value from " + field.getName() + " in excel(" + (row + 1) + "," + col + ")", e);
    }
}
