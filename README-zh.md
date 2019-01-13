
<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Octopus](#octopus)
    - [从Maven导入](#从maven导入)
	- [导出Excel](#导出excel)
	    - [从最简单的例子开始](#从最简单的例子开始)
	    - [自动绘制表头](#自动绘制表头)
	    - [转换数据](#转换数据)
	- [导入Excel](#导入excel)
	    - [导入校验数据](#导入校验数据)
    - [Q&A](#qa)
        - [没有Java注解配置？](#没有java注解配置)
        - [需要操作Apache POI？](#需要操作apache-poi)
        - [有建议或者想法？](#有建议或者想法)
<!-- /TOC -->

# Octopus
 `Octopus` 是一个简单的java excel导入导出工具。目的是不用接触Apache POI的API就可以完成简单的Excel导出导入。
 同时，可以自定义表格样式，导入检验数据和转换数据

**不BB，直接上图**

![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/applicant_example.png)

## 从Maven导入

增加仓库

		<repositories>
		    <repository>
		        <id>chenhuanming-repo</id>
		        <name>chenhuanming-repo</name>
		        <url>https://raw.githubusercontent.com/zerouwar/my-maven-repo/master</url>
		    </repository>
		</repositories>

引入依赖

		<dependency>
				<groupId>cn.chenhuanming</groupId>
				<artifactId>octopus</artifactId>
				<version>1.0.0</version>
		</dependency>

## 导出Excel

### 从最简单的例子开始
我们从最简单的例子开始——导出一些地址数据。`Address`类只有两个属性

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String city;
    private String detail;
}
```

在导出前，我们需要创建一个XML文件定义怎样去导出

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Root xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:noNamespaceSchemaLocation="https://raw.githubusercontent.com/zerouwar/my-maven-repo/master/cn/chenhuanming/octopus/1.0.0/octopus.xsd"
      class="cn.chenhuanming.octopus.entity.Address">

    <Field name="city" description="City"/>
    <Field name="detail" description="Detail"/>

</Root>
```  

首先，创建`Root`根元素。这里引用*octopus.xsd*文件帮助我们编写XML

然后，赋值`class`属性，代表我们要导出的类全限定名

最后，创建两个`Field`元素，代表要导出类的两个属性

`name`属性值就是`Address`里的属性名，实际上Octopus调用其getter方法获取值，所以要确保有getter方法

`description`属性会被用在绘制表头

我们可以开始做最后一件事，编写Java代码

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

这是一个完整的单元测试代码，不过导出Excel其实只要两步：

1. 从XML配置文件中创建一个`ConfigReader`对象
2. 调用`Octopus.writeOneSheet()`，传入导出的文件，configReader，工作表的名字和数据

下面是导出的Excel文件

![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/simplest_example.png)

### 自动绘制表头
Octopus支持导出复杂对象时自动绘制表头

这次我们来导出一些公司数据，这里是`Company`类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private String name;
    private Address address;
}
```

然后我们创建一个 *company.xml* 配置文件

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

我们用`Header`元素代表要导出`Company`的一个复杂属性，同时设置字体颜色是红色

Java代码基本跟之前的一样

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

最后是导出的Excel文件

![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/auto_drawing_header.png)

Octopus可以处理更复杂的数据，你可以在`cn.chenhuanming.octopus.example.ApplicantExample`查看这个更复杂的例子

![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/applicant_example.png)

### 转换数据
有时你想转换导出的数据。例如，在上一个例子中，我们不想导出整个`Address`对象，把它当做一个字符串导出

我们所需要做的只是实现一个`Formatter`

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

`parse`方法用于导入Excel时，只要关注`format`方法。这里接受一个Address对象，返回一个字符串。

最后，配置`AddressFormatter`到XML文件

 ```xml
<Field name="name"
           description="Name"
           color="#ff0000"/>

<Field name="address"
       description="Address"
       formatter="cn.chenhuanming.octopus.formatter.AddressFormatter"/>
```

最后导出的结果

![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/convering_data.png)


## 导入Excel
我们直接拿上一个例子的导出结果来演示导入，共用同一个`ConfigReader`，直接编写导入的代码

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

在控制台可以看到打印导入结果，可以看到，之前的`AddressFormatter`也完成了数据的转换工作

```
Company(name=Graham Motor Services, address=Address(city=Monroe, detail=666 Bonnair Ave))
Company(name=Social Circle Engineering, address=Address(city=Fort Gaines, detail=956 Third Ridge))
Company(name=Enigma Cafe, address=Address(city=Mcdonough, detail=1278 Midway Trail))
Company(name=Hapeville Studios, address=Address(city=Riceboro, detail=823 Tuscarawas Blvd))
Company(name=Thalman Gymnasium, address=Address(city=Ebenezer, detail=1225 Blackwood Avenue))
Company(name=Sparks Pro Services, address=Address(city=Darien, detail=1362 Woodlawn Lane))
Company(name=Toccoa Development, address=Address(city=Ridgeville, detail=1790 Lawn Ave))
```

### 导入校验数据
有时候我们对导入的数据有一定的要求，Octopus提供简单的数据校验配置

首先给我们的`Company`增加一个`status`属性，只能是 *good*,*bad*和*closed* 三个值其中一个，同时`name`不可以为空，看一下XML配置文件

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

这是我们要导入的Excel，可以看到里面有非法数据

![](https://raw.githubusercontent.com/zerouwar/Octopus/master/pictures/wrong_data.png)

看一下怎么编写Java代码

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

这里我们调用`Octopus.readFirstSheetWithValidation`，获取带校验结果的`SheetReader`，看一下导入的结果

```
CheckedData(data=Company(name=Graham Motor Services, address=Address(city=Monroe, detail=666 Bonnair Ave), status=good), exceptions=[])
CheckedData(data=Company(name=Social Circle Engineering, address=Address(city=Fort Gaines, detail=956 Third Ridge), status=null), exceptions=[cn.chenhuanming.octopus.exception.NotAllowValueException])
CheckedData(data=Company(name=null, address=Address(city=Mcdonough, detail=1278 Midway Trail), status=null), exceptions=[cn.chenhuanming.octopus.exception.CanNotBeBlankException, cn.chenhuanming.octopus.exception.NotAllowValueException])
```

可以看到每一个`CheckData`有一个`data`属性和一个`exceptions`列表。
这个异常列表存放着导入时每一个单元格可能出现的校验错误，异常类型都是`ParseException`

除了`is-blankable`和`options`，还可以通过`regex`配置正则表达式检查。当校验错误时，会抛出对应的`ParseException`子类

* `is-blankable`：抛出 `CanNotBeBlankException`
* `options`：抛出 `NotAllowValueException`
* `regex`：抛出 `PatternNotMatchException`

你通过这些异常来进行跟进一步的处理。如果上面三种校验方式不能满足需求，在`Formatter`的`parse`抛出自定义的`ParseException`。Octopus会捕获它们放到`exceptions`列表中，并自动把单元格位置和你的配置内容塞到`ParseException`中

***以上代码都可以在测试路径`cn.chenhuanming.octopus.example`找到，通过这些例子可以感受下Octopus的魅力***

## Q&A

### 没有Java注解配置？
目前只提供XML配置，因为XML和类文件解耦，有时候你无法修改类代码时，尤其是导出场景，XML会是更好的选择。如果你是"anti-xml"，可以实现注解版`ConfigReader`，把注解配置转换成`Field`，这应该不会很难。后面我有空再弄注解配置吧~

### 需要操作Apache POI？
`Octopus`类可以提供一行代码式的API，让你不用碰Apache POI的API。但是如果你确实需要用到Apache POI，可以先看一下Octopus核心类`SheetWriter`和`SheetReader`代码。我在设计的时候尽量考虑扩展，并且完全基于接口实现，实在不行可以选择继承重写，属性基本都是protected，或者直接自己实现接口

### 有建议或者想法？
email我**chenhuanming.cn@gmail.com**

