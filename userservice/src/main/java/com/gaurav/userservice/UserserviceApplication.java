package com.gaurav.userservice;

import com.gaurav.userservice.entity.Role;
import com.gaurav.userservice.entity.User;
import com.gaurav.userservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        User gaurav = User.builder()
                .username("gaurav")
                .password(passwordEncoder.encode("gaurav"))
                .email("gaurav@gmail.com").role(Role.USER).build();
        userRepository.save(gaurav);
    }
}
