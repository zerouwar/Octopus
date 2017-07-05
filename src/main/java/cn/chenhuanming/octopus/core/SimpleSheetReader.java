package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.ModelEntity;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Constructor;

/**
 * Created by chenhuanming on 2017-06-14.
 *
 * @author chenhuanming
 */
public class SimpleSheetReader<T> extends AbstractSheetReader<ModelEntity<T>> {

    private final RowAssembler<T> assembler;
    private Class<T> clazz;
    private Constructor<T> constructor;

    public SimpleSheetReader(Sheet sheet, int startRow, int startCol, Class<T> clazz) {
        this(sheet, new SimpleRowAssembler<>("yyyy-MM-dd",0,clazz),startRow, startCol,clazz);
    }

    public SimpleSheetReader(Sheet sheet, RowAssembler<T> rowAssembler,int startRow, int startCol, Class<T> clazz) {
        super(sheet, startRow, startCol);
        this.assembler = rowAssembler;
        this.clazz = clazz;
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if(constructor.getParameterCount()==0){
                this.constructor = (Constructor<T>) constructor;
                return;
            }
        }
        throw new IllegalArgumentException(clazz+" must has a non parameter constructor");
    }

    @Override
    public ModelEntity<T> get(int i) {
        try {
            return assembler.assemble(sheet.getRow(i),constructor.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException(clazz+" must has a non parameter constructor");
    }
}
