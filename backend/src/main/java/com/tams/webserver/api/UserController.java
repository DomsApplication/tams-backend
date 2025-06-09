package com.tams.webserver.api;

import com.tams.webserver.api.webmodels.ErrorResponse;
import com.tams.webserver.api.webmodels.UserDataSyncRequest;
import com.tams.webserver.api.webmodels.UserDataRequest;
import com.tams.webserver.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getuserList() {
        try {
            return new ResponseEntity<>(userService.getUserList(), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return error response
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getuser(@PathVariable("userId") String userId) {
        try {
            return new ResponseEntity<>(userService.getUserByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return error response
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}/photo")
    public ResponseEntity<?> getuserPhoto(@PathVariable("userId") String userId) {
        try {
            return new ResponseEntity<>(userService.getUserPhotoByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return error response
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}/finger")
    public ResponseEntity<?> getuserFinger(@PathVariable("userId") String userId) {
        try {
            return new ResponseEntity<>(userService.getUserFingerByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return error response
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateuserflag")
    public ResponseEntity<?> updateuserFlags(@RequestBody UserDataSyncRequest userDataSyncRequest) {
        try {
            return new ResponseEntity<>(userService.updateUserCommandFlags(userDataSyncRequest), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return error response
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDataRequest userDataRequest) {
        // Process the userRequest object
        System.out.println("User Request: " + userDataRequest);
        try {
            return new ResponseEntity<>(userService.syncUserData(userDataRequest), HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle exceptions and return error response
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteDepartment(@PathVariable("userId") String userId,
                                              @RequestBody Map<String, Object> requestBody) {
        try {
            boolean deleteRequiredInDevices = (boolean) requestBody.getOrDefault("deleteFromDevices", false);
            return new ResponseEntity<>(userService.deleteUserInfoData(userId, deleteRequiredInDevices), HttpStatus.OK);
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
