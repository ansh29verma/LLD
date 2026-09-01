package com.ansh.repo;

import com.ansh.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(String userId);
    List<User> findAll();
}
