package com.shavika.websocket.datasource.repository;

import com.shavika.websocket.datasource.entity.UserInfo;
import com.shavika.websocket.datasource.entity.UserInfoExt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoExtRepository  extends JpaRepository<UserInfoExt, String> {

    Optional<UserInfoExt> findByUserId(String userId);
}

