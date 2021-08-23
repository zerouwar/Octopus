package cn.chenhuanming.octopus.model;

/**
 * Cell position.
 *
 * @author chenhuanming
 * Created at 2018/12/14
 * @see CellPositions
 */

public interface CellPosition {
    /**
     * Begin with 0,same as java poi
     *
     * @return row index
     */
    int getRow();

    /**
     * Begin with 0,same as java poi
     *
     * @return column index
     */
    int getCol();
}
