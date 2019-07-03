package cn.chenhuanming.octopus.formatter;

/**
 * @author chenhuanming
 * Created at 2019-01-08
 */
public class FloatFormatter extends AbstractFormatter<Float> {
    @Override
    public Float parseImpl(String str) throws Exception {
        return Float.valueOf(str);
    }

    @Override
    public String format(Float aFloat) {
        return String.valueOf(aFloat);
    }

    static class PrimitiveFormatter extends FloatFormatter {
        @Override
        protected Float defaultValueWhenParseEmpty() {
            return 0.0f;
        }
    }
}
