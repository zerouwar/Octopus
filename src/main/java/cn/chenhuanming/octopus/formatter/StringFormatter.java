package cn.chenhuanming.octopus.formatter;

/**
 * @author chenhuanming
 * Created at 2019-01-08
 */
public class StringFormatter extends AbstractFormatter<String> {
    @Override
    public String format(String aShort) {
        return String.valueOf(aShort);
    }


    @Override
    public String parseImpl(String str) throws Exception {
        return str;
    }
}
