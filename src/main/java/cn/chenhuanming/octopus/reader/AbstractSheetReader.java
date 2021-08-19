package cn.chenhuanming.octopus.reader;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.Field;
import cn.chenhuanming.octopus.exception.ParseException;
import cn.chenhuanming.octopus.formatter.Formatter;
import cn.chenhuanming.octopus.model.CellPosition;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * @author chenhuanming
 * Created at 2018/12/20
 */
@Slf4j
public abstract class AbstractSheetReader<T> implements SheetReader<T> {

    protected Sheet sheet;
    protected Config config;
    protected CellPosition startPoint;

    public AbstractSheetReader(Sheet sheet, Config config, CellPosition startPoint) {
        if (sheet == null || config == null || startPoint == null) {
            throw new NullPointerException();
        }
        this.sheet = sheet;
        this.config = config;
        this.startPoint = startPoint;
    }

    @Override
    public T get(int i) {
        T t = newInstance(config.getClassType());

        int col = startPoint.getCol();
        for (Field field : config.getFields()) {
            col = read(startPoint.getRow() + i, col, field, t);
        }
        return t;
    }

    /**
     * set value into object
     */
    protected void setValue(String str, Field field, Object o) throws ParseException {
        Method pusher = field.getPusher();

        Object value = null;

        if (field.getFormatter() != null) {
            value = field.getFormatter().parse(str);
        } else {
            Formatter globalFormatter = config.getFormatterContainer().get(pusher.getParameterTypes()[0]);
            if (globalFormatter != null) {
                value = globalFormatter.parse(str);
            } else {
                value = str;
            }
        }

        try {
            if (value != null) {
                pusher.invoke(o, value);
            }
        } catch (Exception e) {
            log.error("can not set value:" + field.getName(), e);
            throw new ParseException("invoke method failed", e);
        }
    }

    protected T newInstance(Class classType) {
        try {
            return (T) config.getClassType().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("wrong type or no default constructor", e);
        }
    }

    @Override
    public int size() {
        return sheet.getLastRowNum() - startPoint.getRow() + 1;
    }

    abstract int read(int row, int col, Field field, Object o);

    @Override
    public Iterator<T> iterator() {
        return new RowIterator<T>(sheet.getLastRowNum() - startPoint.getRow(), 0);
    }

    private class RowIterator<T> implements Iterator<T> {
        private int last;
        private int cursor;

        public RowIterator(int last, int cursor) {
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
