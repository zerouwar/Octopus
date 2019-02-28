package cn.chenhuanming.octopus.example;

import cn.chenhuanming.octopus.Octopus;
import cn.chenhuanming.octopus.config.ConfigFactory;
import cn.chenhuanming.octopus.config.XmlConfigFactory;
import cn.chenhuanming.octopus.entity.Address;
import cn.chenhuanming.octopus.entity.Company;
import cn.chenhuanming.octopus.model.CheckedData;
import cn.chenhuanming.octopus.model.DefaultCellPosition;
import cn.chenhuanming.octopus.reader.SheetReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2019-01-12
 */
public class CompanyExample {
    List<Company> companies;

    /**
     * make testing data
     */
    @Before
    public void prepare() {
        companies = new ArrayList<>();
        DataFactory df = new DataFactory();
        for (int i = 0; i < df.getNumberBetween(5, 10); i++) {
            companies.add(new Company(df.getBusinessName(), new Address(df.getCity(), df.getAddress())));
        }
    }

    @Test
    public void export() throws FileNotFoundException {

        //where to export
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/company.xlsx");

        //read config from company.xml
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("company.xml");
        ConfigFactory configFactory = Octopus.getXMLConfigFactory(is);

        try {
            Octopus.writeOneSheet(os, configFactory, "company", companies);
        } catch (IOException e) {
            System.out.println("export failed");
        }
    }

    @Test
    public void exportAndImport() throws FileNotFoundException {

        //where to export
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/company2.xlsx");

        //read config from company.xml
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("company2.xml");
        ConfigFactory configFactory = Octopus.getXMLConfigFactory(is);

        try {
            Octopus.writeOneSheet(os, configFactory, "company2", companies);
        } catch (IOException e) {
            System.out.println("export failed");
        }

        //Import Excel

        //First get the excel file
        FileInputStream fis = new FileInputStream(rootPath + "/company2.xlsx");

        try {
            SheetReader<Company> importData = Octopus.readFirstSheet(fis, configFactory, new DefaultCellPosition(1, 0));

            for (Company company : importData) {
                System.out.println(company);
            }
        } catch (Exception e) {
            System.out.println("import failed");
        }
    }

    @Test
    public void importCheckedData() throws IOException, InvalidFormatException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("wrongCompany.xlsx");

        ConfigFactory configFactory = new XmlConfigFactory(this.getClass().getClassLoader().getResourceAsStream("company3.xml"));

        final SheetReader<CheckedData<Company>> sheetReader = Octopus.readFirstSheetWithValidation(is, configFactory, new DefaultCellPosition(1, 0));

        for (CheckedData<Company> checkedData : sheetReader) {
            System.out.println(checkedData);
        }
    }

}
