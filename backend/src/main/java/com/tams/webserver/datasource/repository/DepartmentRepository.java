package com.tams.webserver.datasource.repository;

import com.tams.webserver.datasource.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository  extends JpaRepository<Department, String> {

    Optional<Department> findByDepartmentId(String departmentId);
}
