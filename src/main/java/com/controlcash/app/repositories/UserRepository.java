package com.controlcash.app.repositories;

import com.controlcash.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "SELECT u FROM User u WHERE u.userName =:userName")
    User findByUsername(@Param("userName") String userName);
}
