package cn.chenhuanming.octopus.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author chenhuanming
 * Created at 2018/12/15
 */
@Data
@AllArgsConstructor
public class DefaultCellPosition implements CellPosition {
    private int row;
    private int col;
}
