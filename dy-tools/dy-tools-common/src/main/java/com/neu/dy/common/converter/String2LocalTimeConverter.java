package com.neu.dy.common.converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import com.neu.dy.utils.DateUtils;
import org.springframework.core.convert.converter.Converter;

import static com.neu.dy.utils.DateUtils.DEFAULT_TIME_FORMAT;

/**
 * 解决入参为 Date类型
 *
 */
public class String2LocalTimeConverter extends BaseDateConverter<LocalTime> implements Converter<String, LocalTime> {

    protected static final Map<String, String> FORMAT = new LinkedHashMap(1);

    static {
        FORMAT.put(DateUtils.DEFAULT_TIME_FORMAT, "^\\d{1,2}:\\d{1,2}:\\d{1,2}$");
    }

    @Override
    protected Map<String, String> getFormat() {
        return FORMAT;
    }

    @Override
    public LocalTime convert(String source) {
        return super.convert(source, (key) -> LocalTime.parse(source, DateTimeFormatter.ofPattern(key)));
    }
}
