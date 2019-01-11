package cn.chenhuanming.octopus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author chenhuanming
 * Created at 2019-01-11
 */
@Data
@AllArgsConstructor
public class Company {
    private String name;
    private Address address;
}
