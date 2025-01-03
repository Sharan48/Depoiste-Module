package com.scm2.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.scm2.dto.JwtAuthRespone;
import com.scm2.dto.LoginDto;
import com.scm2.dto.UserDto;
import com.scm2.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/signup", consumes = { "multipart/form-data" })
    public ResponseEntity<UserDto> addUser(
            @Valid @ModelAttribute UserDto usersDto, @Valid @RequestParam(value = "profilePic") MultipartFile file) {
        return new ResponseEntity<>(userService.addUser(usersDto, file), HttpStatus.CREATED);

    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDto> updateContact(@RequestBody UserDto userDto,
            @PathVariable(name = "userId") Integer userId) {
        UserDto userUpdated = userService.updateUserDto(userId, userDto);
        return new ResponseEntity<>(userUpdated, HttpStatus.ACCEPTED);
    }

    @PostMapping(value = { "login", "sigin" })
    public ResponseEntity<JwtAuthRespone> loginToApp(@RequestBody LoginDto loginDto) {
        String user = userService.loginToApp(loginDto);

        JwtAuthRespone jwtauthresponse = new JwtAuthRespone();
        jwtauthresponse.setAccssToken(user);
        return ResponseEntity.ok(jwtauthresponse);

    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "userId") Integer userId) {
        UserDto userDto = userService.getUserById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);

    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllContactDos() {
        List<UserDto> allCOntact = userService.getAllUserDto();
        return new ResponseEntity<>(allCOntact, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable(name = "userId") Integer userId) {
        String deleteUser = userService.deleteUserById(userId);
        return new ResponseEntity<>(deleteUser, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/onlyuser/{userId}")
    public ResponseEntity<String> deleteOnlyUser(@PathVariable(name = "userId") Integer userId) {
        String deleteUser = userService.deleteUserById(userId);
        return new ResponseEntity<>(deleteUser, HttpStatus.ACCEPTED);
    }

    @PostMapping("/email-veify")
    public ResponseEntity<String> emailVerification(@RequestParam(value = "token") String emailVerification) {
        String emailVerified = userService.emailVerification(emailVerification);
        return new ResponseEntity<>(emailVerified, HttpStatus.ACCEPTED);
    }

    @PostMapping("{id}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable(name = "id") Integer id,
            @RequestParam(value = "oldpassword") String oldPassword,
            @RequestParam(value = "newpassword") String newPassword) {

        try {
            String changePassword = userService.changePassword(id, oldPassword, newPassword);
            return new ResponseEntity<>(changePassword, HttpStatus.ACCEPTED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> generateRestToken(@RequestParam(value = "email") String email) {
        String restToken = userService.generateRestToken(email);
        return new ResponseEntity<>(restToken, HttpStatus.ACCEPTED);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam(value = "token") String token,
            @RequestParam(value = "newPassword") String newPassword) {

        String password = userService.resetPassword(token, newPassword);
        return new ResponseEntity<>(password, HttpStatus.ACCEPTED);
    }

}
