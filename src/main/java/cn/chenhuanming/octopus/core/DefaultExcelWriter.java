package cn.chenhuanming.octopus.core;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

/**
 * @author chenhuanming
 * Created at 2018/12/19
 */
public class DefaultExcelWriter implements ExcelWriter {
    private final Workbook workbook;
    private final OutputStream os;

    public DefaultExcelWriter(OutputStream os) {
        this(new XSSFWorkbook(), os);
    }

    public DefaultExcelWriter(Workbook workbook, OutputStream os) {
        this.workbook = workbook;
        this.os = os;
    }

    @Override
    public <T> ExcelWriter write(String sheetName, SheetWriter<T> sheetWriter, Collection<T> collection) {
        Sheet sheet = workbook.createSheet(sheetName);
        sheetWriter.write(sheet, collection);
        return this;
    }

    @Override
    public void close() throws IOException {
        workbook.write(os);
    }
}
