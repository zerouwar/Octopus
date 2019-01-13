package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.exception.ParseException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
@Data
@NoArgsConstructor
public class CheckedData<D> {
    private D data;
    private List<ParseException> exceptions = new LinkedList<>();
}
