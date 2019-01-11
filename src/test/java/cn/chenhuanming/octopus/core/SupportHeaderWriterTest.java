package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.ConfigReader;
import cn.chenhuanming.octopus.model.DefaultCellPosition;
import cn.chenhuanming.octopus.model.XmlConfigReader;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author chenhuanming
 * Created at 2018/12/15
 */
public class SupportHeaderWriterTest {

    @Test
    public void drawHeader() throws IOException {

        Workbook workbook = new XSSFWorkbook();
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/header.xlsx");

        HeaderWriter headerWriter = new DefaultHeaderWriter();

        ConfigReader configReader = new XmlConfigReader(this.getClass().getClassLoader().getResourceAsStream("applicants.xml"));

        headerWriter.drawHeader(workbook.createSheet(), new DefaultCellPosition(0, 0), configReader.getConfig().getFields());

        workbook.write(os);
    }
}