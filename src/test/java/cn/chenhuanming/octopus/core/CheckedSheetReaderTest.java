package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.config.ConfigFactory;
import cn.chenhuanming.octopus.config.XmlConfigFactory;
import cn.chenhuanming.octopus.entity.Applicants;
import cn.chenhuanming.octopus.model.CheckedData;
import cn.chenhuanming.octopus.model.DefaultCellPosition;
import cn.chenhuanming.octopus.reader.CheckedSheetReader;
import cn.chenhuanming.octopus.reader.SheetReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
public class CheckedSheetReaderTest {
    @Test
    public void normal() throws IOException, InvalidFormatException {

        InputStream is = this.getClass().getClassLoader().getResourceAsStream("export.xlsx");
        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);

        ConfigFactory configFactory = new XmlConfigFactory(this.getClass().getClassLoader().getResourceAsStream("applicants.xml"));

        final SheetReader<CheckedData<Applicants>> sheetReader = new CheckedSheetReader<>(sheet, configFactory, new DefaultCellPosition(2, 0));

        for (CheckedData<Applicants> checkedData : sheetReader) {
            System.out.println(checkedData);
        }
    }

    @Test
    public void wrong() throws IOException, InvalidFormatException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("export.xlsx");
        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);

        ConfigFactory configFactory = new XmlConfigFactory(this.getClass().getClassLoader().getResourceAsStream("applicants.xml"));

        final SheetReader<CheckedData<Applicants>> sheetReader = new CheckedSheetReader<>(sheet, configFactory, new DefaultCellPosition(2, 0));

        for (CheckedData<Applicants> checkedData : sheetReader) {
            System.out.println(checkedData);
        }
    }
}