package entity;

import cn.chenhuanming.octopus.annotation.ModelLineNumber;
import cn.chenhuanming.octopus.annotation.ModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Created by Administrator on 2017-06-08.
 */
@Getter
@Setter
public class Student {

    @ModelLineNumber
    private int lineNum;

    @ModelProperty(value = "学号",wrongMsg = "学号有错?")
    private String studentId;

    @ModelProperty(value = "名字",defaultValue = "anonymous")
    private String name;

    @ModelProperty(value = "性别",wrongMsg = "学号有错?")
    private String sex;

    @ModelProperty(value = "入学时间",wrongMsg = "入学时间必须为时间格式")
    private LocalDate inTime;

    @ModelProperty(value = "成绩",wrongMsg = "成绩必须为数字",defaultValue = "100")
    private Double score;

}
