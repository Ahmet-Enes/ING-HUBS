package com.ing.hubs.controller;

import com.ing.hubs.model.dto.AuthenticationRequestDto;
import com.ing.hubs.model.entity.Customer;
import com.ing.hubs.repository.CustomerRepository;
import com.ing.hubs.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final String ROLE_USER = "ROLE_USER";

    @PostMapping("/login")
    public String createAuthenticationToken(@RequestBody AuthenticationRequestDto authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    @PostMapping("/register")
    public Customer register(@RequestBody AuthenticationRequestDto dto) {
        var opt = customerRepository.findByUsername(dto.getUsername());
        if (opt.isPresent()) {
            throw new RuntimeException("User already exists!");
        }
        var customer = new Customer();
        customer.setUsername(dto.getUsername());
        customer.setPassword(passwordEncoder.encode(dto.getPassword()));
        customer.setRole(ROLE_USER);

        return customerRepository.save(customer);
    }
}
