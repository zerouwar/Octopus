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

    @ModelProperty(value = "student's id")
    private String studentId;

    @ModelProperty(value = "student's name",defaultValue = "anonymous")
    private String name;

    @ModelProperty(value = "student's sex",wrongMsg = "sex must be M or F",pattern = "^M|F$")
    private String sex;

    @ModelProperty(value = "student's admission",wrongMsg = "admission must be a date")
    private LocalDate inTime;

    @ModelProperty(value = "student's score",wrongMsg = "score must be numberic",defaultValue = "100")
    private Double score;

}
