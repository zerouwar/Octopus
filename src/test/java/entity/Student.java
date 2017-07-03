package entity;

import cn.chenhuanming.octopus.annotation.ModelIgnore;
import cn.chenhuanming.octopus.annotation.ModelLineNumber;
import cn.chenhuanming.octopus.annotation.ModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Created by Administrator on 2017-06-08.
 */
@Getter
@Setter
@NoArgsConstructor
public class Student {

    @ModelLineNumber
    private int lineNum;

    @ModelProperty(value = "student's id")
    private String studentId;

    @ModelProperty(value = "student's name",defaultValue = "anonymous")
    private String name;

    @ModelProperty(value = "student's sex",wrongMsg = "sex must be M or F",pattern = "^M|F$")
    private String sex;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ModelProperty(value = "student's admission",wrongMsg = "admission must be a date")
    private LocalDate inTime;

    @ModelProperty(value = "student's score",wrongMsg = "score must be numeric",defaultValue = "100")
    private Double score;

    @ModelIgnore
    private GradeAndClazz gradeAndClazz;

    public Student(String studentId, String name, String sex, LocalDate inTime, Double score,GradeAndClazz gradeAndClazz) {
        this.studentId = studentId;
        this.name = name;
        this.sex = sex;
        this.inTime = inTime;
        this.score = score;
        this.gradeAndClazz = gradeAndClazz;
    }


}
