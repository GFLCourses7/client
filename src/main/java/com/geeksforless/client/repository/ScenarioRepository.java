package com.geeksforless.client.repository;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.projections.ScenarioInfo;
import com.geeksforless.client.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    List<Scenario> findByUser(User user);

//    doesn't work
    @Query("SELECT s.id AS id, s.name AS name, s.site AS site, s.result AS result, s.steps AS steps FROM Scenario s WHERE s.id = :scenarioId")
    ScenarioInfo findScenarioInfoById(@Param("scenarioId") Long scenarioId);
}
