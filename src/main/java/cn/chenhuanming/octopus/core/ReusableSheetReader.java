package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.ModelEntity;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * The RowAssembler will reuse modelEntity and entity,so it the same modelEntity and entity for traversal.
 * @param <T> entity you want
 */
public class ReusableSheetReader<T> extends AbstractSheetReader<ModelEntity<T>> {
    private final RowAssembler<T> assembler;
    private ModelEntity<T> modelEntity;

    public ReusableSheetReader(Sheet sheet, int startRow, int startCol, Class<T> clazz) {
        super(sheet, startRow, startCol);
        this.assembler = new ReusableRowAssembler<>("yyyy-MM-dd",0,clazz);
        try {
            modelEntity = assembler.assemble(sheet.getRow(startRow),clazz.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ModelEntity<T> get(int i) {
        modelEntity = assembler.assemble(sheet.getRow(i),modelEntity.getEntity());
        return modelEntity;
    }

}
