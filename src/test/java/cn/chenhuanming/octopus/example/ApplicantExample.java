package cn.chenhuanming.octopus.example;

import cn.chenhuanming.octopus.Octopus;
import cn.chenhuanming.octopus.config.ConfigFactory;
import cn.chenhuanming.octopus.core.AbstractWriterTest;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author chenhuanming
 * Created at 2019-01-12
 */
public class ApplicantExample extends AbstractWriterTest {
    @Test
    public void export() throws IOException {
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/applicator.xlsx");

        ConfigFactory configFactory = Octopus.getXMLConfigReader(this.getClass().getClassLoader().getResourceAsStream("applicants.xml"));

        Octopus.writeOneSheet(os, configFactory, "test", applicantsList);
    }
}
