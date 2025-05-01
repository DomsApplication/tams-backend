package com.shavika.websocket.datasource.repository;

import com.shavika.websocket.datasource.entity.Department;
import com.shavika.websocket.datasource.entity.UserInfoExt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository  extends JpaRepository<Department, String> {

    Optional<Department> findByDepartmentId(String departmentId);
}
