package cn.chenhuanming.octopus.formatter;

import cn.chenhuanming.octopus.entity.Address;
import cn.chenhuanming.octopus.exception.ParseException;

/**
 * @author chenhuanming
 * Created at 2018/12/19
 */
public class AddressFormatter implements Formatter<Address> {
    @Override
    public String format(Address address) {
        return address.getCity() + "," + address.getDetail();
    }

    @Override
    public Address parse(String str) throws ParseException {
        String[] split = str.split(",");
        if (split.length != 2) {
            return null;
        }
        return new Address(split[0], split[1]);
    }
}
