package cn.chenhuanming.octopus.example;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.XmlConfigFactory;
import cn.chenhuanming.octopus.entity.Address;
import cn.chenhuanming.octopus.entity.Applicants;
import cn.chenhuanming.octopus.entity.Company;
import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.CellPositions;
import cn.chenhuanming.octopus.test.AbstractTest;
import cn.chenhuanming.octopus.writer.AutoSizeSheetWriter;
import cn.chenhuanming.octopus.writer.DefaultExcelWriter;
import cn.chenhuanming.octopus.writer.DefaultSheetWriter;
import cn.chenhuanming.octopus.writer.ExcelWriter;
import cn.chenhuanming.octopus.writer.NoHeaderSheetWriter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Advanced example for customized
 *
 * @author guangdao
 * Created at 2021-08-20
 */
public class AdvancedExample extends AbstractTest {

    Config config = new XmlConfigFactory(this.getClass().getClassLoader().getResourceAsStream("applicants.xml")).getConfig();

    /**
     * Auto size content
     */
    @Test
    public void autoSize() throws IOException {
        try (
                FileOutputStream os = new FileOutputStream(this.getClass().getClassLoader().getResource("").getPath() + "/autoSize.xlsx");
                ExcelWriter excelWriter = new DefaultExcelWriter(os)
        ) {
            excelWriter.write("autoSize", new AutoSizeSheetWriter<Applicants>(new DefaultSheetWriter<Applicants>(config)), applicantsList);
        }

    }

    /**
     * Write several sheets in one workbook
     */
    @Test
    public void multiSheet() throws IOException {
        try (
                FileOutputStream os = new FileOutputStream(this.getClass().getClassLoader().getResource("").getPath() + "/multiSheet.xlsx");
                ExcelWriter excelWriter = new DefaultExcelWriter(os)
        ) {
            excelWriter.write("autoSize", new AutoSizeSheetWriter<Applicants>(new DefaultSheetWriter<Applicants>(config)), applicantsList)
                    .write("default", new DefaultSheetWriter<Applicants>(config), applicantsList)
                    .write("noHeader", new NoHeaderSheetWriter<Applicants>(config), applicantsList);
        }
    }

    /**
     * Append data in one sheet
     */
    @Test
    public void append() throws IOException {
        //Prepare company data
        List<Company> companies = new ArrayList<>();
        DataFactory df = new DataFactory();
        for (int i = 0; i < df.getNumberBetween(5, 10); i++) {
            companies.add(new Company(df.getBusinessName(), new Address(df.getCity(), df.getAddress())));
        }

        try (
                FileOutputStream os = new FileOutputStream(this.getClass().getClassLoader().getResource("").getPath() + "/append.xlsx");
                Workbook workbook = new XSSFWorkbook();
        ) {
            //Create one sheet
            Sheet sheet = workbook.createSheet("append");
            CellPosition startPosition = CellPositions.of(0, 0);

            //Write applicants
            CellPosition endPosition = new DefaultSheetWriter<Applicants>(config).write(sheet, applicantsList);

            //Append company data to next row
            Config companyConfig = new XmlConfigFactory(this.getClass().getClassLoader().getResourceAsStream("company.xml")).getConfig();
            new DefaultSheetWriter<Company>(companyConfig, CellPositions.of(endPosition.getRow() + 1, startPosition.getCol())).write(sheet, companies);

            //Must call write method,otherwise sheet is empty
            workbook.write(os);
        }
    }
}
