# Octopus
 `Octopus` is a simple java excel import and export tool.

***

## Dependency
 - poi 3.16
 - lombok 1.16.14
 - jackson 2.8.6

***

## How to import
Now, we have four student information in excel file stored in src/test/java/resources:

    studentId   name    sex    inTime       score
    -----------------------------------------------
    20134123    John    M      2013-9-1     89
    20124524            F      20122-81-31  79
    20156243    Joyce   2      2012-5-15    qwe
    20116522    Nemo    F
    
***

`Student` class

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

What we want is transforming four rows of data into four student object and validate.

Let's see `Student` class,`lineNum` is the row number you see in excel,which marked by `@ModelLineNumber`.
We can customize more information with `@ModelProperty`.There are some properties in `@ModelProperty` .

- `value` : the description of field.
- `defaultValue`:default value of field when the corresponding cell is blank in excel.
- `blankable`: whether cell can be blank in excel or not.
- `wrongMsg` : customized hint to user,not for programmer.
- `pattern` : regex pattern used to validate data.

***

Now,we read excel with `SheetReader` and get four students.Before that,we need use `POI` to get one `Sheet` object.

    InputStream is = getClass().getResourceAsStream("/test.xlsx");
    Workbook workbook = WorkbookFactory.create(is);
    Sheet sheet = workbook.getSheetAt(0);

    //从索引为1的row，索引为0的col（跟POI一样）开始读取，转换为Student对象
    SheetReader<ModelEntity<Student>> students = new ReusableSheetReader<>(sheet,1,0,Student.class);

At the end of code,we get one `SheetReader` object,`SheetReader` implements `Iterable`,so read of excel likes this.

    for (ModelEntity<Student> student:students) {
      System.out.println(student.getEntity().toString());
    }

The result of console.

    Student(lineNum=2, studentId=20134123, name=John, sex=M, inTime=2013-09-01, score=89.0)
    Student(lineNum=3, studentId=20124524, name=Joyce, sex=F, inTime=null, score=79.0)
    Student(lineNum=4, studentId=20156243, name=anonymous, sex=null, inTime=2015-05-15, score=94.0)
    Student(lineNum=5, studentId=20116522, name=Nemo, sex=F, inTime=2011-02-26, score=100.0)

***

At Last,pay attention on exception of transformation and print student in the loop.

    SimpleModelEntity(entity=Student(lineNum=2, studentId=20134123, name=John, sex=M, inTime=2013-09-01, score=89.0), exceptions=[])
    SimpleModelEntity(entity=Student(lineNum=3, studentId=20124524, name=Joyce, sex=F, inTime=null, score=79.0), exceptions=[cn.chenhuanming.octopus.exception.DataFormatException: in cell (3,4) ,20123-8-31 can not be formatted to class java.time.LocalDate])
    SimpleModelEntity(entity=Student(lineNum=4, studentId=20156243, name=anonymous, sex=null, inTime=2015-05-15, score=94.0), exceptions=[cn.chenhuanming.octopus.exception.PatternNotMatchException: P and ^M|F$ don't match!])
    SimpleModelEntity(entity=Student(lineNum=5, studentId=20116522, name=Nemo, sex=F, inTime=2011-02-26, score=100.0), exceptions=[])

There is one exception in second student named Joyce because her `inTime` is not a correct date.The third
student's `sex` is not M or F so he has one `PatternNotMatchException`,in addition,his name is anonymous because of
`defaultValue`.

All type of elements of `exceptions` in `ModelEntity` is `ExcelImportException`.

On a separate note,`ReusableSheetReader` will reuse entity in order to avoid creating more object.
Thus,you should be care of this.If you want a new object in every loop,`SimpleSheetReader` will be nice choice.

*complete demo is src/test/java/cn/chenhuanming/octopus/core/RowAssemblerSheetReaderTest*

***

## Export
Just like import,we will export some students' data.This time we add one field into `Student`

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

There are two string fields in `GradeAndClazz`,`grade` and `clazz`(class is the key word in java).

***

We need a xml configuration file to indicate strategy of export.

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

`Octopus` will export the field of student according to the order of `<Filed>` declaration.
`name` is name of field in class and `description` will be used to write table head.

***

Now,write excel with `ExcelWriter`.

    Workbook workbook = new XSSFWorkbook();
    String rootPath = this.getClass().getClassLoader().getResource("").getPath();
    FileOutputStream os = new FileOutputStream(rootPath+"/export.xlsx");

    GradeAndClazz gradeAndClazz = new GradeAndClazz("2014","R6");
    Student student1 = new Student("201223","John","M", LocalDate.now(),98.00,gradeAndClazz);
    Student student2 = new Student("204354","Tony","M", LocalDate.now(),87.00,gradeAndClazz);
    Student student3 = new Student("202432","Joyce","F", LocalDate.now(),90.00,gradeAndClazz);

    //advice,new ExcelWriter in initialization of application and it should be single.
    ExcelWriter<Student> studentExcelWriter = new OneSheetExcelWriter<>(getClass().getClassLoader().getResourceAsStream("studentOutput.xml"));
    //往Excel中写入这三个Student
    studentExcelWriter.write(workbook,Arrays.asList(student1,student2,student3));

The final export excel.

                                              |    class info      |
    id        name    M     admission   score |---------|----------|
                                              |  grade  |   class  |
    ---------------------------------------------------------------|
    201223    John    M     2017-07-06  98.0  |  2014   |   R6     |
    204354    Tony    M     2017-07-06  87.0  |  2014   |   R6     |
    202432    Joyce   F     2017-07-06  90.0  |  2014   |   R6     |


*complete demo is src/test/java/cn/chenhuanming/octopus/core/OneSheetExcelWriterTest*

### Theory of `Octopus` export part
Actually,`Octopus` transforms Collection of data to a tree,then traverses this tree and write
excel according xml configuration file.The transformation is commissioned to `jackson`.
This brings us benefit is we can use tool of `jackson` to customize the output.
