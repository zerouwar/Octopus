package cn.chenhuanming.octopus.util;

import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by chenhuanming on 2017-06-14.
 *
 * @author chenhuanming
 */
@Data
public class CellPosition {
    private int rowNum;
    private int colNum;

    public CellPosition(Cell cell) {
        this.rowNum = cell.getRowIndex()+1;
        this.colNum = cell.getColumnIndex()+1;
    }

    public CellPosition(int rowNumInPOI, int colNumInPOI) {
        this.rowNum = rowNumInPOI+1;
        this.colNum = colNumInPOI+1;
    }

    @Override
    public String toString(){
        return String.format("cell (%d,%d) ",rowNum,colNum);
    }
}
