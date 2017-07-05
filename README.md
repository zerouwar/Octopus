# Octopus
A java simple object-row-mapping util used to import and export excel

## Introduction

***Octopus*** can convert row of sheet in the excel(only for XSSF) to object you want,in order to get rid of
most operations of POI(actually ***Octopus*** deal with sheet currently,so you still have to get sheet with POI).

What'more,you can use `ExcelWriter`  to export a excel.

**Dependency**:
- java 8
- apache POI 3.16
- lombok 1.16.14
- jackson 2.8.6

## How to import excel

### As a example,we have a Student class which represents one row of sheet.

Every fields of the Student will be 
converted to one column of a row in the order they are declared,except marked by `@ModelIgnore`.Note that field without any annotation
also will be converted.
***
	@Getter
	@Setter
	public class Student {

		@ModelLineNumber
		private int lineNum;

		@ModelProperty(value = "student's id")
		private String studentId;

		@ModelProperty(value = "student's name",defaultValue = "anonymous")
		private String name;

		@ModelProperty(value = "student's sex",wrongMsg = "sex must be M or F")
		private String sex;

		@ModelProperty(value = "student's admission",wrongMsg = "admission must be a date")
		private LocalDate inTime;

		@ModelProperty(value = "student's score",wrongMsg = "score must be numberic",defaultValue = "100")
		private Double score;

	}

Field marked by `@ModelLineNumber` represents the line number in excel you see(begin with 1),
its data type must be `int` or `Integer`.

***
#### These are properties of `@ModelProperty`

value ------ description of property

defaultValue ------ value of property when cell is blank(In POI's words,blank cell or empty string cell)

wrongMsg ------- hint when process of convert occurs error,such as '2013-211-01' can not be
converted to a `Date` Type field,cell's content and pattern don't match,it will be stored in `ExcelImportException` of `ModelEntity` you got.

pattern ------ regex pattern which will be used to check the content of cell

blankable ------ whether cell can be blank(In POI's words,cell type is blank,cell type is string but empty or cell is null)

***
### we can traverse sheet with following code.

    InputStream is = getClass().getResourceAsStream("/test.xlsx");
	Workbook workbook = WorkbookFactory.create(is);
	Sheet sheet = workbook.getSheetAt(0);
	//convert from index 1 row,index 0 column(same as POI)
    SheetReader<ModelEntity<Student>> students = new ReusableSheetReader<>(sheet,1,0,Student.class);

    for (ModelEntity<Student> student:students) {
        System.out.println(student.getEntity());
    }
`RowAssemblerSheetReader` needs four paramters:sheet,startRow(line number start to convert,
begin with 0, same as POI),startCol(column number start to convert,also begin with 0)
and Class to convert(here is Student.class).

And we got some students,`SheetReader` implements `Iterable`,so we can read with `foreach`.
Every loop we got a **`ModelEntity<Student>`**,`ModelEntity` has two method,`getEntity()` returns
the student object represented a row data,`exceptions()` returns a list of `ExcelImportException`
so you can know what errors occured in the process of convert
***
### Now,this is one sheet of excel.

	studentId   name    sex    inTime       score
	-----------------------------------------------
	20134123    John    M      2013-9-1     89
	20124524            F      20122-81-31  79
	20156243    Joyce   2      2012-5-15    qwe
	20116522    Nemo    F          
***
As we see,first row is correct,so it will be converted to a student object.
Name of second row is blank,so his name will be default value -- anonymous.
Then,second student will have one exception(Actually it's `DataFormatException` which extends `ExcelImportException`),
because inTime of second row is not a correct date.
Third student has two exceptions,first is 'PatternNotMatchException' because 2 is not 'M' or 'F',
second is `DataFormatException` for 'qwe' is not numeric.
At last,Last student has not any exceptions,inTime property is `null`,score is 100.

**you can run test in this maven project to see the real result**

## Export Excel

There are some interfaces to export excel,such as `ExcelWriter`,`SheetWriter`.
We use `OneSheetExcelWriter` implemented `ExcelWriter` to export excel commonly.

`ExcelWriter` will leave work to `SheetWriter`,`SheetWriter` will use jackson to serialize collections to
json string , and then transform json string to `JsonNode`(jackson).finally write to sheet.Thus,you can
format output through jackson.

Now we export some students.

