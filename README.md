
<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Octopus](#octopus)
	- [How to import excel](#how-to-import-excel)
	- [How to export excel](#how-to-export-excel)

<!-- /TOC -->

[跳去中文版](https://github.com/zerouwar/Octopus/blob/master/README-zh.md)

# Octopus
 `Octopus` is a simple Java excel import and export tool,
 aiming to not touch Apache POI API for simply import or export excel.
 At the meantime,you can customize cell style,validate importing data according some rule
 or convert data you want

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
				<version>1.0.0</version>
		</dependency>

## Export Excel
Let's export some applicants data.Here is out `Employee` Info

```$xslt

```

***For more details,run unit test with `src/test/cn/chenhuanming/octopus/core/OneSheetExcelWriterTest`***
