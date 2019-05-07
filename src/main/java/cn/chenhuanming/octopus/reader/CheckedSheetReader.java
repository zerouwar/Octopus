package cn.chenhuanming.octopus.reader;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.Field;
import cn.chenhuanming.octopus.config.ImportValidation;
import cn.chenhuanming.octopus.exception.CanNotBeBlankException;
import cn.chenhuanming.octopus.exception.NotAllowValueException;
import cn.chenhuanming.octopus.exception.ParseException;
import cn.chenhuanming.octopus.exception.PatternNotMatchException;
import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.CheckedData;
import cn.chenhuanming.octopus.model.DefaultCellPosition;
import cn.chenhuanming.octopus.util.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * not thread-safe
 *
 * @author chenhuanming
 * Created at 2019-01-09
 */
public class CheckedSheetReader<T> extends DefaultSheetReader<CheckedData<T>> {
    /**
     * not thread-safe
     */
    private CheckedData<T> checkedData;

    public CheckedSheetReader(Sheet sheet, Config config, CellPosition startPoint) {
        super(sheet, config, startPoint);
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
        ImportValidation validation = field.getImportValidation();
        if (!validation.isBlankable() && StringUtils.isEmpty(str)) {
            throw new CanNotBeBlankException();
        }

        if (validation.getOptions() != null && validation.getOptions().size() > 0 && !validation.getOptions().contains(str)) {
            throw new NotAllowValueException(validation.getOptions());
        }

        if (validation.getRegex() != null && !validation.getRegex().matcher(str).matches()) {
            throw new PatternNotMatchException(validation.getRegex());
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
            T data = (T) config.getClassType().newInstance();
            checkedData = new CheckedData<>();
            checkedData.setData(data);
            return checkedData;
        } catch (Exception e) {
            throw new IllegalArgumentException("wrong type or no default constructor", e);
        }
    }

}
