package com.geeksforless.client.security.config;

import com.geeksforless.client.model.Role;
import com.geeksforless.client.model.User;
import com.geeksforless.client.repository.UserRepository;
import com.geeksforless.client.security.JwtService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    private static final Logger logger = LogManager.getLogger(ApplicationConfig.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final String workerUsername;
    private final String workerPassword;

    public ApplicationConfig(UserRepository userRepository,
                             JwtService jwtService,
                             @Value("${worker.username}") String workerUsername,
                             @Value("${worker.password}") String workerPassword) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.workerUsername = workerUsername;
        this.workerPassword = workerPassword;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CommandLineRunner addWorkerIfNotExists() {
        return args -> userRepository.findWorker().ifPresentOrElse(
                this::printWorkerToken,
                () -> {
                    logger.info("Adding worker to DB");
                    User newWorker = new User();
                    newWorker.setUsername(workerUsername);
                    newWorker.setPassword(passwordEncoder().encode(workerPassword));
                    newWorker.setRole(Role.WORKER);
                    userRepository.save(newWorker);
                    printWorkerToken(newWorker);
                }
        );
    }

    private void printWorkerToken(User worker) {
        logger.info("{} Token ---- {}", worker.getUsername(), jwtService.generateTokenForWorker(worker));
    }
}
