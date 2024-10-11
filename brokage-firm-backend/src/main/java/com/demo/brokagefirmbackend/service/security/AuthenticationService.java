package com.demo.brokagefirmbackend.service.security;

import com.demo.brokagefirmbackend.entity.Customer;
import com.demo.brokagefirmbackend.model.enums.Role;
import com.demo.brokagefirmbackend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;

    public void registerCustomer(Customer customer) {
        if (customerRepository.findByUsername(customer.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole(Role.CUSTOMER);
        customerRepository.save(customer);
    }

    public String createAuthenticationToken(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtService.generateToken(userDetails);
    }
}