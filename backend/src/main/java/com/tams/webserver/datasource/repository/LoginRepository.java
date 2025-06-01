package com.tams.webserver.datasource.repository;

import com.tams.webserver.datasource.entity.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository  extends JpaRepository<LoginEntity, String> {

    Optional<LoginEntity> findByUserId(String userId);

}
