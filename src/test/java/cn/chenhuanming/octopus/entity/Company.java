package cn.chenhuanming.octopus.entity;

import cn.chenhuanming.octopus.config.annotation.Field;
import cn.chenhuanming.octopus.config.annotation.Header;
import cn.chenhuanming.octopus.config.annotation.Sheet;
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
@Sheet
public class Company {
    @Field(description = "Company Name", color = "#ff0000")
    private String name;
    @Header(description = "Address")
    private Address address;
    @Field(description = "Status")
    private String status;

    public Company(String name, Address address) {
        this.name = name;
        this.address = address;
    }
}
