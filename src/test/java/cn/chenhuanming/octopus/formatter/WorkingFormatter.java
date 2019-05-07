package cn.chenhuanming.octopus.formatter;

import lombok.EqualsAndHashCode;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
@EqualsAndHashCode
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
