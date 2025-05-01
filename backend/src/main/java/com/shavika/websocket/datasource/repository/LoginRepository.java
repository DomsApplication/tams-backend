package com.shavika.websocket.datasource.repository;

import com.shavika.websocket.datasource.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository  extends JpaRepository<Login, String> {

    Optional<Login> findByUserId(String userId);

}
