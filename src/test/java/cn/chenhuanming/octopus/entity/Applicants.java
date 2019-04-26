package cn.chenhuanming.octopus.entity;

import cn.chenhuanming.octopus.config.annotation.Field;
import cn.chenhuanming.octopus.config.annotation.Header;
import cn.chenhuanming.octopus.config.annotation.Sheet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author chenhuanming
 * Created at 2018/12/18
 */
@Data
@NoArgsConstructor
@Sheet
public class Applicants {
    @Field(description = "Value", color = "#74f441")
    private int id;
    @Field(description = "Name", fontSize = 20, border = "0,2,0,2", borderColor = ",#4242f4,,#4242f4")
    private String name;
    @Header(description = "Job", headerColor = "#4286f4")
    private Job job;
    @Field(description = "EntryDate", dateFormat = "yyyy-MM-dd")
    private Date entryDate;
    @Field(description = "Working/Leaved", options = "Working|Leaved",
            formatter = cn.chenhuanming.octopus.formatter.WorkingFormatter.class, color = "#42f4b9")
    private boolean working = true;
}
