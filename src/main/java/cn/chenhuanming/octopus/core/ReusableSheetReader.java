package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.ModelEntity;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * The RowAssembler will reuse modelEntity and entity,so it is the same modelEntity and entity for traversal.
 * It is appropriate that you just need temporary entity to watch sheet instead of getting entity.
 * @param <T> entity you want
 */
public class ReusableSheetReader<T> extends AbstractSheetReader<ModelEntity<T>> {
    private final RowAssembler<T> assembler;
    private ModelEntity<T> modelEntity;

    public ReusableSheetReader(Sheet sheet, RowAssembler<T> rowAssembler,int startRow, int startCol, Class<T> clazz) {
        super(sheet, startRow, startCol);
        this.assembler = rowAssembler;
        try {
            modelEntity = assembler.assemble(sheet.getRow(startRow),clazz.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ReusableSheetReader(Sheet sheet,int startRow, int startCol, Class<T> clazz) {
        this(sheet,new SimpleRowAssembler<>("yyyy-MM-dd",0,clazz),startRow,startCol,clazz);
    }

    @Override
    public ModelEntity<T> get(int i) {
        modelEntity = assembler.assemble(sheet.getRow(i),modelEntity.getEntity());
        return modelEntity;
    }

}
