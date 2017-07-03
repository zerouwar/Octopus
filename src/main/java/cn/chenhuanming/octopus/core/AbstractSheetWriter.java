package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.OutputField;
import cn.chenhuanming.octopus.model.OutputModel;
import cn.chenhuanming.octopus.util.CellUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.InputStream;
import java.util.Collection;

import static cn.chenhuanming.octopus.util.CellUtil.createCells;

/**
 * Created by chenhuanming on 2017-07-01.
 *
 * @author chenhuanming
 */
public abstract class AbstractSheetWriter<T> implements SheetWriter<T> {
    protected int startRow;
    protected int startCol;
    protected ObjectMapper objectMapper;
    protected OutputModel outputModel;
    protected OutputModelGenerator outputModelGenerator;


    private AbstractSheetWriter() {
        startRow = 0;
        startCol = 0;

    }

    public AbstractSheetWriter(InputStream is) {
        this();
        outputModelGenerator = outputModelGenerator();
        this.objectMapper = ObjectMapper();
        if (objectMapper == null)
            throw new IllegalArgumentException("objectMapper is not initialized");
        if (outputModelGenerator == null)
            throw new IllegalArgumentException("outputModelGenerator is not initialized");
        this.outputModel = outputModelGenerator.generate(is);
    }

    public AbstractSheetWriter(InputStream is, int startRow, int startCol) {
        if (startRow < 0 || startCol < 0)
            throw new IllegalArgumentException("startRow and startCol can not be less than 0");
        this.startRow = startRow;
        this.startCol = startCol;
    }

    @Override
    public void write(Sheet sheet, CellStyle headStyle, CellStyle contentStyle, Collection<T> collection) {
        setHead(sheet, headStyle);
        setContent(sheet,contentStyle,collection);
    }

    private void setHead(Sheet sheet, CellStyle headStyle) {
        createCells(startRow,startRow+outputModel.getHeight(),startCol,startCol+outputModel.getWidth(),sheet,headStyle);
        int colIndex = startCol;
        for (int i = 0; i < outputModel.getFields().size(); i++) {
            OutputField field = outputModel.getFields().get(i);
            setHeadField(startRow, colIndex, sheet, field, headStyle);
            colIndex += field.getWidth();
        }
    }

    private void setHeadField(int rowIndex, int colIndex, Sheet sheet, OutputField field, CellStyle headStyle) {
        if (field.getHeight() != 1 || field.getWidth() != 1) {
            int mergeHeight = field.getHeight();
            if(!field.getFields().isEmpty()){
                mergeHeight = field.getHeight() - field.getFields().get(0).getHeight();
            }
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + mergeHeight - 1, colIndex, colIndex + field.getWidth() - 1));
            for (int i = 0; i < field.getFields().size(); i++) {
                OutputField f = field.getFields().get(i);
                setHeadField(rowIndex + mergeHeight, i==0?colIndex:colIndex + field.getFields().get(i-1).getWidth(), sheet, f, headStyle);
            }
        }
        Row head = sheet.getRow(rowIndex);
        Cell cell = head.getCell(colIndex);
        cell.setCellValue(field.getDescription());

    }

    private void setContent(Sheet sheet, CellStyle contentStyle, Collection<?> collection) {
        JsonNode root;
        CellUtil.createCells(startRow+outputModel.getHeight(),startRow+outputModel.getHeight()+collection.size(),startCol,startCol+outputModel.getWidth(),sheet,contentStyle);

        try {
            String string = objectMapper.writeValueAsString(collection);
            root = objectMapper.readTree(string);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        int rowIndex = startRow + outputModel.getHeight();
        for (JsonNode node : root) {
            Row row = sheet.getRow(rowIndex++);
            int colIndex = startCol;
            for (int i = 0; i < outputModel.getFields().size(); i++) {
                OutputField field = outputModel.getFields().get(i);
                colIndex = i==0?colIndex:colIndex + outputModel.getFields().get(i-1).getWidth();
                setContentField(row,node.get(field.getName()),contentStyle,field,colIndex);
            }
        }
    }

    private void setContentField(Row row,JsonNode node,CellStyle contentStyle,OutputField field,int col){
        if(field.getFields().isEmpty()){
            Cell cell = row.getCell(col);
            cell.setCellValue(node.asText());
        }else {
            for (int i = 0; i < field.getFields().size(); i++) {
                OutputField f = field.getFields().get(i);
                setContentField(row,node.get(f.getName()),contentStyle,f,col+i);
            }
        }
    }

    protected abstract ObjectMapper ObjectMapper();

    protected abstract OutputModelGenerator outputModelGenerator();
}
