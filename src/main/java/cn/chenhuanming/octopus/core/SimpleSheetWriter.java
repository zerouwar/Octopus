package cn.chenhuanming.octopus.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.InputStream;

/**
 * Created by chenhuanming on 2017-07-02.
 *
 * @author chenhuanming
 */
public class SimpleSheetWriter<T> extends AbstractSheetWriter<T> {
    public SimpleSheetWriter(InputStream is) {
        super(is);
    }

    public SimpleSheetWriter(InputStream is, int startRow, int startCol) {
        super(is, startRow, startCol);
    }

    @Override
    protected ObjectMapper ObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Override
    protected OutputModelGenerator outputModelGenerator() {
        return new SimpleOutputModelGenerator();
    }
}
