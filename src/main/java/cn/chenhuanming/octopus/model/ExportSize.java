package cn.chenhuanming.octopus.model;

import java.util.List;

/**
 * Created by chenhuanming on 2017-07-02.
 *
 * @author chenhuanming
 */
public interface ExportSize {


    int getHeight();

    int getWidth();

    void calculateSize();

    default int maxFieldHeight(List<ExportField> exportFields){
        if(exportFields.isEmpty())
            return 0;
        int max = exportFields.get(0).getHeight();
        for (ExportField field:exportFields) {
            int t = maxFieldHeight(field.getFields());
            field.setHeight(t+1);
            if(t>max)
                max = t;
        }
        return max;
    }

    default int sumFieldWidth(List<ExportField> exportFields){
        if(exportFields.isEmpty())
            return 1;
        int s = 0;
        for (ExportField field:exportFields) {
            if(field.getWidth()==0)
                field.setWidth(field.sumFieldWidth(field.getFields()));
            s += field.getWidth();
        }
        return s;
    }
}
