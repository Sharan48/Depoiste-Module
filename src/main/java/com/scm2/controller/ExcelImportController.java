package com.scm2.controller;

import java.io.IOException;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.scm2.service.EmailService;
import com.scm2.service.ExcelImportService;

@RestController
@RequestMapping("/users/reports")
public class ExcelImportController {

    private ExcelImportService mExcelImportService;
    @Autowired
    private EmailService mEmailService;

    public ExcelImportController(ExcelImportService excelImportService) {
        this.mExcelImportService = excelImportService;
    }

    @PostMapping(value = "/import-file", consumes = { "multipart/form-data" })
    public ResponseEntity<String> importUser(@RequestParam(value = "importfile") MultipartFile file)
            throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Please upload a valid Excel file.");
        }

        try {
            String filepath = "src/main/resources/Reports/" + "ErrorImportFile.xlsx";
            String response = mExcelImportService.parseExcelFile(file, filepath);
            mEmailService.sendEmailWithAttachment("sharanu318@gmail.com", "ErrorImport file",
                    "The file containing the errors fileds with error messages", filepath);
            return ResponseEntity.ok("Successfully file imported!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error importing users: " + e.getMessage());
        }

    }
}
