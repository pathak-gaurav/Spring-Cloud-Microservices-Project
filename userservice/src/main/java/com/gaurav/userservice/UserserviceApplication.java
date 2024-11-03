package com.gaurav.userservice;

import com.gaurav.userservice.entity.Role;
import com.gaurav.userservice.entity.User;
import com.gaurav.userservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class UserserviceApplication {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserserviceApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(UserserviceApplication.class, args);
    }

    @PostConstruct
    @Transactional
    public void loadData() {
        User[] users = {
                User.builder()
                        .username("gaurav")
                        .password(passwordEncoder.encode("gaurav"))
                        .email("gaurav@gmail.com")
                        .role(Role.USER)
                        .build(),

                User.builder()
                        .username("john")
                        .password(passwordEncoder.encode("john"))
                        .email("john@gmail.com")
                        .role(Role.USER)
                        .build(),

                User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .email("admin@gmail.com")
                        .role(Role.ADMIN)
                        .build(),

                User.builder()
                        .username("alice")
                        .password(passwordEncoder.encode("alice"))
                        .email("alice@gmail.com")
                        .role(Role.USER)
                        .build(),

                User.builder()
                        .username("bob")
                        .password(passwordEncoder.encode("bob"))
                        .email("bob@gmail.com")
                        .role(Role.USER)
                        .build()
        };

        Arrays.stream(users).forEach(userRepository::save);
    }
}
