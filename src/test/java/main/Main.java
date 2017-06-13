package main;

import cn.chenhuanming.octopus.core.ReusableRowAssembler;
import cn.chenhuanming.octopus.core.RowAssembler;
import cn.chenhuanming.octopus.model.ModelEntity;
import entity.Student;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-06-08.
 */
public class Main {

    @Test
    public void test(){
        RowAssembler<Student> rowAssembler = new ReusableRowAssembler<>("yyyy-MM-dd HH:mm:ss",0,Student.class);
        try {
            InputStream is = getClass().getResourceAsStream("/test.xlsx");
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            List<ModelEntity<Student>> students = new ArrayList<>();
            for (int i = sheet.getFirstRowNum()+1; i <=sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Student student = new Student();
                ModelEntity<Student> modelEntity= rowAssembler.assemble(row,student);
                students.add(modelEntity);
            }
            System.out.println(students);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}
