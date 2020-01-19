<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Octopus](#octopus)
    - [Import from Maven](#import-from-maven)
	- [Export Excel](#export-excel)
	    - [Simplest Example](#simplest-example)
	    - [Auto Drawing Header](#auto-drawing-header)
	    - [Converting Data](#converting-data)
	- [Import Excel](#import-excel)
	    - [Data Validation](#data-validation)
    - [Annotation](#annotation)
    - [Q&A](#qa)
        - [Need Apache POI?](#need-apache-poi)
        - [Have Advice or idea?](#have-advice-or-idea)
<!-- /TOC -->

[跳去中文版](https://github.com/zerouwar/Octopus/blob/master/README-zh.md)

# Octopus

[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/mockito/mockito/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/badge/maven-octopus-blue.svg)](https://search.maven.org/search?q=g:cn.chenhuanming%20AND%20a:octopus)

**Simple** Java excel import and export tool.You can finish work with some object instead of annoying `Apache POI` API.

Besides,some additional function like customizing cell style,converting data during export,validating data during import,etc.

***A complicated excel exporting with Octopus***
![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/applicant_example.png)

## Import from Maven

```xml
<dependency>
        <groupId>cn.chenhuanming</groupId>
        <artifactId>octopus</artifactId>
        <version>1.1.4</version>
</dependency>
```


## Export Excel

### Simplest Example
Let's begin with a simple example,exporting some address information

![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/simplest_example.png)

Define class `Address`

```java
@Data
@AllArgsConstructor
public class Address {
    private String city;
    private String detail;
}
```

We need a xml config to define what and how to export

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Root class="cn.chenhuanming.octopus.entity.Address">

    <Field name="city" description="City"/>
    <Field name="detail" description="Detail"/>

</Root>
```  

Element `Field` represents one column in excel.
Attribute `name` is the field name of class `Address`.Attribute `description` is the column header in excel

Actually `Octopus` get value from getter method,so make sure there is a getter method.

Last,writing code to export

```java
public class AddressExample {
    List<Address> addresses;

    /**
     * preparing testing data
     */
    @Before
    public void prepare() {
        addresses = new ArrayList<>();
        DataFactory df = new DataFactory();
        for (int i = 0; i < df.getNumberBetween(5, 10); i++) {
            addresses.add(new Address(df.getCity(), df.getAddress()));
        }
    }

    @Test
    public void export() throws Exception {

        //where to export
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/address.xlsx");

        //get config from xml file.Singleton pattern is recommending
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("address.xml");
        Config config = new XmlConfigFactory(is).getConfig();

        //just one line of code with Octopus facade
        Octopus.writeOneSheet(os, config, "address", addresses);
    }
}
```

This is a complete unit test.You can find it in [test classpath](https://github.com/zerouwar/Octopus/blob/master/src/test/java/cn/chenhuanming/octopus/example/AddressExample.java) 

### Auto Drawing Header
You can exporting complex structure data.`Octopus` will drawing header automatically.

Now,we will export some company information.Here is the class `Company`

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private String name;
    private Address address;
}
```

XML config file is different from first example.

```xml
<Root class="cn.chenhuanming.octopus.entity.Address">


    <Field name="name"
           description="Name"
           color="#ff0000"/>

    <Header name="address" description="Address">
        <Field name="city" description="City"/>
        <Field name="detail" description="Detail"/>
    </Header>

</Root>
```

This time we set font color of column name to red in excel.
Element `Header` represents a complicated field in company object,

The Java code is almost the same as before

```java
public class CompanyExample {
    List<Company> companies;

    /**
     * preparing testing data
     */
    @Before
    public void prepare() {
        companies = new ArrayList<>();
        DataFactory df = new DataFactory();
        for (int i = 0; i < df.getNumberBetween(5, 10); i++) {
            companies.add(new Company(df.getBusinessName(), new Address(df.getCity(), df.getAddress())));
        }
    }

    @Test
    public void export() throws Exception {

        //where to export
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/company.xlsx");

        //get config from xml file.Singleton pattern is recommending
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("company.xml");
        Config config = new XmlConfigFactory(is).getConfig();

        Octopus.writeOneSheet(os, config, "company", companies);
    }
}
```

Following is the exported excel

![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/auto_drawing_header.png)

Octopus can handle more complicated data,you can check this from `cn.chenhuanming.octopus.example.ApplicantExample` in test classpath

![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/applicant_example.png)

### Converting Data
Sometimes you want to convert data during export.
For example,in previous example,we want export address as one column.

We need to define a `Formatter`

```java
public class AddressFormatter implements Formatter<Address> {
    @Override
    public String format(Address address) {
        return address.getCity() + "," + address.getDetail();
    }

    /**
     * called during import 
     */
    @Override
    public Address parse(String str) {
        String[] split = str.split(",");
        if (split.length != 2) {
            return null;
        }
        return new Address(split[0], split[1]);
    }
}
```

`parse` method is used in importing excel,so just pay attention on `format
 method.It accepts a `Address` object and returns a `String` object.
 
 At last,put this into XML config file
 
 ```xml
<Field name="name"
           description="Name"
           color="#ff0000"/>

<Field name="address"
       description="Address"
       formatter="cn.chenhuanming.octopus.formatter.AddressFormatter"/>
```

Exporting excel will be like this

![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/convering_data.png)

## Import Excel
Let's how to import a excel.Reuse xml config file in [Converting Data](#Converting Data)

```java
SheetReader<Company> importData = Octopus.readFirstSheet(fis, config, new DefaultCellPosition(1, 0));

for (Company company : importData) {
    System.out.println(company);
}
```

Check terminal.See,`AddressFormatter` works!

```
Company(name=Graham Motor Services, address=Address(city=Monroe, detail=666 Bonnair Ave))
Company(name=Social Circle Engineering, address=Address(city=Fort Gaines, detail=956 Third Ridge))
Company(name=Enigma Cafe, address=Address(city=Mcdonough, detail=1278 Midway Trail))
Company(name=Hapeville Studios, address=Address(city=Riceboro, detail=823 Tuscarawas Blvd))
Company(name=Thalman Gymnasium, address=Address(city=Ebenezer, detail=1225 Blackwood Avenue))
Company(name=Sparks Pro Services, address=Address(city=Darien, detail=1362 Woodlawn Lane))
Company(name=Toccoa Development, address=Address(city=Ridgeville, detail=1790 Lawn Ave))
```

### Data Validation
SomeTimes we have restrictions for importing data.Octopus provides simple validation config.

we add a property `status` for class `Company`.

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private String name;
    private Address address;
    private String status;
}
```

We want restrictions during import.

* Value of `status` is only one of *good*,*bad* and *closed*.
* `name` can not be empty.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Root class="cn.chenhuanming.octopus.entity.Company">

    <Field name="name"
           description="Name"
           color="#ff0000"
           is-blankable="false"/>

    <Field name="address"
           description="Address"
           formatter="cn.chenhuanming.octopus.formatter.AddressFormatter"
    />

    <Field name="status"
           description="Status"
           options="good|bad|closed"/>
    <!--| split options -->
    
</Root>
```

Here is the excel we will import,there are three wrong.

![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/wrong_data.png)

Java code

```java
@Test
public void importCheckedData() throws IOException, InvalidFormatException {
    InputStream is = this.getClass().getClassLoader().getResourceAsStream("wrongCompany.xlsx");

    Config config = new XmlConfigFactory(this.getClass().getClassLoader().getResourceAsStream("company3.xml")).getConfig();

    final SheetReader<CheckedData<Company>> sheetReader = Octopus.readFirstSheetWithValidation(is,config,new DefaultCellPosition(1,0));

    for (CheckedData<Company> checkedData : sheetReader) {
        System.out.println(checkedData);
    }
}
```

We call `Octopus.readFirstSheetWithValidation` and get `SheetReader` which implements `Iterable`.Check the terminal

```
CheckedData(data=Company(name=Graham Motor Services, address=Address(city=Monroe, detail=666 Bonnair Ave), status=good), exceptions=[])
CheckedData(data=Company(name=Social Circle Engineering, address=Address(city=Fort Gaines, detail=956 Third Ridge), status=null), exceptions=[cn.chenhuanming.octopus.exception.NotAllowValueException])
CheckedData(data=Company(name=null, address=Address(city=Mcdonough, detail=1278 Midway Trail), status=null), exceptions=[cn.chenhuanming.octopus.exception.CanNotBeBlankException, cn.chenhuanming.octopus.exception.NotAllowValueException])
```

`CheckData` has `data` and `exceptions`.In `exceptions`,it saves all exceptions of every cell occurred during import.All of them are subclass of `ParseException`.

Besides `is-blankable` and `options`,you can apply regular expression validation through `regex`.When validate fails,it will throw corresponding `ParseException`

* `is-blankable`：throws `CanNotBeBlankException`
* `options`：throws `NotAllowValueException`
* `regex`：throws `PatternNotMatchException`

You can handle more with these exceptions.If it is not satisfied with you,throws `ParseException` in `paese` method of `Formatter`.
Octopus will catch them,put into `exceptions` and fill with position of cell and config info at the same time.

***All example could be found at `cn.chenhuanming.octopus.example`，you can run and check these examples*** 

## Annotation
We recommend using XML to configure the import and export, because XML configuration is not coupled with classes and is more flexible than annotations.  
Sometimes, you may be less concerned about flexibility and want to put configuration and data classes together, so annotation can be used.  
Annotations are similar to XML files, There are  `@Sheet`,`@Formatter`,`@Header`,`@Field`:
- `@Sheet` on class, with a optional `formatters` attribute represents the global formatter
- `@Formatter` represents a formatter as the `formatters` attribute value of `@Sheet`
- `@Header` on a field of the data class, indicating that the field is a composite field
- `@Field` on a field of the data class, indicating that the field is a single field

Please refer to the XML file for the attribute values of the annotations(show as above examples). The following is an annotation example of a data class:
```java
@Sheet(formatters = {
        @Formatter(target = BigDecimal.class, format = BigDecimalFormatter.class),
})
public class Applicants {
    @Field(description = "Value", color = "#74f441")
    private int id;
    @Field(description = "Name", fontSize = 20, border = "0,2,0,2", borderColor = ",#4242f4,,#4242f4")
    private String name;
    @Header(description = "Job", headerColor = "#4286f4")
    private Job job;
    @Field(description = "Entry Date", dateFormat = "yyyy-MM-dd")
    private Date entryDate;
    @Field(description = "Working/Leaved", options = "Working|Leaved",
            formatter = cn.chenhuanming.octopus.formatter.WorkingFormatter.class, color = "#42f4b9")
    private boolean working = true;
}

```
Usage:
```java
    Config config = new AnnotationConfigFactory(Applicants.class).getConfig();
    // ... use config ...
```

## Q&A

### Need Apache POI?
`Octopus` provides one-code-api,get rid of Apache API。If you really need Apache POI,check core class `SheetWriter`和`SheetReader`

### Have Advice or Question?
New a issue or email **chenhuanming.cn@gmail.com**

