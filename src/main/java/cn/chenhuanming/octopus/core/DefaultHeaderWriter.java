package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.*;
import cn.chenhuanming.octopus.util.CellUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/15
 */
public class DefaultHeaderWriter implements HeaderWriter {
    @Override
    public CellPosition drawHeader(Sheet sheet, CellPosition startPoint, List<Field> fields) {
        DefaultField fake = new DefaultField(fields);

        SupportHeader fakeHeader = new SupportHeader(fake);

        int row = startPoint.getRow() - 1;
        int col = startPoint.getCol();

        int lastRow = row + fakeHeader.getHeight() - 1;
        int lastCol = col + fakeHeader.getWidth() - 1;

        drawHeaderImpl(sheet, row, lastRow, col, lastCol, fakeHeader);

        return new DefaultCellPosition(lastRow, lastCol);
    }

    private Cost drawHeaderImpl(Sheet sheet, int row, int lastRow, int col, int lastCol, SupportHeader header) {
        if (header.isLeaf()) {
            //set value into the bottom left cell
            if (header.getHeight() == 1) {
                CellUtils.setCellValue(sheet, lastRow, col, header.getDescription(), header.getHeaderCellStyle(sheet.getWorkbook()));
            } else {
                //cost its need
                CellStyle style = header.getHeaderCellStyle(sheet.getWorkbook());
                CellUtils.setCellValue(sheet, lastRow - header.getHeight() + 1, col, header.getDescription(), style);
                CellUtils.setMergeRegion(sheet, lastRow - header.getHeight() + 1, lastRow, col, col, style);
            }
            return new Cost(header.getHeight(), 1);
        }

        int costRow = 0;
        int c = col;
        for (SupportHeader headerChildren : header.getHeaderChildren()) {
            Cost cost = drawHeaderImpl(sheet, row + 1, lastRow, c, col + header.getWidth() - 1, headerChildren);
            c += cost.getColNum();
            costRow = cost.getRowNum();
        }

        if (row >= 0) {
            CellUtils.setMergeRegionValue(sheet, row, lastRow - costRow, col, col + header.getWidth() - 1,
                    header.getDescription(), header.getHeaderCellStyle(sheet.getWorkbook()));
        }

        return new Cost(header.getHeight(), header.getWidth());
    }

    @Data
    @AllArgsConstructor
    private class Cost {
        private int rowNum;
        private int colNum;
    }
}
