package cn.chenhuanming.octopus.util;


import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * @author chenhuanming
 * Created at 2018/12/13
 */
public class ColorUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ColorUtils.class);

    private static final short HSSF_COLOR_INDEX = 0x20;

    /**
     * @param colorStr e.g. "#FFFFFF"
     * @return
     */
    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    public static void setColor(Workbook workbook, Font font, Color color) {
        if (font instanceof XSSFFont) {
            ((XSSFFont) font).setColor(new XSSFColor(color));
        } else if (font instanceof HSSFFont && workbook instanceof HSSFWorkbook) {
            font.setColor(getSimilarColor((HSSFWorkbook) workbook, color).getIndex());
        } else {
            LOGGER.error("unknown font type");
        }
    }

    public static void setForegroundColor(Workbook workbook, CellStyle cellStyle, Color color) {
        if (color == null) {
            return;
        }
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        if (cellStyle instanceof XSSFCellStyle) {
            ((XSSFCellStyle) cellStyle).setFillForegroundColor(new XSSFColor(color));
        } else if (cellStyle instanceof HSSFCellStyle && workbook instanceof HSSFWorkbook) {
            cellStyle.setFillForegroundColor(getSimilarColor((HSSFWorkbook) workbook, color).getIndex());
        } else {
            LOGGER.error("unknown font type");
        }
    }

    public static void setBorderColor(Workbook workbook, CellStyle cellStyle, Color[] color) {
        if (color == null) {
            return;
        }
        if (cellStyle instanceof XSSFCellStyle) {
            ((XSSFCellStyle) cellStyle).setTopBorderColor(new XSSFColor(color[0]));
            ((XSSFCellStyle) cellStyle).setRightBorderColor(new XSSFColor(color[1]));
            ((XSSFCellStyle) cellStyle).setBottomBorderColor(new XSSFColor(color[2]));
            ((XSSFCellStyle) cellStyle).setLeftBorderColor(new XSSFColor(color[3]));
        } else if (cellStyle instanceof HSSFCellStyle && workbook instanceof HSSFWorkbook) {
            cellStyle.setTopBorderColor(getSimilarColor((HSSFWorkbook) workbook, color[0]).getIndex());
            cellStyle.setRightBorderColor(getSimilarColor((HSSFWorkbook) workbook, color[1]).getIndex());
            cellStyle.setBottomBorderColor(getSimilarColor((HSSFWorkbook) workbook, color[2]).getIndex());
            cellStyle.setLeftBorderColor(getSimilarColor((HSSFWorkbook) workbook, color[3]).getIndex());
        } else {
            LOGGER.error("unknown font type");
        }
    }

    private static HSSFColor getSimilarColor(HSSFWorkbook workbook, Color color) {
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor result = palette.findSimilarColor(color.getRed(), color.getGreen(), color.getBlue());
        return result == null ? HSSFColor.HSSFColorPredefined.AUTOMATIC.getColor() : result;
    }

}
