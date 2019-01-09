package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.ConfigReader;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author chenhuanming
 * Created at 2018/12/20
 */
public class OctopusTest extends AbstractWriterTest {

    @Test
    public void test() throws IOException {
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/excelWriterHelper.xlsx");

        ConfigReader configReader = Octopus.getXMLConfigReader(this.getClass().getClassLoader().getResourceAsStream("employee.xml"));
        Octopus.writeOneSheet(os, configReader, "test", employeeList);
    }
}