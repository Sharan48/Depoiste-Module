package com.scm2.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import com.scm2.exception.BlogException;

public enum DepositeStatus {
    PENDING, APPROVED, REJECTED;

    public static DepositeStatus fromValue(String value) {
        for (DepositeStatus status : DepositeStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new BlogException("Invalid status: " + value + ". Allowed values are PENDING, APPROVED, REJECTED.",
                HttpStatus.BAD_REQUEST);
    }
}
