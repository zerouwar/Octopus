package cn.chenhuanming.octopus.reader;

/**
 * Sheet Reader
 * Created by Administrator on 2017-06-10.
 */
public interface SheetReader<T> extends Iterable<T> {
    /**
     * Get data from data index i.
     * i is data index,not the sheet row index
     *
     * @param i data index
     * @return data
     */
    T get(int i);

    /**
     * Data count.
     *
     * @return data count
     */
    int size();
}
