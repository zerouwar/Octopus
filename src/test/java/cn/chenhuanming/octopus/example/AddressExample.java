package cn.chenhuanming.octopus.example;

import cn.chenhuanming.octopus.core.Octopus;
import cn.chenhuanming.octopus.entity.Address;
import cn.chenhuanming.octopus.model.ConfigReader;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2019-01-11
 */
public class AddressExample {
    List<Address> addresses;

    /**
     * make testing data
     */
    @Before
    public void prepare() {
        addresses = new ArrayList<>();
        DataFactory df = new DataFactory();
        for (int i = 0; i < df.getNumberBetween(5, 10); i++) {
            addresses.add(new Address(df.getCity(), df.getAddress()));
        }
    }

    @Test
    public void export() throws FileNotFoundException {

        //where to export
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/address.xlsx");

        //read config from address.xml
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("address.xml");
        ConfigReader configReader = Octopus.getXMLConfigReader(is);

        try {
            Octopus.writeOneSheet(os, configReader, "address", addresses);
        } catch (IOException e) {
            System.out.println("export failed");
        }
    }
}
