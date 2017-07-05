package cn.chenhuanming.octopus.core;

import entity.GradeAndClazz;
import entity.Student;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * Created by chenhuanming on 2017-07-03.
 *
 * @author chenhuanming
 */
public class OneSheetExcelWriterTest {
    @Test
    public void write() throws Exception {
        GradeAndClazz gradeAndClazz = new GradeAndClazz("2014","R6");
        Student student1 = new Student("201223","John","M", LocalDate.now(),98.00,gradeAndClazz);
        Student student2 = new Student("204354","Tony","M", LocalDate.now(),87.00,gradeAndClazz);
        Student student3 = new Student("202432","Joyce","F", LocalDate.now(),90.00,gradeAndClazz);

        ExcelWriter<Student> studentExcelWriter = new OneSheetExcelWriter<>(getClass().getClassLoader().getResourceAsStream("studentOutput.xml"));
        studentExcelWriter.write(Arrays.asList(student1,student2,student3));
    }

}