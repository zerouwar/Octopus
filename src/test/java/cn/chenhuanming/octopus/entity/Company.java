package cn.chenhuanming.octopus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenhuanming
 * Created at 2019-01-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private String name;
    private Address address;
    private String status;

    public Company(String name, Address address) {
        this.name = name;
        this.address = address;
    }
}
