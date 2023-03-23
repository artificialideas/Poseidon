package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    Optional<User> findById(Integer id);

    User findByUsername(String username);

    void save(User user);

    void delete(User user);
}