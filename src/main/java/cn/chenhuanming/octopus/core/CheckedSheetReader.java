package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.exception.CanNotBeBlankException;
import cn.chenhuanming.octopus.exception.NotAllowValueException;
import cn.chenhuanming.octopus.exception.ParseException;
import cn.chenhuanming.octopus.exception.PatternNotMatchException;
import cn.chenhuanming.octopus.model.*;
import cn.chenhuanming.octopus.util.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * not thread-safe
 * @author chenhuanming
 * Created at 2019-01-09
 */
public class CheckedSheetReader<T> extends DefaultSheetReader<CheckedData<T>> {
    /**
     * not thread-safe
     */
    private CheckedData<T> checkedData;

    public CheckedSheetReader(Sheet sheet, ConfigReader configReader, CellPosition startPoint) {
        super(sheet, configReader, startPoint);
    }

    @Override
    int read(int row, int col, Field field, Object o) {
        if (o instanceof CheckedData) {
            return super.read(row, col, field, ((CheckedData) o).getData());
        }
        return super.read(row, col, field, o);
    }

    @Override
    protected void setValue(String str, Field field, Object o) throws ParseException {
        if (!field.isBlankable() && StringUtils.isEmpty(str)) {
            throw new CanNotBeBlankException();
        }

        if (field.getOptions() != null && field.getOptions().size() > 0 && !field.getOptions().contains(str)) {
            throw new NotAllowValueException(field.getOptions());
        }

        if (field.getRegex() != null && !field.getRegex().matcher(str).matches()) {
            throw new PatternNotMatchException(field.getRegex());
        }

        super.setValue(str, field, o);
    }

    @Override
    protected void failWhenParse(int row, int col, Field field, ParseException e) {
        e.setField(field);
        e.setCellPosition(new DefaultCellPosition(row, col));
        checkedData.getExceptions().add(e);
    }

    @Override
    protected CheckedData<T> newInstance(Class classType) {
        try {
            T data = (T) configReader.getConfig().getClassType().newInstance();
            checkedData = new CheckedData<>();
            checkedData.setData(data);
            return checkedData;
        } catch (Exception e) {
            throw new IllegalArgumentException("wrong type or no default constructor", e);
        }
    }

}
