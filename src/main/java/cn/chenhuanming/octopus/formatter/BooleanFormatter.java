package cn.chenhuanming.octopus.formatter;

/**
 * @author chenhuanming
 * Created at 2019-01-08
 */
public class BooleanFormatter extends AbstractFormatter<Boolean> {
    @Override
    public Boolean parseImpl(String str) throws Exception {
        return Boolean.valueOf(str);
    }

    @Override
    public String format(Boolean aBoolean) {
        return String.valueOf(aBoolean);
    }
}
