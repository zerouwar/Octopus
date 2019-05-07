package cn.chenhuanming.octopus.config;

import cn.chenhuanming.octopus.util.ColorUtils;
import cn.chenhuanming.octopus.util.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;

import java.awt.*;

/**
 * @author chenhuanming
 * Created at 2018/12/10
 */
public abstract class AbstractConfigFactory implements ConfigFactory {
    protected BorderStyle[] convertBorder(String border) {
        BorderStyle[] result = new BorderStyle[4];
        String[] split = border.split(",");
        for (int i = 0; i < split.length; i++) {
            short val = Short.parseShort(split[i]);
            BorderStyle style = BorderStyle.valueOf(val);
            result[i] = style;
        }
        return result;
    }

    protected Color[] convertBorderColor(String borderColor) {
        Color[] result = new Color[4];
        String[] split = borderColor.split(",");

        for (int i = 0; i < split.length; i++) {
            String c = split[i];
            Color color = ColorUtils.hex2Rgb(StringUtils.isEmpty(c) ? "#000000" : c);
            result[i] = color;
        }
        return result;
    }
}
