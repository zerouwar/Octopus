package cn.chenhuanming.octopus.entity;

import cn.chenhuanming.octopus.config.annotation.Field;
import cn.chenhuanming.octopus.config.annotation.Sheet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenhuanming
 * Created at 2018/12/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Sheet
public class Address {
    @Field(description = " City")
    private String city;
    @Field(description = "Detail")
    private String detail;
}
