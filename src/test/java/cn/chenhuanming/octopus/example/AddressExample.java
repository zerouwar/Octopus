package cn.chenhuanming.octopus.example;

import cn.chenhuanming.octopus.Octopus;
import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.XmlConfigFactory;
import cn.chenhuanming.octopus.config.annotation.AnnotationConfigFactory;
import cn.chenhuanming.octopus.entity.Address;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.FileOutputStream;
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
     * preparing testing data
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
    public void export() throws Exception {

        //where to export
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/address.xlsx");

        //get config from xml file.Singleton pattern is recommending
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("address.xml");
        Config config = new XmlConfigFactory(is).getConfig();

        //just one line of code with Octopus facade
        Octopus.writeOneSheet(os, config, "address", addresses);
    }

    @Test
    public void exportWithAnnotation() throws Exception {
        //where to export
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/address1.xlsx");

        Config config = new AnnotationConfigFactory(Address.class).getConfig();

        Octopus.writeOneSheet(os, config, "address", addresses);
    }

}
