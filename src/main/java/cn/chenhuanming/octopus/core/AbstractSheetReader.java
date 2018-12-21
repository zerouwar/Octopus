package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.Config;
import cn.chenhuanming.octopus.model.ConfigReader;
import cn.chenhuanming.octopus.model.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenhuanming
 * Created at 2018/12/20
 */
public abstract class AbstractSheetReader<T> implements SheetReader<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSheetReader.class);

    protected ConfigReader configReader;
    protected CellPosition startPoint;

    @Override
    public T get(int i) {
        int row = startPoint.getRow() + i;

        Config config = configReader.getConfig();

        T t = null;
        try {
            t = (T) config.getClazz().newInstance();
        } catch (Exception e) {
            LOGGER.error("can not new instance:", e);
        }
        if (t == null) {
            return null;
        }
        // todo: chenhuanming at 2018/12/21,临时保存
        return null;
    }

    private Object readCell(int row, int col, Field field, Object o) {
        if (field.isLeaf()) {
            return null;
        }
        return null;
    }
}
