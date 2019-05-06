package cn.chenhuanming.octopus.example;

import cn.chenhuanming.octopus.Octopus;
import cn.chenhuanming.octopus.config.ConfigFactory;
import cn.chenhuanming.octopus.config.annotation.AnnotationConfigFactory;
import cn.chenhuanming.octopus.core.AbstractWriterTest;
import cn.chenhuanming.octopus.entity.Applicants;
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

        ConfigFactory configFactory = Octopus
                .getXMLConfigFactory(this.getClass().getClassLoader().getResourceAsStream("applicants.xml"));

        Octopus.writeOneSheet(os, configFactory, "test", applicantsList);
    }

    @Test
    public void exportWithAnnotation() throws IOException {
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/applicator1.xlsx");

        ConfigFactory configFactory = new AnnotationConfigFactory(Applicants.class);

        Octopus.writeOneSheet(os, configFactory, "test", applicantsList);
    }
}
