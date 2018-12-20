package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.entity.Address;
import cn.chenhuanming.octopus.entity.Employee;
import cn.chenhuanming.octopus.entity.Job;
import cn.chenhuanming.octopus.model.XmlConfigReader;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/13
 */
public class DefaultSheetWriterTest {

    List<Employee> employeeList;

    @Before
    public void before() {
        employeeList = new ArrayList<>();
        DataFactory df = new DataFactory();
        for (int i = 0; i < 6; i++) {
            employeeList.add(new Employee(df.getNumberBetween(0, 100), df.getName(), new Job(new Address(df.getCity(), df.getAddress()), df.getBusinessName())));
        }
    }

    @Test
    public void test() throws IOException {

        Workbook workbook = new XSSFWorkbook();
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/export.xlsx");

        DefaultSheetWriter writer = new DefaultSheetWriter(new XmlConfigReader(this.getClass().getClassLoader().getResourceAsStream("employee.xml")));

        writer.write(workbook.createSheet(), employeeList);

        workbook.write(os);
    }

}