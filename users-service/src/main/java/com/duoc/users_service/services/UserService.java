package com.duoc.users_service.services;

import com.duoc.users_service.models.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User findByNickname(String nickname);
    User findByEmail(String email);
    User save(User user);
    void deleteById(Long id);
    User updateById(Long id, User user);
}
