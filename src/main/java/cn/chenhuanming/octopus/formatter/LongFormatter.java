package cn.chenhuanming.octopus.formatter;

/**
 * @author chenhuanming
 * Created at 2019-01-08
 */
public class LongFormatter extends AbstractFormatter<Long> {
    @Override
    public Long parseImpl(String str) throws Exception {
        return Long.valueOf(str);
    }

    @Override
    public String format(Long aLong) {
        return String.valueOf(aLong);
    }
}
