package com.scm2.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {

    private List<T> data;
    private int page;
    private int size;
    private int totalPages;

}
