package cn.chenhuanming.octopus.formatter;

import cn.chenhuanming.octopus.model.formatter.AbstractFormatter;

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
