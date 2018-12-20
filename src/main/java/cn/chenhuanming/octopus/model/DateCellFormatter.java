package cn.chenhuanming.octopus.model;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenhuanming
 * Created at 2018/12/17
 */
public class DateCellFormatter implements CellFormatter<Date> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateCellFormatter.class);

    private final DateFormat FORMAT;

    public DateCellFormatter(String format) {
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
    public Date parse(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return null;
        }
        try {
            return FORMAT.parse(str);
        } catch (ParseException e) {
            LOGGER.debug("format fail", e);
            return null;
        }
    }
}
