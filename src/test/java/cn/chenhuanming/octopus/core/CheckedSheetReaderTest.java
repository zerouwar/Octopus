package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.XmlConfigFactory;
import cn.chenhuanming.octopus.config.annotation.AnnotationConfigFactory;
import cn.chenhuanming.octopus.entity.Applicants;
import cn.chenhuanming.octopus.model.CheckedData;
import cn.chenhuanming.octopus.model.DefaultCellPosition;
import cn.chenhuanming.octopus.reader.CheckedSheetReader;
import cn.chenhuanming.octopus.reader.SheetReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
public class CheckedSheetReaderTest {
    private Config config = new XmlConfigFactory(
            this.getClass().getClassLoader().getResourceAsStream("applicants.xml")).getConfig();

    private void switchToAnnotation() {
        config = new AnnotationConfigFactory(Applicants.class).getConfig();
    }

    @Test
    public void testNormal() throws IOException, InvalidFormatException {
        normal();
        switchToAnnotation();
        System.out.println("----------------");
        System.out.println(config.getFormatterContainer().get(BigDecimal.class));
        normal();
    }

    private void normal() throws IOException, InvalidFormatException {

        InputStream is = this.getClass().getClassLoader().getResourceAsStream("import.xlsx");
        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);

        final SheetReader<CheckedData<Applicants>> sheetReader = new CheckedSheetReader<>(sheet, config,
                new DefaultCellPosition(4, 0));

        for (CheckedData<Applicants> checkedData : sheetReader) {
            System.out.println(checkedData);
        }
    }

}
