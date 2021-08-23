package cn.chenhuanming.octopus;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.ConfigFactory;
import cn.chenhuanming.octopus.config.XmlConfigFactory;
import cn.chenhuanming.octopus.config.annotation.AnnotationConfigFactory;
import cn.chenhuanming.octopus.exception.SheetNotFoundException;
import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.CheckedData;
import cn.chenhuanming.octopus.reader.CheckedSheetReader;
import cn.chenhuanming.octopus.reader.DefaultSheetReader;
import cn.chenhuanming.octopus.reader.SheetReader;
import cn.chenhuanming.octopus.writer.DefaultExcelWriter;
import cn.chenhuanming.octopus.writer.DefaultSheetWriter;
import cn.chenhuanming.octopus.writer.ExcelWriter;
import cn.chenhuanming.octopus.writer.SheetWriter;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

/**
 * Excel operate facade.
 *
 * @author chenhuanming
 * Created at 2018/12/20
 * @see SheetReader
 * @see ExcelWriter
 * @see SheetWriter
 * @see Config
 * @see XmlConfigFactory
 * @see AnnotationConfigFactory
 */
public final class Octopus {
    /**
     * Create excel workbook and write a sheet data.
     *
     * @param os        write out this workbook to this output stream
     * @param config    get config through @{{@link ConfigFactory}}
     * @param sheetName name of sheet
     * @param data      data
     * @param <T>       class type of data
     * @throws IOException when writing excel file failed
     */
    public static <T> void writeOneSheet(OutputStream os, Config config, String sheetName, Collection<T> data) throws IOException {
        ExcelWriter writer = new DefaultExcelWriter(new SXSSFWorkbook(), os);
        writer.write(sheetName, new DefaultSheetWriter<T>(config), data);
        writer.close();
    }

    /**
     * Open excel workbook and read data from the first sheet.
     */
    public static <T> SheetReader<T> readFirstSheet(InputStream is, Config config, CellPosition startPosition) throws IOException, InvalidFormatException, EncryptedDocumentException {
        return readOneSheet(is, 0, config, startPosition);
    }

    /**
     * Open excel workbook and read data from the special index sheet.
     *
     * @param is            excel file
     * @param index         position,starting from 0
     * @param config        get config through @{{@link ConfigFactory}}
     * @param startPosition where to start read,starts from 0
     * @param <T>           class type of data
     * @return sheet reader
     * @throws IOException                if an error occurs while reading the data
     * @throws InvalidFormatException     if the contents of the file cannot be parsed into a {@link Workbook}
     * @throws EncryptedDocumentException If the workbook is password protected
     */
    public static <T> SheetReader<T> readOneSheet(InputStream is, int index, Config config, CellPosition startPosition) throws IOException, InvalidFormatException, EncryptedDocumentException {
        Workbook workbook = WorkbookFactory.create(is);
        return new DefaultSheetReader<T>(workbook.getSheetAt(index), config, startPosition);
    }

    /**
     * Read the sheet data with the specified name.
     */
    public static <T> SheetReader<T> readBySheetName(InputStream is, String sheetName, Config config, CellPosition startPosition) throws IOException, InvalidFormatException, EncryptedDocumentException, SheetNotFoundException {
        Workbook workbook = WorkbookFactory.create(is);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet.getSheetName().equals(sheetName)) {
                return new DefaultSheetReader<>(sheet, config, startPosition);
            }
        }
        throw new SheetNotFoundException("not found:" + sheetName);
    }

    /**
     * Read and validate data from the special index sheet.
     */
    public static <T> SheetReader<CheckedData<T>> readOneSheetWithValidation(InputStream is, int index, Config config, CellPosition startPosition) throws IOException, InvalidFormatException, EncryptedDocumentException {
        Workbook workbook = WorkbookFactory.create(is);
        return new CheckedSheetReader<>(workbook.getSheetAt(index), config, startPosition);
    }

    public static <T> SheetReader<CheckedData<T>> readFirstSheetWithValidation(InputStream is, Config config, CellPosition startPosition) throws IOException, InvalidFormatException, EncryptedDocumentException {
        return readOneSheetWithValidation(is, 0, config, startPosition);
    }

    public static <T> SheetReader<CheckedData<T>> readBySheetNameWithValidation(InputStream is, String sheetName, Config config, CellPosition startPosition) throws IOException, InvalidFormatException, EncryptedDocumentException, SheetNotFoundException {
        Workbook workbook = WorkbookFactory.create(is);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet.getSheetName().equals(sheetName)) {
                return new CheckedSheetReader<>(sheet, config, startPosition);
            }
        }
        throw new SheetNotFoundException("not found:" + sheetName);
    }
}
