# Octopus
`Octopus`是一个简易的Excel导入导出工具。目前主要就两个功能:

1. 导入：将excel中一行数据转换为指定的java对象，并通过指定的正则表达式检查合法性。
2. 导出：按照给定的xml配置，将一个Collection对象写入到excel中。

项目地址：[Octopus in github](https://github.com/zerouwar/Octopus)

## 相关依赖
- poi 3.16
- lombok 1.16.14
- jackson 2.8.6

## 导入
假设我们需要导入一些学生信息，手上有一个excel文件（在项目src/test/java/resources下的test.xlsx）:

    studentId   name    sex    inTime       score
    -----------------------------------------------
    20134123    John    M      2013-9-1     89
    20124524            F      20122-81-31  79
    20156243    Joyce   2      2012-5-15    qwe
    20116522    Nemo    F          

先看一下`Student`类。

        @Getter
        @Setter
        @ToString
      	public class Student {

      		@ModelLineNumber
      		private int lineNum;

      		@ModelProperty(value = "student's id")
      		private String studentId;

      		@ModelProperty(value = "student's name",defaultValue = "anonymous")
      		private String name;

      		@ModelProperty(value = "student's sex",wrongMsg = "sex must be M or F",pattern = "^M|F$")
      		private String sex;

      		@ModelProperty(value = "student's admission",wrongMsg = "admission must be a date")
      		private LocalDate inTime;

      		@ModelProperty(value = "student's score",wrongMsg = "score must be numberic",defaultValue = "100")
      		private Double score;

      	}

我们的目标就是把这四行数据转换成四个`Student`对象，并且能简单检查一下数据合法性。

再把目光放到`Student`类，`lineNum`表示该对象对应的row在excel中的行号，需要用`@ModelLineNumber`来告诉`Octopus`。被`@ModelIgnore`标记的属性不会被`Octopus`处理，**但是即使没有任何注解的属性依然会被处理**。利用`@ModelProperty`可以自定义更多的信息，这里说一下`@ModelProperty`的属性。

- value 该属性的现实含义。
- defaultValue 当excel中没有数据时该属性的默认值。
- blankable  excel中的单元格能否为空白。
- wrongMsg 错误信息，给用户作提示作用，通过`ExcelImportException`获取，后面会说到异常处理。
- pattern 检查数据合法性的正则表达式，这里不匹配时也会在异常里有所体现。

现在，我们使用`SheetReader`来读取这个excel内容，并转换成四个学生对象。在此之前，先用POI读取这个excel文件，拿到sheet对象

    InputStream is = getClass().getResourceAsStream("/test.xlsx");
    Workbook workbook = WorkbookFactory.create(is);
    Sheet sheet = workbook.getSheetAt(0);

    //从索引为1的row，索引为0的col（跟POI一样）开始读取，转换为Student对象
    SheetReader<ModelEntity<Student>> students = new ReusableSheetReader<>(sheet,1,0,Student.class);


最后得到一个`SheetReader`对象，`SheetReader`接口继承了`Iterable`，因此可以通过foreach来迭代读取学生信息。

    for (ModelEntity<Student> student:students) {
      System.out.println(student.getEntity().toString());
    }

输出结果如下

    Student(lineNum=2, studentId=20134123, name=John, sex=M, inTime=2013-09-01, score=89.0)
    Student(lineNum=3, studentId=20124524, name=Joyce, sex=F, inTime=null, score=79.0)
    Student(lineNum=4, studentId=20156243, name=anonymous, sex=null, inTime=2015-05-15, score=94.0)
    Student(lineNum=5, studentId=20116522, name=Nemo, sex=F, inTime=2011-02-26, score=100.0)

现在关注下异常情况，`ModelEntity`除了指定的entity外，还有一个异常集合。

    SimpleModelEntity(entity=Student(lineNum=2, studentId=20134123, name=John, sex=M, inTime=2013-09-01, score=89.0), exceptions=[])
    SimpleModelEntity(entity=Student(lineNum=3, studentId=20124524, name=Joyce, sex=F, inTime=null, score=79.0), exceptions=[cn.chenhuanming.octopus.exception.DataFormatException: in cell (3,4) ,20123-8-31 can not be formatted to class java.time.LocalDate])
    SimpleModelEntity(entity=Student(lineNum=4, studentId=20156243, name=anonymous, sex=null, inTime=2015-05-15, score=94.0), exceptions=[cn.chenhuanming.octopus.exception.PatternNotMatchException: P and ^M|F$ don't match!])
    SimpleModelEntity(entity=Student(lineNum=5, studentId=20116522, name=Nemo, sex=F, inTime=2011-02-26, score=100.0), exceptions=[])

可以看到第二个叫Joyce的学生的inTime不是一个日期，因此在异常中有一个`DataFormatException`。第三个学生的sex因为不是M或F而有`PatternNotMatchException`，同时name是anonymous（默认值）。`ModelEntity`里exceptions的元素类型都是`ExcelImportException`。

需要注意的是，这里的exceptions只是给程序员提示而已。如果需要返回错误提示给用户的话，可以在`ExcelImportException`里获取`@ModelProperty`设定的wrongMsg，或者根据异常类的类型来自定义错误提示。

另外`ModelEntity`里的entity，即自己指定的pojo，一般只做临时存储，尤其注意的是`ReusableSheetReader`这个会重用entity，即迭代过程只有一个entity对象。如果需要每次迭代都要一个全新的对象，可以使用`SimpleSheetReader`。

*上面例子可以通过执行src/test/java/cn/chenhuanming/octopus/core下的`RowAssemblerSheetReaderTest`类来查看运行效果。*

## 导出Excel
导出Excel比导入用起来相对容易一点。但是考虑到可能导出需要自定义的excel内容更多，所以我只做了一个简单的表头绘制和数据填充。

同样的，我们用`Student`来做例子，不过这次加多一个班级属性。

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public class Student {

        @ModelLineNumber
        private int lineNum;

        @ModelProperty(value = "student's id")
        private String studentId;

        @ModelProperty(value = "student's name",defaultValue = "anonymous")
        private String name;

        @ModelProperty(value = "student's sex",wrongMsg = "sex must be M or F",pattern = "^M|F$")
        private String sex;

        @JsonFormat(pattern = "yyyy-MM-dd")
        @ModelProperty(value = "student's admission",wrongMsg = "admission must be a date")
        private LocalDate inTime;

        @ModelProperty(value = "student's score",wrongMsg = "score must be numeric",defaultValue = "100")
        private Double score;

        @ModelIgnore
        private GradeAndClazz gradeAndClazz;

        public Student(String studentId, String name, String sex, LocalDate inTime, Double score,GradeAndClazz gradeAndClazz) {
            this.studentId = studentId;
            this.name = name;
            this.sex = sex;
            this.inTime = inTime;
            this.score = score;
            this.gradeAndClazz = gradeAndClazz;
        }

    }

`GradeAndClazz`有两个字符串属性，grade和clazz（因为class是关键字），分别代表年级和班级。

导出我们需要用到一个xml配置文件，让`Octopus`了解导出策略。

    <?xml version="1.0" encoding="UTF-8"?>
    <ExportModel class="entity.Student">
    <Field name="studentId" description="id"></Field>
    <Field name="name" description="name"></Field>
    <Field name="sex" description="sex"></Field>
    <Field name="inTime" description="admission"></Field>
    <Field name="score" description="score"></Field>
    <Field name="gradeAndClazz" description="class info">
        <Field name="grade" description="grade"></Field>
        <Field name="clazz" description="class"></Field>
    </Field>

    </ExportModel>

其中<ExportModel>的class属性指定了导出用的模版类，每一个<Field>标签代表了模版类的属性，这里<Field>的顺序就对应了导出顺序，name是模版类中属性的名字，description会用来做表格头。

现在，可以开始编写导出代码了。导出用`ExcelWriter`，这里用它的一个实现`OneSheetExcelWriter`。

    Workbook workbook = new XSSFWorkbook();
    String rootPath = this.getClass().getClassLoader().getResource("").getPath();
    FileOutputStream os = new FileOutputStream(rootPath+"/export.xlsx");

    GradeAndClazz gradeAndClazz = new GradeAndClazz("2014","R6");
    Student student1 = new Student("201223","John","M", LocalDate.now(),98.00,gradeAndClazz);
    Student student2 = new Student("204354","Tony","M", LocalDate.now(),87.00,gradeAndClazz);
    Student student3 = new Student("202432","Joyce","F", LocalDate.now(),90.00,gradeAndClazz);

    //指定studentExport.xml映射配置文件，这一步建议在项目中提前初始化并把该对象设成单例。
    ExcelWriter<Student> studentExcelWriter = new OneSheetExcelWriter<>(getClass().getClassLoader().getResourceAsStream("studentOutput.xml"));
    //往Excel中写入这三个Student
    studentExcelWriter.write(workbook,Arrays.asList(student1,student2,student3));

写入excel结果。

                                              |    class info      |
    id        name    M     admission   score |---------|----------|
                                              |  grade  |   class  |
    ---------------------------------------------------------------|
    201223    John    M     2017-07-06  98.0  |  2014   |   R6     |
    204354    Tony    M     2017-07-06  87.0  |  2014   |   R6     |
    202432    Joyce   F     2017-07-06  90.0  |  2014   |   R6     |

*实际效果请运行src/test/java/cn/chenhuanming/octopus/core/OneSheetExcelWriterTest类。*

### 关于`Octopus`的导出原理
导入基本就是获取，检查，赋值，没什么特别的东西。导出虽然看起来比导入简单，但是考虑到自定义输出的可能性更大，由此我想到了可以利用json序列化工具来帮我做对象的转换工作，尤其是这些json序列化工具都会建立一棵树的结构。所以利用这一点，`Octopus`本身并不做数据的序列化工作，由jackson转换后给`Octopus`一个`Node`，`Octopus`就可以根据配置文件，通过遍历这棵树来写入excel。因此前一段序列化工作，完全可以利用jackson的一些注解或者`@JsonSerialize`等自定义序列化。
