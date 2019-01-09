package cn.chenhuanming.octopus.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author chenhuanming
 * Created at 2018/12/18
 */
@Data
@NoArgsConstructor
public class Employee {
    private ID id;
    private String name;
    private Job job;
    private Date entryDate;
    private boolean working = true;

}
