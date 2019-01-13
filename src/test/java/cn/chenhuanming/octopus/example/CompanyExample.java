package cn.chenhuanming.octopus.example;

import cn.chenhuanming.octopus.core.Octopus;
import cn.chenhuanming.octopus.core.SheetReader;
import cn.chenhuanming.octopus.entity.Address;
import cn.chenhuanming.octopus.entity.Company;
import cn.chenhuanming.octopus.model.CheckedData;
import cn.chenhuanming.octopus.model.ConfigReader;
import cn.chenhuanming.octopus.model.DefaultCellPosition;
import cn.chenhuanming.octopus.model.XmlConfigReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
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
        ConfigReader configReader = Octopus.getXMLConfigReader(is);

        try {
            Octopus.writeOneSheet(os, configReader, "company", companies);
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
        ConfigReader configReader = Octopus.getXMLConfigReader(is);

        try {
            Octopus.writeOneSheet(os, configReader, "company2", companies);
        } catch (IOException e) {
            System.out.println("export failed");
        }

        //Import Excel

        //First get the excel file
        FileInputStream fis = new FileInputStream(rootPath + "/company2.xlsx");

        try {
            SheetReader<Company> importData = Octopus.readFirstSheet(fis, configReader, new DefaultCellPosition(1, 0));

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

        ConfigReader configReader = new XmlConfigReader(this.getClass().getClassLoader().getResourceAsStream("company3.xml"));

        final SheetReader<CheckedData<Company>> sheetReader = Octopus.readFirstSheetWithValidation(is, configReader, new DefaultCellPosition(1, 0));

        for (CheckedData<Company> checkedData : sheetReader) {
            System.out.println(checkedData);
        }
    }

}
