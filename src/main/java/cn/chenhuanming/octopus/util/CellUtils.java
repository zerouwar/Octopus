package cn.chenhuanming.octopus.util;

import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.DefaultCellPosition;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * @author chenhuanming
 * Created at 2018/12/16
 */
public class CellUtils {

    public static final CellPosition POSITION_ZERO_ZERO = new DefaultCellPosition(0, 0);

    public static void setCellValue(Sheet sheet, int row, int col, String value, CellStyle cellStyle) {
        Row sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            sheetRow = sheet.createRow(row);
        }
        Cell cell = sheetRow.getCell(col);
        if (cell == null) {
            cell = sheetRow.createCell(col);
        }
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

    public static void setMergeRegionValue(Sheet sheet, int row, int lastRow, int col, int lastCol, String value, CellStyle cellStyle) {
        if (row == lastRow && col == lastCol) {
            setCellValue(sheet, row, col, value, cellStyle);
        } else {
            setCellValue(sheet, lastRow, col, value, cellStyle);
            setMergeRegion(sheet, row, lastRow, col, lastCol, cellStyle);
        }
    }

    public static void setMergeRegion(Sheet sheet, int row, int lastRow, int col, int lastCol, CellStyle cellStyle) {

        int i = sheet.addMergedRegion(new CellRangeAddress(row, lastRow, col, lastCol));

        /**
         * seems like a bug
         */
        CellRangeAddress region = sheet.getMergedRegion(sheet instanceof XSSFSheet ? i - 1 : i);

        RegionUtil.setBorderTop(cellStyle.getBorderTopEnum(), region, sheet);
        RegionUtil.setBorderLeft(cellStyle.getBorderLeftEnum(), region, sheet);
        RegionUtil.setBorderBottom(cellStyle.getBorderBottomEnum(), region, sheet);
        RegionUtil.setBorderRight(cellStyle.getBorderRightEnum(), region, sheet);
    }

    public static String getCellValue(Sheet sheet, int row, int col, String defaultValue) {
        Cell cell = sheet.getRow(row).getCell(col);

        if (cell == null) {
            return defaultValue;
        }
        switch (cell.getCellTypeEnum()) {
            case STRING:
                return cell.getStringCellValue();
            case FORMULA:
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return defaultValue;
        }
    }

    public static boolean isDate(Cell cell) {
        try {
            return DateUtil.isCellDateFormatted(cell);
        } catch (Exception e) {
            return false;
        }
    }


}
