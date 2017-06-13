package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.annotation.ModelIgnore;
import cn.chenhuanming.octopus.annotation.ModelLineNumber;
import cn.chenhuanming.octopus.annotation.ModelProperty;
import cn.chenhuanming.octopus.dataConvert.ConvertManager;
import cn.chenhuanming.octopus.dataConvert.DefaultValueConvertManager;
import cn.chenhuanming.octopus.dataConvert.SimpleConvertManager;
import cn.chenhuanming.octopus.exception.ExcelImportException;
import cn.chenhuanming.octopus.exception.PatternNotMatchException;
import cn.chenhuanming.octopus.exception.UnExpectedException;
import cn.chenhuanming.octopus.model.ConfigurableModelEntity;
import cn.chenhuanming.octopus.model.ModelEntity;
import cn.chenhuanming.octopus.model.ModelEntityWithMethodHandle;
import cn.chenhuanming.octopus.util.DataUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * Created by Administrator on 2017-06-08.
 */
public abstract class AbstractRowAssembler<T> implements RowAssembler<T> {
    //目前实体类的Class
    private final Class<T> clazz;
    //保存每个带有@ModelProperty属性的注解信息及其MethodHandle
    private ModelEntityWithMethodHandle[] modelProperties;
    //行号MethodHandle
    private MethodHandle lineNumberHandler;

    private final ConvertManager convertManager;
    private final ConvertManager defaultConvertManager;

    @Getter
    @Setter
    private int startIndex;//开始的列索引，默认从0开始


    public AbstractRowAssembler(String date2StringFormat, int mantissaNumber, Class<T> clazz) {
        this.clazz = clazz;
        startIndex = 0;
        convertManager = new SimpleConvertManager(date2StringFormat, mantissaNumber);
        defaultConvertManager = new DefaultValueConvertManager();
        //读取类信息和注解信息
        initModelInfo();

    }

    @Override
    public ModelEntity<T> assemble(Row row, T t) {
        ConfigurableModelEntity<T> modelEntity = modelEntity();
        //处理行号
        if(lineNumberHandler!=null){
            try {
                lineNumberHandler.bindTo(t).invoke(row.getRowNum()+1);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        //开始处理@ModelProperty注解的属性
        for (int i = 0; i < modelProperties.length; i++) {
            ModelEntityWithMethodHandle handle = modelProperties[i];
            MethodHandle methodHandle = handle.getHandle().bindTo(t);
            Cell cell = row.getCell(startIndex + i);
            try {
                if(cell==null){
                    methodHandle.invoke(defaultConvertManager.convert(row.createCell(i),methodHandle.type().parameterType(0),handle));
                }else if(cell.getCellTypeEnum()== CellType.BLANK||(cell.getCellTypeEnum()== CellType.STRING&&cell.getStringCellValue().equals(""))){
                    methodHandle.invoke(defaultConvertManager.convert(cell,methodHandle.type().parameterType(0),handle));
                }
                else {
                    methodHandle.invoke(null);

                    Object val = convertManager.convert(cell,methodHandle.type().parameterType(0),handle);

                    //检查pattern是否匹配
                    patternMatches(handle,val);

                    methodHandle.invoke(val);
                }
            }catch (ExcelImportException e){
                modelEntity.addException(e);
            } catch (Throwable cause) {
                cause.printStackTrace();
                UnExpectedException exception = new UnExpectedException("it occurs unknow error:"+cause.toString(),cause,handle);
                modelEntity.addException(exception);
            }
        }
        modelEntity.setEntity(t);
        return modelEntity;
    }

    @Override
    public boolean isSupport(Row row) {
        return true;
    }

    /**
     * 每次装配所用的modelEntity，可以通过实现该方法重用ModelEntity
     * @return
     */
    protected abstract ConfigurableModelEntity<T> modelEntity();

    /**
     * 初始化，读取类信息和注解信息
     */
    private void initModelInfo() {
        Field[] fields = this.clazz.getDeclaredFields();
        List<ModelEntityWithMethodHandle> handles = new ArrayList<>();
        for (Field field : fields) {
            //遇到ModelIgnore忽略掉
            if (field.getAnnotation(ModelIgnore.class) != null)
                continue;
            //处理ModelLineNumber
            if (lineNumberHandler==null&&field.getAnnotation(ModelLineNumber.class) != null) {
                if (field.getType() != Integer.class && field.getType() != int.class)
                    throw new IllegalArgumentException("type of property annotated by @ModelLineNumber must be Integer or int");
                lineNumberHandler = getMethodHandle(field);
                continue;
            }
            //获取ModelProperty
            handles.add(getModelProperty(field));
        }
        modelProperties = new ModelEntityWithMethodHandle[handles.size()];
        IntStream.range(0, handles.size()).forEach(i -> modelProperties[i] = handles.get(i));
    }

    /**
     * 检查pattern是否跟val匹配
     * @param handle
     * @param val
     * @throws PatternNotMatchException 当pattern和val不匹配时抛出
     */
    private void patternMatches(ModelEntityWithMethodHandle handle,Object val) throws PatternNotMatchException {
        if(handle.getPattern().isPresent()){
            if(!handle.getPattern().get().matcher(val.toString()).matches())
                throw new PatternNotMatchException(handle);
        }
    }

    /**
     * 从类的Filed获取ModelProperty
     * @param field
     * @return
     */
    private ModelEntityWithMethodHandle getModelProperty(Field field) {
        ModelProperty property = field.getAnnotation(ModelProperty.class);
        ModelEntityWithMethodHandle handle = new ModelEntityWithMethodHandle();
        handle.setName(field.getName());

        MethodHandle mh = getMethodHandle(field);
        handle.setHandle(mh);
        if (property != null) {
            handle.setDescription(property.value());
            handle.setWrongMsg(property.wrongMsg());
            handle.setPattern(DataUtils.notNullAndNotEmpty(property.pattern(),s -> Optional.of(Pattern.compile(property.pattern())),s -> Optional.empty()));
            handle.setDefaultValue(property.defaultValue());
        } else {
            handle.setWrongMsg("");
            handle.setDescription("");
            handle.setPattern(Optional.empty());
            handle.setDefaultValue("");
        }
        return handle;
    }

    /**
     * 获取方法句柄
     *
     * @param field Model的property
     * @return 方法句柄
     */
    private MethodHandle getMethodHandle(Field field) {
        StringBuilder setter = new StringBuilder("set");
        String name = field.getName();
        setter.append(Character.toUpperCase(name.charAt(0))).append(name.substring(1));

        MethodType mt = MethodType.methodType(void.class, field.getType());
        try {
            return lookup().findVirtual(this.clazz, setter.toString(), mt);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("property " + name + " must has setter method");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("property " + name + " must has setter method");
    }

}
