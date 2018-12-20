package cn.chenhuanming.octopus.entity;

import lombok.Data;

/**
 * @author chenhuanming
 * Created at 2018/12/18
 */
@Data
public class Employee {
    private ID id;
    private String name;
    private Job job;

    public Employee(int id, String name, Job job) {
        this.id = new ID(id);
        this.name = name;
        this.job = job;
    }
}
