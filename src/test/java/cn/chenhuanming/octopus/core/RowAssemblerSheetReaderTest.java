package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.ModelEntity;
import entity.Student;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017-06-10.
 */
public class RowAssemblerSheetReaderTest {
    @Test
    public void test() throws IOException, InvalidFormatException {
        InputStream is = getClass().getResourceAsStream("/test.xlsx");
        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);
        SheetReader<ModelEntity<Student>> students = new ReusableSheetReader<>(sheet,1,0,Student.class);

        for (ModelEntity<Student> student:students) {
            System.out.println(student.getEntity().getInTime());
        }
    }

}