package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.annotation.ModelIgnore;
import cn.chenhuanming.octopus.annotation.ModelLineNumber;
import cn.chenhuanming.octopus.annotation.ModelProperty;
import cn.chenhuanming.octopus.dataConvert.ConvertManager;
import cn.chenhuanming.octopus.dataConvert.DefaultValueConvertManager;
import cn.chenhuanming.octopus.dataConvert.SimpleConvertManager;
import cn.chenhuanming.octopus.dictionary.DefaultValue;
import cn.chenhuanming.octopus.exception.CellCanNotBlankException;
import cn.chenhuanming.octopus.exception.ExcelImportException;
import cn.chenhuanming.octopus.exception.PatternNotMatchException;
import cn.chenhuanming.octopus.exception.UnExpectedException;
import cn.chenhuanming.octopus.model.ConfigurableModelEntity;
import cn.chenhuanming.octopus.model.ModelEntity;
import cn.chenhuanming.octopus.model.ModelEntityWithMethodHandleInImport;
import cn.chenhuanming.octopus.util.CellUtil;
import cn.chenhuanming.octopus.util.DataUtil;
import cn.chenhuanming.octopus.util.MethodHandleUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Created by Administrator on 2017-06-08.
 */
public abstract class AbstractRowAssembler<T> implements RowAssembler<T> {
    //目前实体类的Class
    private final Class<T> clazz;
    //保存每个带有@ModelProperty属性的注解信息及其MethodHandle
    private ModelEntityWithMethodHandleInImport[] modelProperties;
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
            ModelEntityWithMethodHandleInImport handle = modelProperties[i];
            MethodHandle methodHandle = handle.getHandle().bindTo(t);
            Cell cell = row.getCell(startIndex + i);
            try {
                if(cell==null){
                    if(handle.isBlankable())
                        methodHandle.invoke(defaultConvertManager.convert(row.createCell(i),methodHandle.type().parameterType(0),handle));
                    else
                        throw new CellCanNotBlankException(CellUtil.positionMsg(cell),handle,cell);
                }else if(cell.getCellTypeEnum()== CellType.BLANK||(cell.getCellTypeEnum()== CellType.STRING&&cell.getStringCellValue().equals(""))){
                    if(handle.isBlankable())
                        methodHandle.invoke(defaultConvertManager.convert(cell,methodHandle.type().parameterType(0),handle));
                    else
                        throw new CellCanNotBlankException(CellUtil.positionMsg(cell),handle,cell);
                }
                else {
                    Object defaultValue = DefaultValue.getDefaultValue(methodHandle.type().parameterType(0));
                    methodHandle.invoke(defaultValue);

                    Object val = convertManager.convert(cell,methodHandle.type().parameterType(0),handle);

                    //检查pattern是否匹配
                    patternMatches(handle,val,cell);

                    methodHandle.invoke(val);
                }
            }catch (ExcelImportException e){
                modelEntity.addException(e);
            } catch (Throwable cause) {
                cause.printStackTrace();
                UnExpectedException exception = new UnExpectedException("it occurs unknow error:"+cause.toString(),cause,handle,row.getRowNum(),startIndex + i);
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
        List<ModelEntityWithMethodHandleInImport> handles = new ArrayList<>();
        for (Field field : fields) {
            //遇到ModelIgnore忽略掉
            if (field.getAnnotation(ModelIgnore.class) != null)
                continue;
            //处理ModelLineNumber
            if (lineNumberHandler==null&&field.getAnnotation(ModelLineNumber.class) != null) {
                if (field.getType() != Integer.class && field.getType() != int.class)
                    throw new IllegalArgumentException("type of property annotated by @ModelLineNumber must be Integer or int");
                lineNumberHandler = MethodHandleUtil.setterMethodHandle(field);
                continue;
            }
            //获取ModelProperty
            handles.add(getModelProperty(field));
        }
        modelProperties = new ModelEntityWithMethodHandleInImport[handles.size()];
        IntStream.range(0, handles.size()).forEach(i -> modelProperties[i] = handles.get(i));
    }

    /**
     * 检查pattern是否跟val匹配
     * @param handle
     * @param val
     * @throws PatternNotMatchException 当pattern和val不匹配时抛出
     */
    private void patternMatches(ModelEntityWithMethodHandleInImport handle, Object val, Cell cell) throws PatternNotMatchException {
        if(handle.getPattern().isPresent()){
            if(!handle.getPattern().get().matcher(val.toString()).matches())
                throw new PatternNotMatchException(handle,cell);
        }
    }

    /**
     * 从类的Filed获取ModelProperty
     * @param field
     * @return
     */
    private ModelEntityWithMethodHandleInImport getModelProperty(Field field) {
        ModelProperty property = field.getAnnotation(ModelProperty.class);
        ModelEntityWithMethodHandleInImport handle = new ModelEntityWithMethodHandleInImport();
        handle.setName(field.getName());

        MethodHandle mh = MethodHandleUtil.setterMethodHandle(field);
        handle.setHandle(mh);
        if (property != null) {
            handle.setDescription(property.value());
            handle.setWrongMsg(property.wrongMsg());
            handle.setPattern(DataUtil.notNullAndNotEmpty(property.pattern(), s -> Optional.of(Pattern.compile(property.pattern())), s -> Optional.empty()));
            handle.setDefaultValue(property.defaultValue());
            handle.setBlankable(property.blankable());
        } else {
            handle.setWrongMsg("");
            handle.setDescription("");
            handle.setPattern(Optional.empty());
            handle.setDefaultValue("");
            handle.setBlankable(true);
        }
        return handle;
    }



}
