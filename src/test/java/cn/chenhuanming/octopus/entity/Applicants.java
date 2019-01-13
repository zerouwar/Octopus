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
public class Applicants {
    private int id;
    private String name;
    private Job job;
    private Date entryDate;
    private boolean working = true;
}
