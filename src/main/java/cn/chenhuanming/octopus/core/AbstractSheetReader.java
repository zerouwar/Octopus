package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.ConfigReader;
import cn.chenhuanming.octopus.model.Field;
import cn.chenhuanming.octopus.model.formatter.Formatter;
import cn.chenhuanming.octopus.model.formatter.ParseException;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * @author chenhuanming
 * Created at 2018/12/20
 */
public abstract class AbstractSheetReader<T> implements SheetReader<T> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractSheetReader.class);

    protected Sheet sheet;
    protected ConfigReader configReader;
    protected CellPosition startPoint;

    public AbstractSheetReader(Sheet sheet, ConfigReader configReader, CellPosition startPoint) {
        this.sheet = sheet;
        this.configReader = configReader;
        this.startPoint = startPoint;
    }

    /**
     * set value into object
     *
     * @param str
     * @param field
     * @param o
     */
    protected void setValue(String str, Field field, Object o) {
        Method pusher = field.getPusher();

        Object value = null;

        try {
            if (field.getFormatter() != null) {
                value = field.getFormatter().parse(str);
            } else {
                Formatter globalFormatter = configReader.getConfig().getFormatterContainer().get(pusher.getParameterTypes()[0]);
                if (globalFormatter != null) {
                    value = globalFormatter.parse(str);
                } else {
                    value = str;
                }
            }
        } catch (ParseException e) {
            LOGGER.debug(field.getName() + " parse failed:" + str);
        }

        try {
            if (value != null) {
                pusher.invoke(o, value);
            }
        } catch (Exception e) {
            LOGGER.error("can not set value:" + field.getName(), e);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new SheetIterator<T>(sheet.getLastRowNum(), startPoint.getRow());
    }

    private class SheetIterator<T> implements Iterator<T> {
        private int last;
        private int cursor;

        public SheetIterator(int last, int cursor) {
            this.last = last;
            this.cursor = cursor;
        }

        @Override
        public boolean hasNext() {
            return cursor <= last;
        }

        @Override
        public T next() {
            return (T) get(cursor++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }
}
