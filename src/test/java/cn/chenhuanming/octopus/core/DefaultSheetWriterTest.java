package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.XmlConfigReader;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author chenhuanming
 * Created at 2018/12/13
 */
public class DefaultSheetWriterTest extends AbstractWriterTest {

    @Test
    public void test() throws IOException {

        Workbook workbook = new XSSFWorkbook();
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/export.xlsx");

        DefaultSheetWriter writer = new DefaultSheetWriter(new XmlConfigReader(this.getClass().getClassLoader().getResourceAsStream("applicants.xml")));

        writer.write(workbook.createSheet(), applicantsList);

        workbook.write(os);
    }

}