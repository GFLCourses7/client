package com.geeksforless.client.security.config;


import com.geeksforless.client.model.User;
import com.geeksforless.client.model.enums.Role;
import com.geeksforless.client.repository.UserRepository;
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

    private final String WORKER_NAME;
    private final String WORKER_PASSWORD;

    public ApplicationConfig(UserRepository userRepository,
                             @Value("${worker.name}") String workerName,
                             @Value("${worker.password}") String workerPassword) {
        this.userRepository = userRepository;
        WORKER_NAME = workerName;
        WORKER_PASSWORD = workerPassword;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner addWorkerIfNotExists() {
        System.out.println(WORKER_NAME + " " + WORKER_PASSWORD);
        return args -> userRepository.findWorker()
                .ifPresentOrElse(worker -> {
                        },
                        () -> {
                            logger.info("Adding worker to DB");
                            User newWorker = new User();
                            newWorker.setUserName(WORKER_NAME);
                            newWorker.setPassWord(passwordEncoder().encode(WORKER_PASSWORD));
                            newWorker.setRole(Role.WORKER);
                            userRepository.save(newWorker);
                        }
                );
    }
}
