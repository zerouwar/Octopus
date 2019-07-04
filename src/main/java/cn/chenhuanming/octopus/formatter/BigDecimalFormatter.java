package cn.chenhuanming.octopus.formatter;

import java.math.BigDecimal;

/**
 * @author huiyadanli
 * Created at 2019-06-22
 */
public class BigDecimalFormatter extends AbstractFormatter<BigDecimal> {
    @Override
    public BigDecimal parseImpl(String str) throws Exception {
        return new BigDecimal(str);
    }

    @Override
    public String format(BigDecimal bigDecimal) {
        return bigDecimal.toString();
    }
}
