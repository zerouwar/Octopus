package cn.chenhuanming.octopus.formatter;

import cn.chenhuanming.octopus.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenhuanming
 * Created at 2018/12/17
 */
@Slf4j
public class DateFormatter extends AbstractFormatter<Date> {

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
    public Date parseImpl(String str) throws Exception {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return FORMAT.parse(str);
    }
}
