package com.mlv.dreamshop.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mlv.dreamshop.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   boolean existsByEmail(String email);

    User findByEmail(String email);
}
