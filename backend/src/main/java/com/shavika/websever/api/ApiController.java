package com.shavika.websever.api;

import com.shavika.websever.api.webmodels.HealthCheck;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api")
public class ApiController {

    @GetMapping("/health")
    public ResponseEntity<HealthCheck> healthCheck() {
        HealthCheck response = HealthCheck.builder().service_status("Up").build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
