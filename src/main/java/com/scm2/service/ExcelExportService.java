package com.scm2.service;

import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.scm2.dto.UserDto;

public interface ExcelExportService {
    byte[] exportContactsToExcel(List<UserDto> user);

    CellStyle getHeaderCellStyle(Workbook workbook);

    byte[] getRealData();

    String exportAndSendEmail(String filePath);

}
