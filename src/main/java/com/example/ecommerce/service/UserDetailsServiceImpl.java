package com.example.ecommerce.service;

import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User.UserBuilder;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user: {}", username);
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            User currentUser = user.get();
            logger.info("User found: {}", currentUser.getUsername());
            logger.info("User password from database: {}", currentUser.getPassword());
            logger.info("User role from database: {}", currentUser.getRole());

            UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(currentUser.getPassword()); // Remove the {bcrypt} prefix
            builder.roles(currentUser.getRole().replace("ROLE_","")); //Remove the ROLE_ prefix
            logger.info("User details built successfully.");
            return builder.build();
        } else {
            logger.warn("User not found: {}", username);
            throw new UsernameNotFoundException("User not found.");
        }
    }
}