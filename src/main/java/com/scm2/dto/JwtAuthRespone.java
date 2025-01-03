package com.scm2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthRespone {
    private String accssToken;
    private String tokenType = "Bearer";
}
