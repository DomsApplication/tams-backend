package com.tams.usermanagement.datasource.repository;

import com.tams.usermanagement.datasource.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository  extends JpaRepository<Login, String> {

    Optional<Login> findByUserId(String userId);

}
