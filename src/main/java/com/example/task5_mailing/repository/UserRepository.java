package com.example.task5_mailing.repository;

import com.example.task5_mailing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long > {
    Optional<User> findByUsername(String username);

    @Query("select u from users u where lower(u.username) like lower(concat('%', ?1,'%'))")
    List<User> searchUsers(String key);
}
