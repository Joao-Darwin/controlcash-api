package com.controlcash.app.repositories;

import com.controlcash.app.models.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<Goal, UUID> {
}
