package cn.chenhuanming.octopus.model.formatter;

/**
 * @author chenhuanming
 * Created at 2019-01-08
 */
public class IntegerFormatter extends AbstractFormatter<Integer> {

    @Override
    public String format(Integer integer) {
        return String.valueOf(integer);
    }


    @Override
    Integer parseImpl(String str) throws Exception {
        return Integer.valueOf(str);
    }
}
