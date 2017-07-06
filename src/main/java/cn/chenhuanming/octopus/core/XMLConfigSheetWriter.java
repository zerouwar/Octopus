package cn.chenhuanming.octopus.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.InputStream;

/**
 * Created by chenhuanming on 2017-07-02.
 *
 * @author chenhuanming
 */
public class XMLConfigSheetWriter<T> extends AbstractSheetWriter<T> {
    private InputStream is;

    public XMLConfigSheetWriter(InputStream is) {
        this.is = is;
        prepare();
    }

    public XMLConfigSheetWriter(InputStream is, int startRow, int startCol) {
        super(startRow, startCol);
        this.is = is;
        prepare();
    }

    @Override
    protected ObjectMapper ObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Override
    protected ExportModelGenerator exportModelGenerator() {
        return new XMLExportModelGenerator(is);
    }
}
