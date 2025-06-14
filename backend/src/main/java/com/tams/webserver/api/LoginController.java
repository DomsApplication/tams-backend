package com.tams.webserver.api;

import com.tams.webserver.api.webmodels.ErrorResponse;
import com.tams.webserver.api.webmodels.LoginBasicAuth;
import com.tams.webserver.api.webmodels.LoginRequest;
import com.tams.webserver.api.webmodels.TokenResponse;
import com.tams.webserver.services.LoginService;
import com.tams.webserver.utils.Utilities;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/login")
public class LoginController {

    private LoginService loginService;

    @PostMapping("/create")
    public ResponseEntity<?> createLogin(@RequestBody LoginRequest loginRequest) {
        try {
            if(loginRequest == null) {
                throw new RuntimeException("Login details is empty, Try again.");
            }
            return new ResponseEntity<>(loginService.createLogin(loginRequest), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateLogin(@RequestBody LoginRequest loginRequest) {
        try {
            return new ResponseEntity<>(loginService.updateLogin(loginRequest), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getLoginById(@PathVariable String userId) {
        try {
            return new ResponseEntity<>(loginService.getLoginById(userId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/activate/{userId}")
    public ResponseEntity<?> activateUser(@PathVariable String userId) {
        try {
            return new ResponseEntity<>(loginService.activateLogin(userId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/deactivate/{userId}")
    public ResponseEntity<?> deactivateLoginUser(@PathVariable String userId) {
        try {
            return new ResponseEntity<>(loginService.deactivateLogin(userId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteLogin(@PathVariable String userId) {
        try {
            return new ResponseEntity<>(loginService.deleteLogin(userId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/token")
    public ResponseEntity<?> authenticate(@RequestBody LoginBasicAuth loginBasicAuth, HttpServletRequest request,
                                          HttpServletResponse response) {
        try {
            TokenResponse token = loginService.authenticate(request, response, loginBasicAuth);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse errorResponse = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ping")
    public ResponseEntity<?> pingForActive() {
        try {
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        try {
            return new ResponseEntity<>(loginService.getAllRoles(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
