package com.mlv.dreamshop.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mlv.dreamshop.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existByEmail(String email);
}
