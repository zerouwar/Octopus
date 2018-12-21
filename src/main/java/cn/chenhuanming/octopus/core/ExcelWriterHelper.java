package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.ConfigReader;
import cn.chenhuanming.octopus.model.XmlConfigReader;
import lombok.Getter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/20
 */
public final class ExcelWriterHelper {
    public static void write(OutputStream os, List<WriteExcelMaterial> materials) throws IOException {

        ExcelWriter excelWriter = new DefaultExcelWriter(new XSSFWorkbook(), os);

        for (WriteExcelMaterial material : materials) {
            excelWriter.write(material.getSheetName(), new DefaultSheetWriter(material.getConfigReader()), material.getData());
        }
        excelWriter.close();
    }

    public static void writeOneSheet(OutputStream os, WriteExcelMaterial material) throws IOException {
        write(os, Collections.singletonList(material));
    }

    @Getter
    public static class WriteExcelMaterial {
        private ConfigReader configReader;
        private String sheetName;
        private Collection data;

        public WriteExcelMaterial(ConfigReader configReader, String sheetName, Collection data) {
            this.configReader = configReader;
            this.sheetName = sheetName;
            this.data = data;
        }
    }

    public static ConfigReader getDefaultConfigReader(InputStream is) {
        return new XmlConfigReader(is);
    }

}
