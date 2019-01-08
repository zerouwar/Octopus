package cn.chenhuanming.octopus.core;


import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.ConfigReader;
import cn.chenhuanming.octopus.model.Field;
import cn.chenhuanming.octopus.util.CellUtils;
import cn.chenhuanming.octopus.util.ReflectionUtils;
import org.apache.poi.ss.usermodel.Sheet;


/**
 * @author chenhuanming
 * Created at 2019-01-06
 */
public class DefaultSheetReader<T> extends AbstractSheetReader<T> {

    public DefaultSheetReader(Sheet sheet, ConfigReader configReader, CellPosition startPoint) {
        super(sheet, configReader, startPoint);
    }

    @Override
    public T get(int i) {
        T t = null;
        try {
            t = (T) configReader.getConfig().getClassType().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("wrong type or no default constructor", e);
        }

        int col = startPoint.getCol();
        for (Field field : configReader.getConfig().getFields()) {
            col = read(startPoint.getRow() + i, col, field, t);
        }
        return t;

    }

    private int read(int row, int col, final Field field, Object o) {
        if (field.isLeaf()) {
            String str = CellUtils.getCellValue(sheet, row, col, field.getDefaultValue());

            setValue(str, field, o);

            return col + 1;
        }

        Object instance = ReflectionUtils.newInstance(field.getPusher().getParameterTypes()[0]);
        for (Field child : field.getChildren()) {
            if (instance != null) {
                col = read(row, col, child, instance);
                try {
                    field.getPusher().invoke(o, instance);
                } catch (Exception e) {
                    LOGGER.error("failed to set " + instance + " into " + o, e);
                }
            }
        }
        return col;
    }
}
