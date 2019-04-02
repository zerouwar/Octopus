package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.config.Field;
import cn.chenhuanming.octopus.writer.DefaultHeaderWriter;
import org.junit.Test;

/**
 * @author chenhuanming
 * Created at 2018/12/14
 */
public class SupportFieldTest {
    @Test
    public void test() {
        Field field1 = new Field();
        Field field2 = new Field();
        Field field3 = new Field();
        Field field4 = new Field();
        Field field5 = new Field();

        field2.addChildren(field3).addChildren(field4);
        field1.addChildren(field2).addChildren(field5);

        DefaultHeaderWriter.SupportField header = new DefaultHeaderWriter.SupportField(field1);

        assert header.getHeight() == 3;
        assert header.getWidth() == 3;
    }
}