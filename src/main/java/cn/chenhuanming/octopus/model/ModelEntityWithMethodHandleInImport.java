package cn.chenhuanming.octopus.model;

import lombok.Getter;
import lombok.Setter;

import java.lang.invoke.MethodHandle;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017-06-09.
 */
@Getter
@Setter
public class ModelEntityWithMethodHandleInImport {
    private String name;// name of field
    private String description;// description of field
    private String defaultValue;// default value when cell is blank
    private MethodHandle handle;// method handle
    private String wrongMsg;// wrong message
    private Optional<Pattern> pattern;// regex pattern
    private boolean blankable;// whether cell can be blank
}
