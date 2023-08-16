package com.example.demo.user.repository;

import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    @Query("select u from User u join fetch u.userRole.role where u.email=:email")
    Optional<User> findUser(@Param("email") String email);
    Optional<User> findByNickname(String nickname);
}
