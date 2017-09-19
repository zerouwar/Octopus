package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.dataConvert.ConvertManager;
import cn.chenhuanming.octopus.dictionary.DefaultValue;
import cn.chenhuanming.octopus.exception.CellCanNotBlankException;
import cn.chenhuanming.octopus.exception.ExcelImportException;
import cn.chenhuanming.octopus.exception.PatternNotMatchException;
import cn.chenhuanming.octopus.exception.UnExpectedException;
import cn.chenhuanming.octopus.model.ConfigurableModelEntity;
import cn.chenhuanming.octopus.model.ImportModelProperty;
import cn.chenhuanming.octopus.model.ModelEntity;
import cn.chenhuanming.octopus.util.CellUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.lang.invoke.MethodHandle;

/**
 * Created by Administrator on 2017-06-08.
 */
public abstract class AbstractRowAssembler<T> implements RowAssembler<T> {
    //目前实体类的Class
    private final ImportModel<T> importModel;
    private final ConvertManager convertManager;
    private final ConvertManager defaultConvertManager;

    @Getter
    @Setter
    private int startIndex;//开始的列索引，默认从0开始


    public AbstractRowAssembler(ConvertManager convertManager,ConvertManager defaultConvertManager, ImportModel<T> importModel) {
        this.importModel = importModel;
        startIndex = 0;
        this.convertManager = convertManager;
        this.defaultConvertManager = defaultConvertManager;
    }

    @Override
    public ModelEntity<T> assemble(Row row, T t) {
        ConfigurableModelEntity<T> modelEntity = modelEntity();
        //处理行号
        if(importModel.getLineNumberHandler()!=null){
            try {
                importModel.getLineNumberHandler().bindTo(t).invoke(row.getRowNum()+1);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        //开始处理@ModelProperty注解的属性
        for (int i = 0; i < importModel.getModelProperties().length; i++) {
            ImportModelProperty handle = importModel.getModelProperties()[i];
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
     * 检查pattern是否跟val匹配
     * @param handle
     * @param val
     * @throws PatternNotMatchException 当pattern和val不匹配时抛出
     */
    private void patternMatches(ImportModelProperty handle, Object val, Cell cell) throws PatternNotMatchException {
        if(handle.getPattern().isPresent()){
            if(!handle.getPattern().get().matcher(val.toString()).matches())
                throw new PatternNotMatchException(handle,cell);
        }
    }


}
