package cn.chenhuanming.octopus.core;

import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by chenhuanming on 2017-07-03.
 *
 * @author chenhuanming
 */
public abstract class AbstractExcelWriter<T> implements ExcelWriter<T> {
    protected Workbook workbook;
    protected SheetWriter<T> sheetWriter;
    protected CellStyle headStyle;
    protected CellStyle contentStyle;

    public AbstractExcelWriter() {
    }

    @Override
    public void writeToStream(OutputStream os) throws IOException {
        workbook.write(os);
    }

    public void setWorkbook(Workbook workbook){
        this.workbook = workbook;
        headStyle = headStyle();
        contentStyle = contentStyle();
    }

    public void setSheetWriter(SheetWriter<T> sheetWriter){
        this.sheetWriter = sheetWriter;
    }

    protected CellStyle headStyle(){
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

    protected CellStyle contentStyle(){
        return workbook.createCellStyle();
    }

}
