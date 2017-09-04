
<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Octopus](#octopus)
	- [How to import excel](#how-to-import-excel)
	- [How to export excel](#how-to-export-excel)

<!-- /TOC -->

[跳去中文版](https://github.com/zerouwar/Octopus/blob/master/README-zh.md)

# Octopus
 `Octopus` is a simple java excel import and export tool.

## Import from Maven

Add repository

		<repositories>
		    <repository>
		        <id>chenhuanming-repo</id>
		        <name>chenhuanming-repo</name>
		        <url>https://raw.githubusercontent.com/zerouwar/my-maven-repo/master</url>
		    </repository>
		</repositories>

Add dependency

		<dependency>
				<groupId>cn.chenhuanming</groupId>
				<artifactId>octopus</artifactId>
				<version>1.0-SNAPSHOT</version>
		</dependency>

## How to import excel
Here is a sheet of excel represents four students information.

| studentId | name  | sex |   inTime   | score |
| --------- | ----- | --- | ---------- | ----- |
| 20134123  | John  | M   | 2013-9-1   | 89    |
| 20124524  | Joyce | F   | 20123-8-31 | 79    |
| 20156243  |       | P   | 2015-5-15  | 94    |
| 20116522  | Nemo  | F   | 2011-2-26  |       |

And we have a `Student` class to save student information.

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

Assemble students object with following code.

    InputStream is = getClass().getResourceAsStream("/test.xlsx");
    Workbook workbook = WorkbookFactory.create(is);
    Sheet sheet = workbook.getSheetAt(0);

    //read students with ReusableSheetReader
    SheetReader<ModelEntity<Student>> students = new ReusableSheetReader<>(sheet,1,0,Student.class);

    //print students information
    for (ModelEntity<Student> student:students) {
        System.out.println(student.toString());
    }

output result.

    SimpleModelEntity(entity=Student(lineNum=2, studentId=20134123, name=John, sex=M, inTime=2013-09-01, score=89.0, gradeAndClazz=null), exceptions=[])
    SimpleModelEntity(entity=Student(lineNum=3, studentId=20124524, name=Joyce, sex=F, inTime=null, score=79.0, gradeAndClazz=null), exceptions=[cn.chenhuanming.octopus.exception.DataFormatException: in cell (3,4) ,20123-8-31 can not be formatted to class java.time.LocalDate])
    SimpleModelEntity(entity=Student(lineNum=4, studentId=20156243, name=anonymous, sex=null, inTime=2015-05-15, score=94.0, gradeAndClazz=null), exceptions=[cn.chenhuanming.octopus.exception.PatternNotMatchException: P and ^M|F$ don't match!])
    SimpleModelEntity(entity=Student(lineNum=5, studentId=20116522, name=Nemo, sex=F, inTime=2011-02-26, score=100.0, gradeAndClazz=null), exceptions=[])

You can get more error details through exceptions in `ModelEntity<Student>`.

***complete test case at `src/test/cn/chenhuanming/octopus/core/SheetReaderTest`***

## How to export excel
We add `GradeAndClazz` field into `Student` class so as to show export features.This is final `Student` class which can be used to import and export.

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

And the `GradeAndClazz` class.

    @Getter
    @Setter
    @AllArgsConstructor
    public class GradeAndClazz{
        private String grade;
        private String clazz;
    }

Use xml to config fields that needs to export.

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

Export students with following code.

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

Here is the export result.

                                              |    class info      |
    id        name    M     admission   score |---------|----------|
                                              |  grade  |   class  |
    ---------------------------------------------------------------|
    201223    John    M     2017-07-06  98.0  |  2014   |   R6     |
    204354    Tony    M     2017-07-06  87.0  |  2014   |   R6     |
    202432    Joyce   F     2017-07-06  90.0  |  2014   |   R6     |

As you see,it will merge cells.Note that value of admission is formatted through `@JsonFormat`.Actually,`Octopus` calls `jackson` to serialize students to json and write to excel.

***For more details,run unit test with `src/test/cn/chenhuanming/octopus/core/OneSheetExcelWriterTest`***
