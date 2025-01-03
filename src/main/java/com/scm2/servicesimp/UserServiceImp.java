package com.scm2.servicesimp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.scm2.dto.LoginDto;
import com.scm2.dto.UserDto;
import com.scm2.entity.Contact;
import com.scm2.entity.Role;
import com.scm2.entity.User;
import com.scm2.exception.BlogException;
import com.scm2.exception.ResourceNotFoundException;
import com.scm2.repositaries.ContactRepository;
import com.scm2.repositaries.RoleRepository;
import com.scm2.repositaries.UserRepository;
import com.scm2.security.JwtTokenProvider;
import com.scm2.service.EmailService;
import com.scm2.service.ImageService;
import com.scm2.service.UserService;
import com.scm2.utils.Helper;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private EmailService emailService;

    private UserRepository userRepository;
    private ContactRepository contactRepository;
    private ModelMapper modelMapper;

    public UserServiceImp(UserRepository userRepository, ModelMapper modelMapper, ContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto addUser(UserDto userDto, MultipartFile file) {
        if (userRepository.existsByphonenumber(userDto.getPhonenumber())) {
            throw new ResourceNotFoundException("Phone number already exists");
        }
        // User user = modelMapper.map(userDto, User.class);
        // return modelMapper.map(userRepository.save(user), UserDto.class);

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new BlogException("Email is already exist!.",
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new BlogException("Username is already exist!.", HttpStatus.BAD_REQUEST);
        }

        String fileurl = null;
        String validatefile = file.getOriginalFilename();

        if (validatefile == null || !validatefile.matches(".*\\.(jpg|jpeg|png)$")) {
            throw new BlogException("Invalid file format. Only jpg,jpeg, and png are allowed.", HttpStatus.BAD_REQUEST);
        }

        if (file != null && !file.isEmpty()) {
            fileurl = imageService.uploadImage(file);
            userDto.setProfilePicUrl(fileurl);
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhonenumber(userDto.getPhonenumber());
        user.setPhoneVerified(userDto.isPhoneVerified());
        user.setEmailVerified(userDto.isEmailVerified());
        user.setProviderUserId(userDto.getProviderUserId());
        user.setProvider(userDto.getProvider());
        user.setAbout(userDto.getAbout());
        user.setProfilePicUrl(fileurl);

        Set<Role> role = new HashSet<>();
        Role roles = roleRepository.findByRoleName("ROLE_USER").get();
        role.add(roles);
        user.setRoles(role);

        // Email verification
        String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
        User userSaved = userRepository.save(user);

        String emailLink = Helper.getLinkForEmailVerification(emailToken);

        emailService.sendMail(userSaved.getEmail(), "Verify account: Email Contact Manager", emailLink);

        return modelMapper.map(userSaved, UserDto.class);

    }

    @Override
    public UserDto updateUserDto(Integer id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));

        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }

        if (userDto.getPassword() != null) {
            user.setPassword(userDto.getPassword());
        }

        if (userDto.getPhonenumber() != null) {
            user.setPhonenumber(userDto.getPhonenumber());
        }

        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public List<UserDto> getAllUserDto() {
        List<User> allUser = userRepository.findAll();
        List<UserDto> dtoUser = allUser.stream().map(contact -> modelMapper.map(contact, UserDto.class))
                .collect(Collectors.toList());
        return dtoUser;
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public String deleteUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
        userRepository.delete(user);
        return "User deleted successfully";
    }

    @Override
    public String deleteOnlyUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));

        for (Contact contact : user.getContacts()) {
            // user.removeContact(contact);
            contact.setUser(null); // set user to null to remove the relationship with the user.
            contactRepository.save(contact);
        }
        userRepository.delete(user);

        return "User Deleted successfully";
    }

    @Override
    public String loginToApp(LoginDto loginDto) {

        try {
            Authentication authetication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),
                            loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authetication);
            String token = jwtTokenProvider.generateToken(authetication);
            return token;

        } catch (UsernameNotFoundException ex) {
            return "Login failed: Username or email not found.";

        } catch (Exception ex) {
            return "Login failed: Invalid username or password.";
        }

    }

    @Override
    public String emailVerification(String emailToken) {

        User user = userRepository.findByEmailToken(emailToken).orElse(null);

        if (user != null) {
            if (user.getEmailToken().equals(emailToken)) {
                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepository.save(user);

                return "Email verified successfully!. Now you can login";
            }
        }
        return "Email verification failed!";

    }

    @Override
    public String changePassword(Integer id, String oldPassword, String newPassword) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            // System.out.println(user.getPassword());
            return "Old password is incorrect";
        } else {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return "Password changed successfully";
        }

    }

    @Override
    public String generateRestToken(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BlogException("User with email not found: " + email, HttpStatus.BAD_REQUEST));
        String token = UUID.randomUUID().toString();

        user.setEmailToken(token);
        userRepository.save(user);
        emailService.sendMail(email, "Reset Password", "Reset passwork Token : " + token);
        return "Reset token sent to your email";

    }

    @Override
    public String resetPassword(String token, String newPassword) {
        User user = userRepository.findByEmailToken(token).orElseThrow(null);

        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setEmailToken(null);
            userRepository.save(user);
            return "Password reset successfully";
        }

        return "Password reset failed";
    }

}
