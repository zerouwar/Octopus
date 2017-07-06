package cn.chenhuanming.octopus.core;

import entity.GradeAndClazz;
import entity.Student;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Created by chenhuanming on 2017-07-01.
 *
 * @author chenhuanming
 */
public class SimpleSheetWriterTest {
    @Test
    public void testSheetWriter() throws Exception {
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        rootPath = rootPath.substring(1);
        SheetWriter sheetWriter = new XMLConfigSheetWriter(getClass().getClassLoader().getResourceAsStream("studentExport.xml"));

        XSSFWorkbook workbook = new XSSFWorkbook();
        CellStyle headStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headStyle.setFont(font);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        CellStyle contentStyle = workbook.createCellStyle();

        GradeAndClazz gradeAndClazz = new GradeAndClazz("2014","R6");
        Student student1 = new Student("201223","John","M", LocalDate.now(),98.00,gradeAndClazz);
        Student student2 = new Student("204354","Tony","M", LocalDate.now(),87.00,gradeAndClazz);
        Student student3 = new Student("202432","Joyce","F", LocalDate.now(),90.00,gradeAndClazz);

        sheetWriter.write(workbook.createSheet(),headStyle,contentStyle, Arrays.asList(student1,student2,student3));

        OutputStream os = new FileOutputStream(rootPath+"exportStudent.xlsx");
        workbook.write(os);
    }
}