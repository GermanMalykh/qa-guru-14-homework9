import com.codeborne.pdftest.PDF;

import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.List;
import java.util.zip.ZipFile;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class ZipFilesParserTest {

    ClassLoader classLoader = ZipFilesParserTest.class.getClassLoader();

    static final String zipFileName = "files.zip";

    @DisplayName("Checking PDF File Values from Zip")
    @Test
    void zipFilePdfTest() throws Exception {

        String pdfFileName = "sample.pdf",
                pdfText = "This is a small demonstration .pdf file -";

        try (
                ZipFile zf = new ZipFile(classLoader.getResource(zipFileName).getPath());
                InputStream entryStream = zf.getInputStream(zf.getEntry(pdfFileName))
        ) {
            PDF pdf = new PDF(entryStream);
            assertThat(pdf.text).contains(pdfText);
        }
    }

    @DisplayName("Checking CSV File Values from Zip")
    @Test
    void checkCSVInZipTest() throws Exception {

        String csvFileName = "file_example_CSV_5000.csv";

        String[] csvColumnList = new String[]{"", "First Name", "Last Name",
                "Gender", "Country", "Age", "Date", "Id"},
                csvValueColumnList = new String[]{"32", "Stasia", "Becker", "Female",
                        "Great Britain", "34", "16/08/2016", "7521"};

        try (
                ZipFile zf = new ZipFile(classLoader.getResource(zipFileName).getPath());
                InputStream entryStream = zf.getInputStream(zf.getEntry(csvFileName));
                CSVReader csvReader = new CSVReader(new InputStreamReader(entryStream, UTF_8))
        ) {
            List<String[]> csv = csvReader.readAll();
            assertThat(csv).contains(
                    csvColumnList,
                    csvValueColumnList
            );
        }
    }

    @DisplayName("Checking XLS File Values from Zip")
    @Test
    void zipFileXlsTest() throws Exception {

        String xlsFileName = "file_example_XLS_50.xls",
                xlsText = "Prothro";

        int xlsSheet = 0,
                xlsRow = 25,
                xlsCell = 2;

        try (
                ZipFile zf = new ZipFile(classLoader.getResource(zipFileName).getPath());
                InputStream entryStream = zf.getInputStream(zf.getEntry(xlsFileName))
        ) {
            XLS xls = new XLS(entryStream);
            assertThat(xls.excel
                    .getSheetAt(xlsSheet)
                    .getRow(xlsRow)
                    .getCell(xlsCell)
                    .getStringCellValue()).isEqualTo(xlsText);
        }
    }

}