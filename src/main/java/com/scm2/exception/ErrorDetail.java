package com.scm2.exception;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorDetail {
    private Date date;
    private String message;
    private String details;

    public ErrorDetail(String message) {
        this.message = message;
    }

    public ErrorDetail(Date date, String message, String details) {
        this.date = date;
        this.message = message;
        this.details = details;
    }

}
