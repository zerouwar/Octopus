package cn.chenhuanming.octopus.formatter;

/**
 * @author chenhuanming
 * Created at 2019-01-08
 */
public class ShortFormatter extends AbstractFormatter<Short> {
    @Override
    public String format(Short aShort) {
        return String.valueOf(aShort);
    }


    @Override
    public Short parseImpl(String str) throws Exception {
        return Short.valueOf(str);
    }
}
