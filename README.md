
<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Octopus](#octopus)
    - [Import from Maven](#import-from-maven)
	- [Export Excel](#export-excel)
	    - [Simplest Example](#simplest-example)
	    - [Auto Drawing Header](#auto-drawing-header)
	    - [Converting Data](#converting-data)
	- [Import Excel](#import-excel)
	    - [Data Validation](#data-validation)
    - [Q&A](#qa)
        - [No Java Annotation Config?](#no-java-annotation-config)
        - [Need Apache POI?](#need-apache-poi)
        - [Have Advice or idea?](#have-advice-or-idea)
<!-- /TOC -->

[跳去中文版](https://github.com/zerouwar/Octopus/blob/master/README-zh.md)

# Octopus
 `Octopus` is a simple Java excel import and export tool,
 aiming to not touch Apache POI API for simply import or export excel.
 At the meantime,you can customize cell style,validate importing data according some rule
 and convert data you want

**Talk less,directly see a picutre**
![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/applicant_example.png)

## Import from Maven

Add repository

```xml
<repositories>
    <repository>
        <id>chenhuanming-repo</id>
        <name>chenhuanming-repo</name>
        <url>https://raw.githubusercontent.com/zerouwar/my-maven-repo/master</url>
    </repository>
</repositories>
```


Add dependency

```xml
<dependency>
        <groupId>cn.chenhuanming</groupId>
        <artifactId>octopus</artifactId>
        <version>1.0.0</version>
</dependency>
```


## Export Excel

### Simplest Example
Let's begin with simplest example,to export some addresses.Only two fields in `Address` class

```java
@Data
@AllArgsConstructor
public class Address {
    private String city;
    private String detail;
}
```

Before exporting,we must create a XML config to define how and what to export

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Root xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:noNamespaceSchemaLocation="https://raw.githubusercontent.com/zerouwar/my-maven-repo/master/cn/chenhuanming/octopus/1.0.0/octopus.xsd"
      class="cn.chenhuanming.octopus.entity.Address">

    <Field name="city" description="City"/>
    <Field name="detail" description="Detail"/>

</Root>
```  

First,create a root element named `Root`.Here refer a *octopus.xsd* file to help us compose XML code.

Second,assign `class` attribute,which is the class name we will export

Last,create two `Field` element,representing two column data read from `Address` we will export.

Value of Attribute `name` is the field name of Address.
Actually Octopus get value from its getter method,so make sure there is a getter method.

Attribute `description` will be used to draw header

Let's do the last thing,writing Java code

```java
public class AddressExample {
    List<Address> addresses;

    /**
     * make testing data
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
    public void export() throws FileNotFoundException {

        //where to export
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/address.xlsx");

        //read config from address.xml
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("address.xml");
        ConfigReader configReader = Octopus.getXMLConfigReader(is);

        try {
            Octopus.writeOneSheet(os, configReader, "address", addresses);
        } catch (IOException e) {
            System.out.println("export failed");
        }
    }
}
```

This is a complete unit test.In fact,exporting Excel only needs two steps:

1. Create a `ConfigReader` instance from XML config file
2. Call `Octopus.writeOneSheet()` and pass exporting file,configReader,name of sheet and data

Here is exporting excel file

![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/simplest_example.png)

### Auto Drawing Header
Octopus supports exporting complicated object and drawing header automatically.

This Time we need to export some companies.Here is `Company` class

```java
@Data
@AllArgsConstructor
public class Company {
    private String name;
    private Address address;
}
```

And we create a XML config file named *company.xml*

```xml
<Root xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:noNamespaceSchemaLocation="https://raw.githubusercontent.com/zerouwar/my-maven-repo/master/cn/chenhuanming/octopus/1.0.0/octopus.xsd"
      class="cn.chenhuanming.octopus.entity.Address">


    <Field name="name"
           description="Name"
           color="#ff0000"/>

    <Header name="address" description="Address">
        <Field name="city" description="City"/>
        <Field name="detail" description="Detail"/>
    </Header>

</Root>
```

We use `Header` element representing a complicated field in company object,and set font color of `name` to `#ff0000`

The Java code is almost the same as before

```java
public class CompanyExample {
    List<Company> companies;

    /**
     * make testing data
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
    public void export() throws FileNotFoundException {

        //where to export
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();
        FileOutputStream os = new FileOutputStream(rootPath + "/company.xlsx");

        //read config from company.xml
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("company.xml");
        ConfigReader configReader = Octopus.getXMLConfigReader(is);

        try {
            Octopus.writeOneSheet(os, configReader, "company", companies);
        } catch (IOException e) {
            System.out.println("export failed");
        }
    }

}
```

This is the final exporting excel file

![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/auto_drawing_header.png)

Octopus can handle more complicated data,you can check this from `cn.chenhuanming.octopus.example.ApplicantExample` in test classpath

![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/applicant_example.png)

### Converting Data
Sometimes you want to convert exporting data.For example,in previous example,we don't want to export entire `Address`,just make this like a string.

All we need is implementing `Formatter`

```java
public class AddressFormatter implements Formatter<Address> {
    @Override
    public String format(Address address) {
        return address.getCity() + "," + address.getDetail();
    }

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
 
 At last,put this into XML file
 
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
We directly reuse previous example to see how to import a excel.Reuse `ConfigReader` object,just change Java code

```java
//First get the excel file
FileInputStream fis = new FileInputStream(rootPath + "/company2.xlsx");

try {
    SheetReader<Company> importData = Octopus.readFirstSheet(fis, configReader, new DefaultCellPosition(1, 0));

    
    for (Company company : importData) {
        System.out.println(company);
    }
} catch (Exception e) {
    System.out.println("import failed");
}
```

And check the output of terminal.See,`AddressFormatter` we created works!

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
SomeTimes we have some requirements for importing data.Octopus provides simple validation config.

First of all,we add a field `status` for `Company` class.And value of `status` is only one of *good*,*bad* and *closed*,`name` can not be empty.
Let's check out XML config

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Root xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:noNamespaceSchemaLocation="https://raw.githubusercontent.com/zerouwar/my-maven-repo/master/cn/chenhuanming/octopus/1.0.0/octopus.xsd"
      class="cn.chenhuanming.octopus.entity.Company">


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

And the Java code

```java
@Test
public void importCheckedData() throws IOException, InvalidFormatException {
    InputStream is = this.getClass().getClassLoader().getResourceAsStream("wrongCompany.xlsx");

    ConfigReader configReader = new XmlConfigReader(this.getClass().getClassLoader().getResourceAsStream("company3.xml"));

    final SheetReader<CheckedData<Company>> sheetReader = Octopus.readFirstSheetWithValidation(is,configReader,new DefaultCellPosition(1,0));

    for (CheckedData<Company> checkedData : sheetReader) {
        System.out.println(checkedData);
    }
}
```

We call `Octopus.readFirstSheetWithValidation` and get `SheetReader` with result of validation.Check the terminal

```
CheckedData(data=Company(name=Graham Motor Services, address=Address(city=Monroe, detail=666 Bonnair Ave), status=good), exceptions=[])
CheckedData(data=Company(name=Social Circle Engineering, address=Address(city=Fort Gaines, detail=956 Third Ridge), status=null), exceptions=[cn.chenhuanming.octopus.exception.NotAllowValueException])
CheckedData(data=Company(name=null, address=Address(city=Mcdonough, detail=1278 Midway Trail), status=null), exceptions=[cn.chenhuanming.octopus.exception.CanNotBeBlankException, cn.chenhuanming.octopus.exception.NotAllowValueException])
```

`CheckData` has `data` and `exceptions`.In `exceptions`,it saves all exceptions of every cell occurred in importing.All of them are subclass of `ParseException`

Besides `is-blankable` and `options`,you can apply regular expression validation through `regex`.When validate fails,it will throw corresponding `ParseException`

* `is-blankable`：throws `CanNotBeBlankException`
* `options`：throws `NotAllowValueException`
* `regex`：throws `PatternNotMatchException`

You can handle more with these exceptions.If it can not be satisfied with you,you can throws `ParseException` in `paese` method of `Formatters`.
Octopus will catch them,put into `exceptions` and fill with position of cell and config info at the same time.

***All example could be founud at `cn.chenhuanming.octopus.example`，you can run and check these examples*** 

## Q&A

### No Java Annotation Config?
For now,only XML config because XML and class file are separate.Sometimes you can not modify class file,especially when exporting excel,XML will be better choice. 
If you are "anti-xml", can implements `ConfigReader`,it will not be hard.

### Need Apache POI?
`Octopus` provides one-code-api,get rid of Apache API。If you really need Apache POI,check core class `SheetWriter`和`SheetReader`

### Have Advice or idea?
email me **chenhuanming.cn@gmail.com**

