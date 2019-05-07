package cn.chenhuanming.octopus.formatter;

import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author : youthlin.chen @ 2019-04-26 17:23
 */
@EqualsAndHashCode
public class BigDecimalFormatter extends AbstractFormatter<BigDecimal> {
    @Override
    public String format(BigDecimal bigDecimal) {
        return String.valueOf(bigDecimal);
    }

    @Override
    public BigDecimal parseImpl(String str) throws Exception {
        return new BigDecimal(str);
    }

}
