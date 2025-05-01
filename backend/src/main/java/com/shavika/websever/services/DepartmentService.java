package com.shavika.websever.services;

import com.shavika.websever.api.webmodels.DepartmentRequest;
import com.shavika.websever.api.webmodels.DepartmentResponse;
import com.shavika.websocket.datasource.entity.Department;
import com.shavika.websocket.datasource.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class DepartmentService {

    private DepartmentRepository departmentRepository;

    public DepartmentResponse insertDepartment(DepartmentRequest request) throws Exception {
        Department department = Department.builder()
                .departmentId(request.getDepartmentId())
                .departmentName(request.getDepartmentName())
                .build();
        log.info("Department INSERT/1:::" + department);
        Department department1 = departmentRepository.save(department);
        log.info("Department INSERT/2:::" + department1);
        DepartmentResponse response = DepartmentResponse.builder()
                .departmentId(department1.getDepartmentId())
                .departmentName(department1.getDepartmentName())
                .build();
        log.info("Department INSERT/3:::" + response);
        return response;
    }

    public List<DepartmentResponse> getList() throws Exception {
        return departmentRepository.findAll().stream()
                .map(department -> DepartmentResponse.builder()
                        .departmentId(department.getDepartmentId())
                        .departmentName(department.getDepartmentName())
                        .build())
                .collect(Collectors.toList());
    }

    public DepartmentResponse deleteDepartment(String deptId) throws  Exception {
        Optional<Department> department = departmentRepository.findByDepartmentId(deptId);
        if (department.isPresent()){
            Department dept = department.get();
            departmentRepository.delete(dept);
            return DepartmentResponse.builder()
                    .departmentId(dept.getDepartmentId())
                    .departmentName(dept.getDepartmentName())
                    .build();
        }
        return null;
    }

}
