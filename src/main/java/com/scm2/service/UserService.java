package com.scm2.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.scm2.dto.LoginDto;
import com.scm2.dto.UserDto;

public interface UserService {
    UserDto addUser(UserDto usersDto, MultipartFile file);

    UserDto updateUserDto(Integer id, UserDto userDto);

    List<UserDto> getAllUserDto();

    UserDto getUserById(Integer userId);

    String deleteUserById(Integer userId);

    String deleteOnlyUser(Integer userId);

    String loginToApp(LoginDto loginDto);

    String emailVerification(String emailToken);

    String changePassword(Integer id, String oldPassword, String newPassword);

    String generateRestToken(String email);

    String resetPassword(String token, String newPassword);

}
