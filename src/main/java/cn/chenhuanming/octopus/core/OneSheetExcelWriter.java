package cn.chenhuanming.octopus.core;

import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.util.Collection;

/**
 * Created by chenhuanming on 2017-07-03.
 *
 * @author chenhuanming
 */
public class OneSheetExcelWriter<T> extends AbstractExcelWriter<T> {

    public OneSheetExcelWriter(InputStream xmlInputStream) {
        super(new XMLConfigSheetWriter(xmlInputStream));
    }

    @Override
    public void write(Workbook workbook,Collection<T> collection) {
        if(workbook==null)
            throw new IllegalStateException("workbook can not be null!");
        Sheet sheet = workbook.createSheet();
        sheetWriter.write(sheet,headStyle(workbook),contentStyle(workbook),collection);
    }

    protected CellStyle headStyle(Workbook workbook){
        CellStyle headStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headStyle.setFont(font);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        return headStyle;
    }

    protected CellStyle contentStyle(Workbook workbook){
        return workbook.createCellStyle();
    }

}
