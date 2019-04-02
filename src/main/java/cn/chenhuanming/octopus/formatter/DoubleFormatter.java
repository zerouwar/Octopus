package cn.chenhuanming.octopus.formatter;

/**
 * @author chenhuanming
 * Created at 2019-01-08
 */
public class DoubleFormatter extends AbstractFormatter<Double> {
    @Override
    public Double parseImpl(String str) throws Exception {
        return Double.valueOf(str);
    }

    @Override
    public String format(Double aDouble) {
        return String.valueOf(aDouble);
    }
}
