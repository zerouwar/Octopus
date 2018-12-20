package cn.chenhuanming.octopus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author chenhuanming
 * Created at 2018/12/18
 */
@Data
@AllArgsConstructor
public class Address {
    private String city;
    private String detail;
}
