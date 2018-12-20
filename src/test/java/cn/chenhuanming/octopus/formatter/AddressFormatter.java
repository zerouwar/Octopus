package cn.chenhuanming.octopus.formatter;

import cn.chenhuanming.octopus.entity.Address;
import cn.chenhuanming.octopus.model.CellFormatter;

/**
 * @author chenhuanming
 * Created at 2018/12/19
 */
public class AddressFormatter implements CellFormatter<Address> {
    @Override
    public String format(Address address) {
        return address.getCity() + "," + address.getDetail();
    }

    @Override
    public Address parse(String str) {
        return null;
    }
}
