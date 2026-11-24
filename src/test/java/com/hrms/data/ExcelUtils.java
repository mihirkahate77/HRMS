package com.hrms.data;

import java.io.InputStream;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

// Reads test data from src/test/resources/testdata.xlsx (not a machine-specific path).
public class ExcelUtils {
    public static String[][] getSheetData(String sheetName) {
        try (InputStream input = ExcelUtils.class.getClassLoader().getResourceAsStream("testdata.xlsx")) {
            if (input == null) throw new RuntimeException("Add testdata.xlsx to src/test/resources");
            try (Workbook workbook = WorkbookFactory.create(input)) {
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) throw new RuntimeException("Sheet not found: " + sheetName);
                int rows = sheet.getPhysicalNumberOfRows();
                int columns = sheet.getRow(0).getLastCellNum();
                String[][] data = new String[rows - 1][columns];
                DataFormatter formatter = new DataFormatter();
                for (int r = 1; r < rows; r++) {
                    Row row = sheet.getRow(r);
                    for (int c = 0; c < columns; c++) {
                        data[r - 1][c] = row == null || row.getCell(c) == null ? "" : formatter.formatCellValue(row.getCell(c));
                    }
                }
                return data;
            }
        } catch (Exception e) { throw new RuntimeException("Unable to read Excel test data", e); }
    }
}
