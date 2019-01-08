package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.entity.Employee;
import cn.chenhuanming.octopus.model.ConfigReader;
import cn.chenhuanming.octopus.model.DefaultCellPosition;
import cn.chenhuanming.octopus.model.XmlConfigReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author chenhuanming
 * Created at 2019-01-07
 */
public class DefaultSheetReaderTest {
    @Test
    public void test() throws IOException, InvalidFormatException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("export.xlsx");

        ConfigReader configReader = new XmlConfigReader(this.getClass().getClassLoader().getResourceAsStream("employee.xml"));

        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);

        SheetReader<Employee> sheetReader = new DefaultSheetReader<>(sheet, configReader, new DefaultCellPosition(2, 0));

        Employee employee = sheetReader.get(0);

        System.out.println(employee);
    }

}