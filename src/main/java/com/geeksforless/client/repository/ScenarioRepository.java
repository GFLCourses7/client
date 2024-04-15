package com.geeksforless.client.repository;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
}
