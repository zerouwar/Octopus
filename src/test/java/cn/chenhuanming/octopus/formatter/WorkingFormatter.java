package cn.chenhuanming.octopus.formatter;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
public class WorkingFormatter extends AbstractFormatter<Boolean> {
    @Override
    public Boolean parseImpl(String str) throws Exception {
        return "Working".equals(str);
    }

    @Override
    public String format(Boolean aBoolean) {
        return aBoolean ? "Working" : "Leaved";
    }
}
