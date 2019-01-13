package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.entity.Address;
import cn.chenhuanming.octopus.entity.Applicants;
import cn.chenhuanming.octopus.entity.Company;
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
    protected List<Applicants> applicantsList;

    @Before
    public void before() {
        applicantsList = new ArrayList<>();
        DataFactory df = new DataFactory();
        for (int i = 0; i < df.getNumberBetween(5, 10); i++) {
            Applicants applicants = new Applicants();
            applicants.setId(df.getNumberBetween(0, 100));
            applicants.setName(df.getName());
            Address address = new Address(df.getCity(), df.getAddress());
            Company company = new Company(df.getBusinessName(), address);
            applicants.setJob(new Job(company, df.getRandomWord(), df.getRandomWord()));
            applicants.setEntryDate(df.getBirthDate());
            applicants.setWorking((i & 1) == 0);
            applicantsList.add(applicants);

        }
    }
}
