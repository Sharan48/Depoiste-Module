package com.scm2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ContactDto {
    private Integer id;
    private String name;
    private String phonenumber;
    private String address;
    private String picture;
    private String description;
    private boolean favorite = false;
    private String wesiteLink;
    private String linkedInLink;

}
