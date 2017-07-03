package cn.chenhuanming.octopus.model;

import java.util.List;

/**
 * Created by chenhuanming on 2017-07-02.
 *
 * @author chenhuanming
 */
public interface OutputSize {


    int getHeight();

    int getWidth();

    void calculateSize();

    default int maxFieldHeight(List<OutputField> outputFields){
        if(outputFields.isEmpty())
            return 0;
        int max = outputFields.get(0).getHeight();
        for (OutputField field:outputFields) {
            int t = maxFieldHeight(field.getFields());
            field.setHeight(t+1);
            if(t>max)
                max = t;
        }
        return max;
    }

    default int sumFieldWidth(List<OutputField> outputFields){
        if(outputFields.isEmpty())
            return 1;
        int s = 0;
        for (OutputField field:outputFields) {
            if(field.getWidth()==0)
                field.setWidth(field.sumFieldWidth(field.getFields()));
            s += field.getWidth();
        }
        return s;
    }
}
