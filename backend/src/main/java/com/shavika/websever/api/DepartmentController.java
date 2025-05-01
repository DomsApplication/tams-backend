package com.shavika.websever.api;


import com.shavika.websever.api.webmodels.DepartmentRequest;
import com.shavika.websever.api.webmodels.ErrorResponse;
import com.shavika.websever.services.DepartmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/department")
public class DepartmentController {

    private DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<?> insertDepartment(@RequestBody DepartmentRequest request) {
        try {
            log.info("Department INSERT:::" + request);
            return new ResponseEntity<>(departmentService.insertDepartment(request), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder()
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getDepartmentList() {
        try {
            return new ResponseEntity<>(departmentService.getList(), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return error response
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder()
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{deptId}")
    public ResponseEntity<?> deleteDepartment(@PathVariable("deptId") String deptId) {
        try {
            return new ResponseEntity<>(departmentService.deleteDepartment(deptId), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return error response
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder()
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
