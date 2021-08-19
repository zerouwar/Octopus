package cn.chenhuanming.octopus.test;

import cn.chenhuanming.octopus.Octopus;
import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.XmlConfigFactory;
import cn.chenhuanming.octopus.config.annotation.AnnotationConfigFactory;
import cn.chenhuanming.octopus.entity.Applicants;
import cn.chenhuanming.octopus.exception.SheetNotFoundException;
import cn.chenhuanming.octopus.model.CellPositions;
import cn.chenhuanming.octopus.reader.SheetReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Automated Testing.
 *
 * @author chenhuanming
 * Created at 2018/12/13
 */
public class AutoTest extends AbstractTest {

    @Test
    public void testConfigFactory() {
        Config annotationConfig = new AnnotationConfigFactory(Applicants.class).getConfig();

        Config xmlConfig = new XmlConfigFactory(this.getClass().getClassLoader().getResourceAsStream("applicants.xml")).getConfig();

        Assert.assertEquals(annotationConfig, xmlConfig);

    }

    /**
     * Write data to excel file,then read from it,make sure that data is equal
     */
    @Test
    public void testExportAndImport() throws IOException, InvalidFormatException, SheetNotFoundException {
        final String fileName = "testExportAndImport.xlsx";
        final String sheetName = "sheet1";
        Config config = new AnnotationConfigFactory(Applicants.class).getConfig();

        //Write to file
        FileOutputStream os = new FileOutputStream(this.getClass().getClassLoader().getResource("").getPath() + "/" + fileName);
        Octopus.writeOneSheet(os, config, sheetName, applicantsList);

        //Then read from file
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        SheetReader<Applicants> readApplicants = Octopus.readBySheetName(is, sheetName, config, CellPositions.getContentStartPosition(CellPositions.POSITION_ZERO_ZERO, config));

        //Assert data is equal
        Assert.assertEquals(applicantsList.size(), readApplicants.size());
        for (int i = 0; i < readApplicants.size() - 4; i++) {
            Assert.assertEquals(applicantsList.get(i), readApplicants.get(i));
        }

    }

}