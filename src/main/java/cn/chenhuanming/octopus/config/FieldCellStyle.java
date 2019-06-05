package cn.chenhuanming.octopus.config;

import lombok.Builder;
import lombok.Value;
import org.apache.poi.ss.usermodel.BorderStyle;

import java.awt.*;


/**
 * cell style config of field
 * @author guangdao
 * Created at 2019-02-25
 */
@Value
@Builder
public class FieldCellStyle {
    private short fontSize;
    private Color color;
    private boolean bold;
    private Color foregroundColor;
    private BorderStyle[] border;
    private Color[] borderColor;
    private int width;
}
