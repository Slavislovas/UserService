package com.fitnessapp.userservice.business.repository;

import com.fitnessapp.userservice.business.repository.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, Long> {
    @Query("'username' : ?0")
    Optional<UserEntity> findByUsername(String username);
}
