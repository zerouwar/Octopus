package cn.chenhuanming.octopus.util;

import java.awt.*;

/**
 * @author chenhuanming
 * Created at 2018/12/13
 */
public class ColorUtils {

    /**
     * @param colorStr e.g. "#FFFFFF"
     * @return
     */
    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    public static Color short2Rgb(short colorShort) {
        return new Color(colorShort);
    }
}
