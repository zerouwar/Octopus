package cn.chenhuanming.octopus.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

/**
 * Created by chenhuanming on 2017-07-01.
 *
 * @author chenhuanming
 */
@Getter
@Setter
public class OutputField implements OutputSize {
    private final String name;
    private final String description;
    private int height;
    private int width;
    private List<OutputField> fields;

    public OutputField(String name, String description) {
        this.name = name;
        this.description = description;
        width = 0;
        height = 1;
        fields = Collections.emptyList();
    }

    @Override
    public void calculateSize() {
        System.out.println("---------outputField is calculating size.....");
        int maxHeight = maxFieldHeight(fields);
        this.height = maxHeight + 1;
        this.width = sumFieldWidth(fields);
    }


}
