package com.ing.hubs.service;

import com.ing.hubs.model.dto.AuthenticationRequestDto;
import com.ing.hubs.model.entity.Customer;
import com.ing.hubs.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {

    private final CustomerRepository repository;
    private final String ROLE_ADMIN = "ROLE_ADMIN";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var customer = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(customer.getUsername(), customer.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(customer.getRole())));
    }

    private Boolean isUserAdmin(User user) {
        if (CollectionUtils.isEmpty(user.getAuthorities())) {
            return false;
        }
        return user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()).contains(ROLE_ADMIN);
    }

    public void authorizeCustomer(String customerName) {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = repository.findByUsername(customerName)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        if (!isUserAdmin(user) && !customer.getUsername().equals(user.getUsername())) {
            throw new RuntimeException("Unauthorized access");
        }
    }
}
