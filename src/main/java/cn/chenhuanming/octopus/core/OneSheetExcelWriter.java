package cn.chenhuanming.octopus.core;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.Collection;

/**
 * Created by chenhuanming on 2017-07-03.
 *
 * @author chenhuanming
 */
public class OneSheetExcelWriter<T> extends AbstractExcelWriter<T> {

    public OneSheetExcelWriter(InputStream xmlInputStream) {
        setWorkbook(new XSSFWorkbook());
        setSheetWriter(new XMLConfigSheetWriter(xmlInputStream));
        headStyle = headStyle();
        contentStyle = contentStyle();
    }

    @Override
    public void write(Collection<T> collection) {
        setWorkbook(new XSSFWorkbook());
        if(workbook==null)
            throw new IllegalStateException("workbook can not be null!");
        if(sheetWriter==null)
            throw new IllegalStateException("sheetWriter can not be null!");
        Sheet sheet = workbook.createSheet();
        sheetWriter.write(sheet,headStyle,contentStyle,collection);
    }

}
