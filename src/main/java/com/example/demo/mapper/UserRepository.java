package com.example.demo.mapper;

import com.example.demo.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
    //查詢 select id,username,password from users where username = ?
    Optional<UserModel> findByUsername(String username);
}