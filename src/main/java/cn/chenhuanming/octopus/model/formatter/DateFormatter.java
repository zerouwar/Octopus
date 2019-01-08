package cn.chenhuanming.octopus.model.formatter;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenhuanming
 * Created at 2018/12/17
 */
public class DateFormatter extends AbstractFormatter<Date> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateFormatter.class);

    private final DateFormat FORMAT;

    public DateFormatter(String format) {
        this.FORMAT = new SimpleDateFormat(format);
    }

    @Override
    public String format(Date date) {
        if (date == null) {
            return null;
        }
        return FORMAT.format(date);
    }

    @Override
    Date parseImpl(String str) throws Exception {
        if (Strings.isNullOrEmpty(str)) {
            return null;
        }
        return FORMAT.parse(str);
    }
}
