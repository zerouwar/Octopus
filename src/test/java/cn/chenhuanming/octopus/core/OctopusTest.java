package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.Octopus;
import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.XmlConfigFactory;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author chenhuanming
 * Created at 2018/12/20
 */
public class OctopusTest extends AbstractWriterTest {
    @Override
    protected int number() {
        return 100000;
    }

    @Test
    public void export() throws IOException {
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/octopusExport.xlsx");

        Config config = new XmlConfigFactory(this.getClass().getClassLoader().getResourceAsStream("applicants.xml")).getConfig();

        Octopus.writeOneSheet(os, config, "test", applicantsList);
    }
}