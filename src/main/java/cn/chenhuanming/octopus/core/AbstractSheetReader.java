package cn.chenhuanming.octopus.core;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;

/**
 * Created by Administrator on 2017-06-10.
 */
public abstract class AbstractSheetReader<T> implements SheetReader<T>{
    protected final Sheet sheet;
    protected final int startRow;
    protected final int startCol;
    protected final int lastRow;

    public AbstractSheetReader(Sheet sheet, int startRow, int startCol) {
        this.sheet = sheet;
        this.startRow = startRow;
        this.startCol = startCol;
        lastRow = sheet.getLastRowNum();
    }

    public AbstractSheetReader(Sheet sheet) {
        this(sheet,1,0);
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    protected class Itr implements Iterator<T>{
        private int index;

        protected Itr() {
            index = startRow;
        }


        @Override
        public boolean hasNext() {
            return index<=lastRow?true:false;
        }

        @Override
        public T next() {
            return get(index++);
        }
    }
}
