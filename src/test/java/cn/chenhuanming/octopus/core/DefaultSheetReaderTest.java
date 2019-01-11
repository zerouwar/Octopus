package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.entity.Applicants;
import cn.chenhuanming.octopus.model.ConfigReader;
import cn.chenhuanming.octopus.model.DefaultCellPosition;
import cn.chenhuanming.octopus.model.XmlConfigReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author chenhuanming
 * Created at 2019-01-07
 */
public class DefaultSheetReaderTest {

    private Sheet sheet;

    @Before
    public void prepare() throws IOException, InvalidFormatException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("export.xlsx");
        Workbook workbook = WorkbookFactory.create(is);
        this.sheet = workbook.getSheetAt(0);
    }

    @Test
    public void test() {
        ConfigReader configReader = new XmlConfigReader(this.getClass().getClassLoader().getResourceAsStream("applicants.xml"));

        final SheetReader<Applicants> sheetReader = new DefaultSheetReader<>(sheet, configReader, new DefaultCellPosition(2, 0));

        for (Applicants applicants : sheetReader) {
            System.out.println(applicants);
        }

    }

}