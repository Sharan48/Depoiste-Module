package com.scm2.servicesimp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scm2.dto.UserDto;
import com.scm2.service.ExcelExportService;
import com.scm2.service.UserService;

@Service
public class ExcelExportServiceImp implements ExcelExportService {

    @Autowired
    private UserService userService;

    @Override
    public byte[] exportContactsToExcel(List<UserDto> user) {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheetName = workbook.createSheet("User");

        // Create header name
        String[] header = { "Id", "username", "email", "phoneNumber" };

        // create header row
        Row headerRow = sheetName.createRow(0);
        for (int i = 0; i < header.length; i++) {
            headerRow.createCell(i).setCellValue(header[i]);
            headerRow.getCell(i).setCellStyle(getHeaderCellStyle(workbook));
        }

        // create data rows
        int rowIndex = 1;
        for (UserDto u : user) {
            Row row = sheetName.createRow(rowIndex++);
            row.createCell(0).setCellValue(u.getId());
            row.createCell(1).setCellValue(u.getUsername());
            row.createCell(2).setCellValue(u.getEmail());
            row.createCell(3).setCellValue(u.getPhonenumber());
        }

        // autoincrement column
        for (int i = 0; i < header.length; i++) {
            sheetName.autoSizeColumn(i);

        }

        // write to byte array
        ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
        try {
            workbook.write(outputstream);
            workbook.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

        return outputstream.toByteArray();
    }

    @Override
    public CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    @Override
    public byte[] getRealData() {
        List<UserDto> user = userService.getAllUserDto();
        byte[] data = exportContactsToExcel(user);
        return data;
    }

    @Override
    public String exportAndSendEmail(String filePath) {
        List<UserDto> user = userService.getAllUserDto();
        Workbook workbook = new XSSFWorkbook();

        Sheet sheetName = workbook.createSheet("User");

        // Create header name
        String[] header = { "Id", "username", "email", "phoneNumber" };

        // create header row
        Row headerRow = sheetName.createRow(0);
        for (int i = 0; i < header.length; i++) {
            headerRow.createCell(i).setCellValue(header[i]);
            headerRow.getCell(i).setCellStyle(getHeaderCellStyle(workbook));
        }

        // create data rows
        int rowIndex = 1;
        for (UserDto u : user) {
            Row row = sheetName.createRow(rowIndex++);
            row.createCell(0).setCellValue(u.getId());
            row.createCell(1).setCellValue(u.getUsername());
            row.createCell(2).setCellValue(u.getEmail());
            row.createCell(3).setCellValue(u.getPhonenumber());
        }

        // autoincrement column
        for (int i = 0; i < header.length; i++) {
            sheetName.autoSizeColumn(i);

        }

        try (FileOutputStream fileOutput = new FileOutputStream(filePath)) {
            workbook.write(fileOutput);
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;

    }

}
