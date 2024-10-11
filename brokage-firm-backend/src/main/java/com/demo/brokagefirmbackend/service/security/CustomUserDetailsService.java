package com.demo.brokagefirmbackend.service.security;

import com.demo.brokagefirmbackend.entity.Customer;
import com.demo.brokagefirmbackend.model.enums.Role;
import com.demo.brokagefirmbackend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals(adminUsername)) {
            return User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .roles(Role.ADMIN.name())
                    .build();
        }

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(customer.getUsername())
                .password(customer.getPassword())
                .roles(customer.getRole().name())
                .build();
    }

}
