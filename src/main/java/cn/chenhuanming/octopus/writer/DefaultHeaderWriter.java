package cn.chenhuanming.octopus.writer;

import cn.chenhuanming.octopus.config.Field;
import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.DefaultCellPosition;
import cn.chenhuanming.octopus.model.WorkbookContext;
import cn.chenhuanming.octopus.util.CellUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/15
 */
public class DefaultHeaderWriter implements HeaderWriter {
    @Override
    public CellPosition drawHeader(Sheet sheet, CellPosition startPoint, List<Field> fields) {
        Field fake = new Field(fields);

        SupportField supportField = new SupportField(fake);

        int row = startPoint.getRow() - 1;
        int col = startPoint.getCol();

        int lastRow = row + supportField.getHeight() - 1;
        int lastCol = col + supportField.getWidth() - 1;

        WorkbookContext bookResource = new WorkbookContext(sheet.getWorkbook());

        drawHeaderImpl(sheet, row, lastRow, col, lastCol, supportField, bookResource);

        return new DefaultCellPosition(lastRow, lastCol);
    }

    private Cost drawHeaderImpl(Sheet sheet, int row, int lastRow, int col, int lastCol, SupportField header, WorkbookContext bookResource) {
        Field field = header.getField();
        CellStyle style = bookResource.getHeaderStyle(field);
        if (field.isLeaf()) {
            //set value into the bottom left cell
            if (header.getHeight() == 1) {
                CellUtils.setCellValue(sheet, lastRow, col, field.getDescription(), style);
            } else {
                //cost its need
                CellUtils.setCellValue(sheet, lastRow - header.getHeight() + 1, col, field.getDescription(), style);
                CellUtils.setMergeRegion(sheet, lastRow - header.getHeight() + 1, lastRow, col, col, style);
            }
            return new Cost(header.getHeight(), 1);
        }

        int costRow = 0;
        int c = col;
        for (SupportField headerChildren : header.getHeaderChildren()) {
            Cost cost = drawHeaderImpl(sheet, row + 1, lastRow, c, col + header.getWidth() - 1, headerChildren, bookResource);
            c += cost.getColNum();
            costRow = cost.getRowNum();
        }

        if (row >= 0) {
            CellUtils.setMergeRegionValue(sheet, row, lastRow - costRow, col, col + header.getWidth() - 1,
                    field.getDescription(), style);
        }

        return new Cost(header.getHeight(), header.getWidth());
    }

    @Data
    @AllArgsConstructor
    private class Cost {
        private int rowNum;
        private int colNum;
    }

    /**
     * help to calculate height and width
     *
     * @author chenhuanming
     * Created at 2018/12/14
     */
    @Data
    public static class SupportField {
        private Field field;
        private int height;
        private int width;
        private List<SupportField> headerChildren;

        public SupportField(Field field) {
            this.field = field;
            this.headerChildren = new ArrayList<>(field.getChildren().size());
            if (field.isLeaf()) {
                this.height = 1;
                this.width = 1;
                return;
            }
            int h = 1;
            int w = 0;
            for (Field child : field.getChildren()) {
                SupportField header = new SupportField(child);
                h = Math.max(h, header.getHeight());
                w += header.width;
                headerChildren.add(header);
            }

            //height of all children is the max value of them
            for (SupportField child : headerChildren) {
                child.setHeight(h);
            }
            this.height = h + 1;
            this.width = w;
        }
    }
}
