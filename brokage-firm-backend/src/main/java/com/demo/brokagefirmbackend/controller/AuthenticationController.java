package com.demo.brokagefirmbackend.controller;

import com.demo.brokagefirmbackend.entity.Customer;
import com.demo.brokagefirmbackend.model.request.AuthenticationRequest;
import com.demo.brokagefirmbackend.model.response.AuthenticationResponse;
import com.demo.brokagefirmbackend.service.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerCustomer(@RequestBody Customer customer) {
        authenticationService.registerCustomer(customer);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authRequest) {
        String jwt = authenticationService.createAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}