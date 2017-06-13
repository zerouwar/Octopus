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
public class ModelEntityWithMethodHandle {
    private String name;//属性名
    private String defaultValue;//默认值
    private MethodHandle handle;//方法句柄
    private String description;//属性描述
    private String wrongMsg;//错误信息
    private Optional<Pattern> pattern;//正则表达式检查

}
