package com.scm2.dto;

import org.springframework.web.multipart.MultipartFile;

import com.scm2.enums.Provider;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {

    private Integer id;
    private String username;
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email should be valid")
    private String email;
    @Pattern(regexp = "^\\d{10}$", message = "PhoneNumber should be valid")
    private String phonenumber;
    private String password;
    private String about;
    private String profilePicUrl;
    private String emailToken;

    @NotNull(message = "Profile picture is required")
    private MultipartFile profilePic;

    // information
    private boolean enabled = false;
    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    // provider
    private Provider provider = Provider.SELF;
    private String providerUserId;

}