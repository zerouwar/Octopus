package cn.chenhuanming.octopus.config;

import cn.chenhuanming.octopus.config.annotation.AnnotationConfigFactory;
import cn.chenhuanming.octopus.entity.Applicants;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author guangdao
 * Created at 2019-05-07
 */
public class ConfigFactoryTest {

    @Test
    public void assertSameConfig() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("applicants.xml");
        Config xmlConfig = new XmlConfigFactory(is).getConfig();
        Config javaConfig = new AnnotationConfigFactory(Applicants.class).getConfig();

        Assert.assertEquals(xmlConfig.getClassType(), javaConfig.getClassType());

        Assert.assertEquals(xmlConfig.getFields(), javaConfig.getFields());
    }
}
