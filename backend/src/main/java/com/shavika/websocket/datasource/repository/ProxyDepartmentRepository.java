package com.shavika.websocket.datasource.repository;

import com.shavika.websocket.datasource.entity.ProxyDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProxyDepartmentRepository  extends JpaRepository<ProxyDepartment, String> {

    Optional<ProxyDepartment> findByProxyId(String proxyId);
}
