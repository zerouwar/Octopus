package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.OutputModel;

import java.io.InputStream;

/**
 * Created by chenhuanming on 2017-07-01.
 *
 * @author chenhuanming
 */
public interface OutputModelGenerator {
    OutputModel generate(InputStream is);
}
