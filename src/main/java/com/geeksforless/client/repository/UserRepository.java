package com.geeksforless.client.repository;

import com.geeksforless.client.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    @Query("SELECT u FROM User u WHERE u.role = 'WORKER'")
    Optional<User> findWorker();
}
