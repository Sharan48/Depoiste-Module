package com.scm2.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scm2.service.EmailService;
import com.scm2.service.ExcelExportService;

@RestController
@RequestMapping("/user/report")
public class ExcelExportController {

    @Autowired
    private ExcelExportService getReportService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/excel")
    public void exportExcelReport(HttpServletResponse response) throws IOException {
        byte[] data = getReportService.getRealData();

        // set response headers
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=User.xlsx");

        // write excel data to file
        response.getOutputStream().write(data);
        response.getOutputStream().flush();
    }

    @PostMapping("/sendtoemail")
    public ResponseEntity<String> exportReportToEmail(@RequestParam(value = "emails") String email) throws IOException {
        String filePath = "src/main/resources/Reports/exported_data.xlsx";

        getReportService.exportAndSendEmail(filePath);

        emailService.sendEmailWithAttachment(email, "Sending report", "Sending report from internally generated",
                filePath);

        return new ResponseEntity<>("Email Send successfully", HttpStatus.ACCEPTED);
    }

}
