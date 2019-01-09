package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.entity.Address;
import cn.chenhuanming.octopus.entity.Employee;
import cn.chenhuanming.octopus.entity.ID;
import cn.chenhuanming.octopus.entity.Job;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
public class AbstractWriterTest {
    List<Employee> employeeList;

    @Before
    public void before() {
        employeeList = new ArrayList<>();
        DataFactory df = new DataFactory();
        for (int i = 0; i < df.getNumberBetween(5, 10); i++) {
            Employee employee = new Employee();
            employee.setId(new ID(df.getNumberBetween(0, 100)));
            employee.setName(df.getName());
            employee.setJob(new Job(new Address(df.getCity(), df.getAddress()), df.getBusinessName()));
            employee.setEntryDate(df.getBirthDate());
            employee.setWorking((i & 1) == 0);
            employeeList.add(employee);

        }
    }
}
