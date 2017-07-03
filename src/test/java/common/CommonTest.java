package common;

import org.junit.Test;

import java.util.stream.IntStream;

/**
 * Created by chenhuanming on 2017-07-01.
 *
 * @author chenhuanming
 */
public class CommonTest {
    @Test
    public void test(){
        String name = "string equals test";
        String hello = "hello world!";
        IntStream.range(0,100000).forEach(i->{
            "hello world".equals(hello);
            "string equals tes".equals(name);
        });
    }
}
