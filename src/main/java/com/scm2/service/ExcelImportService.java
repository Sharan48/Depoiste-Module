package com.scm2.service;

import java.io.IOException;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelImportService {
    String parseExcelFile(MultipartFile file, String filePath) throws IOException;

    boolean isRowValid(Row row, int rowIndex);

}
