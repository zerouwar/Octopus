package cn.chenhuanming.octopus.model;

import org.junit.Test;

/**
 * @author chenhuanming
 * Created at 2018/12/14
 */
public class SupportHeaderTest {
    @Test
    public void test() {
        DefaultField field1 = new DefaultField();
        DefaultField field2 = new DefaultField();
        DefaultField field3 = new DefaultField();
        DefaultField field4 = new DefaultField();
        DefaultField field5 = new DefaultField();

        field2.addChildren(field3).addChildren(field4);
        field1.addChildren(field2).addChildren(field5);

        SupportHeader header = new SupportHeader(field1);

        assert header.getHeight() == 3;
        assert header.getWidth() == 3;
    }
}