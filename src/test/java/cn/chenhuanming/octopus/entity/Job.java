package cn.chenhuanming.octopus.entity;

import cn.chenhuanming.octopus.config.annotation.Field;
import cn.chenhuanming.octopus.config.annotation.Header;
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
public class Job {
    @Header(description = "Company Name")
    private Company company;
    @Field(description = "JD")
    private String JobDescription;
    @Field(description = "Rank", color = "#f44271", foregroundColor = "#eaeae5")
    private String rank;
}
