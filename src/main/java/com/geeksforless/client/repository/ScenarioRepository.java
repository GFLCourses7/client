package com.geeksforless.client.repository;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.projections.ScenarioInfo;
import com.geeksforless.client.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    List<ScenarioInfo> findByUser(User user);
}
