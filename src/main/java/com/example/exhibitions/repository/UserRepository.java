package com.example.exhibitions.repository;

import com.example.exhibitions.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User getUserByUsername(String username);

    @Modifying
    @Transactional
    @Query("update User u set u.enabled = ?1 where u.id = ?2")
    void changeEnabledStatus(Boolean bool, Long id);
}
