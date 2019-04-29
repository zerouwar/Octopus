package cn.chenhuanming.octopus.model;

/**
 * @author chenhuanming
 * Created at 2018/12/14
 */

public interface CellPosition {
    /**
     * begin with 0,same as java poi
     * @return row index
     */
    int getRow();

    /**
     * begin with 0,same as java poi
     * @return column index
     */
    int getCol();
}
