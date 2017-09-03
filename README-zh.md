<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Octopus](#octopus)
	- [如何导入excel](#如何导入excel)
	- [如何导出excel](#如何导出excel)

<!-- /TOC -->
# Octopus
 `Octopus` 是一个简单的java excel导入导出工具.

## 如何导入excel
下面是一个excel文件中sheet的数据，有四个学生信息.

| studentId | name  | sex |   inTime   | score |
| --------- | ----- | --- | ---------- | ----- |
| 20134123  | John  | M   | 2013-9-1   | 89    |
| 20124524  | Joyce | F   | 20123-8-31 | 79    |
| 20156243  |       | P   | 2015-5-15  | 94    |
| 20116522  | Nemo  | F   | 2011-2-26  |       |

一个学生类，用来保存从excel中读取的学生信息.

    //lombok annotations
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public class Student {

        @ModelLineNumber
        private int lineNum;

        @ModelProperty(value = "id",blankable = false)
        private String studentId;

        @ModelProperty(value = "name",defaultValue = "anonymous")
        private String name;

        @ModelProperty(value = "sex",wrongMsg = "sex must be M or F",pattern = "^M|F$")
        private String sex;

        @ModelProperty(value = "admission",wrongMsg = "admission must be a date")
        private LocalDate inTime;

        @ModelProperty(value = "score",wrongMsg = "score must be numeric",defaultValue = "100")
        private Double score;

    }

用代码读取excel，并输出学生信息：

    InputStream is = getClass().getResourceAsStream("/test.xlsx");
    Workbook workbook = WorkbookFactory.create(is);
    Sheet sheet = workbook.getSheetAt(0);

    //read students with ReusableSheetReader
    SheetReader<ModelEntity<Student>> students = new ReusableSheetReader<>(sheet,1,0,Student.class);

    //print students information
    for (ModelEntity<Student> student:students) {
        System.out.println(student.toString());
    }

输出的学生信息

    SimpleModelEntity(entity=Student(lineNum=2, studentId=20134123, name=John, sex=M, inTime=2013-09-01, score=89.0, gradeAndClazz=null), exceptions=[])
    SimpleModelEntity(entity=Student(lineNum=3, studentId=20124524, name=Joyce, sex=F, inTime=null, score=79.0, gradeAndClazz=null), exceptions=[cn.chenhuanming.octopus.exception.DataFormatException: in cell (3,4) ,20123-8-31 can not be formatted to class java.time.LocalDate])
    SimpleModelEntity(entity=Student(lineNum=4, studentId=20156243, name=anonymous, sex=null, inTime=2015-05-15, score=94.0, gradeAndClazz=null), exceptions=[cn.chenhuanming.octopus.exception.PatternNotMatchException: P and ^M|F$ don't match!])
    SimpleModelEntity(entity=Student(lineNum=5, studentId=20116522, name=Nemo, sex=F, inTime=2011-02-26, score=100.0, gradeAndClazz=null), exceptions=[])

通过`ModelEntity<Student>`，可以获取更多异常信息，例如`@ModelProperty`的配置信息和所发生的异常.

***完整的测试用例：`src/test/cn/chenhuanming/octopus/core/SheetReaderTest`***

## 如何导出excel
为了说明导出的特性，我们给`Student`类增加一个属性`GradeAndClazz`用来表示年级和班级.下面是最终的`Student`类，可以用来导入导出.

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public class Student {

        @ModelLineNumber
        private int lineNum;

        @ModelProperty(value = "id",blankable = false)
        private String studentId;

        @ModelProperty(value = "name",defaultValue = "anonymous")
        private String name;

        @ModelProperty(value = "sex",wrongMsg = "sex must be M or F",pattern = "^M|F$")
        private String sex;

        //jackson annotation to format output
        @JsonFormat(pattern = "yyyy-MM-dd")
        @ModelProperty(value = "admission",wrongMsg = "admission must be a date")
        private LocalDate inTime;

        @ModelProperty(value = "score",wrongMsg = "score must be numeric",defaultValue = "100")
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

`GradeAndClazz`类，只有年级和班级两个信息.

    @Getter
    @Setter
    @AllArgsConstructor
    public class GradeAndClazz{
        private String grade;
        private String clazz;
    }

需要一个xml来配置导出的属性和属性描述作为表头

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

用代码导出学生信息

    //prepare workbook and stuednts objects
    Workbook workbook = new XSSFWorkbook();
    String rootPath = this.getClass().getClassLoader().getResource("").getPath();
    FileOutputStream os = new FileOutputStream(rootPath+"/export.xlsx");
    GradeAndClazz gradeAndClazz = new GradeAndClazz("2014","R6");
    Student student1 = new Student("201223","John","M", LocalDate.now(),98.00,gradeAndClazz);
    Student student2 = new Student("204354","Tony","M", LocalDate.now(),87.00,gradeAndClazz);
    Student student3 = new Student("202432","Joyce","F", LocalDate.now(),90.00,gradeAndClazz);

    //write excel with OneSheetExcelWriter
    ExcelWriter<Student> studentExcelWriter = new OneSheetExcelWriter<>(getClass().getClassLoader().getResourceAsStream("studentExport.xml"));

    studentExcelWriter.write(workbook,Arrays.asList(student1,student2,student3));
    workbook.write(os);

导出结果

                                              |    class info      |
    id        name    M     admission   score |---------|----------|
                                              |  grade  |   class  |
    ---------------------------------------------------------------|
    201223    John    M     2017-07-06  98.0  |  2014   |   R6     |
    204354    Tony    M     2017-07-06  87.0  |  2014   |   R6     |
    202432    Joyce   F     2017-07-06  90.0  |  2014   |   R6     |

可以看到，对于gradeAndClazz属性，会用一个合并单元格来表示.admission因为被`@JsonFormat`标记，因此会格式化输出日期。事实上`Octopus`会调用`jackson`来格式化json后再写入excel.

***详细例子在 `src/test/cn/chenhuanming/octopus/core/OneSheetExcelWriterTest`***
