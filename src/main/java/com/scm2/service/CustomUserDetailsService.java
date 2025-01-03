package com.scm2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scm2.entity.User;
import com.scm2.repositaries.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String usernameoremail) throws UsernameNotFoundException {
                User user = userRepository.findByUsernameOrEmail(usernameoremail, usernameoremail).orElseThrow(
                                () -> new UsernameNotFoundException(
                                                "User not found with username or email: " + usernameoremail));

                // if (!user.isEmailVerified()) {
                // throw new IllegalStateException("User email not verified" +
                // usernameoremail);

                // }

                List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                                .map((role) -> new SimpleGrantedAuthority(role.getRoleName()))
                                .collect(Collectors.toList());
                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                                authorities);
        }

}
