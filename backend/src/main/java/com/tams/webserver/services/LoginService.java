package com.tams.webserver.services;

import com.tams.webserver.api.webmodels.*;

import com.tams.webserver.config.ApplicationProperties;
import com.tams.webserver.datasource.entity.LoginEntity;
import com.tams.webserver.datasource.repository.LoginRepository;
import com.tams.webserver.utils.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Transactional
    private LoginResponse saveOrUpdate(LoginEntity loginEntity) throws  Exception {
        LoginEntity loginEntitySaved = loginRepository.save(loginEntity);
        return this.getLoginResponse(loginEntitySaved);
    }

    private LoginResponse getLoginResponse(LoginEntity loginEntity) throws  Exception {
        return LoginResponse.builder()
                .userId(loginEntity.getUserId())
                .firstName(loginEntity.getFirstName())
                .lastLogin(loginEntity.getLastName())
                .role(loginEntity.getRole())
                .isActive(loginEntity.getIsActive().toString())
                .lastLogin((null == loginEntity.getLastLogin()) ? null :
                        Utilities.formatLocalDateTimeToUTCString(loginEntity.getLastLogin(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z))
                .build();
    }

    public LoginResponse createLogin(LoginRequest loginRequest) throws  Exception {
        if(loginRequest.getUserId().isEmpty() || loginRequest.getPassword().isEmpty()
        || loginRequest.getRole().isEmpty() || loginRequest.getFirstName().isEmpty()) {
            throw new RuntimeException("userId/password/firstName/role should not be empty. ");
        }

        try {
            Utilities.validateEmail(loginRequest.getUserId().trim().toString());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UserID, User ID should be valid email address.");
        }

        Optional<LoginEntity> existingLogin = loginRepository.findByUserId(loginRequest.getUserId());
        if(existingLogin.isPresent()) {
            throw new RuntimeException("User: "+loginRequest.getUserId()+" is already exist.");
        }

        if(!Utilities.getListOfRoles().contains(loginRequest.getRole())) {
            throw new RuntimeException("User Role should be any one of these '"+Utilities.USER_ROLES+"'");
        }

        LoginEntity loginEntity = LoginEntity.builder()
                .userId(loginRequest.getUserId())
                .password(Utilities.encryptPassword(loginRequest.getPassword()))
                .firstName(loginRequest.getFirstName())
                .lastName(loginRequest.getLastName())
                .role(loginRequest.getRole())
                .isActive(true).build();
        return this.saveOrUpdate(loginEntity);
    }

    public LoginResponse updateLogin(LoginRequest loginRequest) throws  Exception {
        if(loginRequest.getUserId().isEmpty() || loginRequest.getRole().isEmpty()
                || loginRequest.getFirstName().isEmpty()) {
            throw new RuntimeException("userId/firstName/role should not be empty.");
        }

        Optional<LoginEntity> existingLogin = loginRepository.findByUserId(loginRequest.getUserId());
        if(existingLogin.isEmpty()) {
            throw new RuntimeException("User: "+loginRequest.getUserId()+" is not exist.");
        }
        LoginEntity loginEntity = existingLogin.get();
        if(!loginEntity.getIsActive()) {
            throw new RuntimeException("User: "+loginRequest.getUserId()+" is deactivated.");
        }
        loginEntity.setFirstName(loginRequest.getFirstName());
        loginEntity.setLastName(loginRequest.getLastName());
        loginEntity.setRole(loginRequest.getRole());
        return this.saveOrUpdate(loginEntity);
    }

    public LoginResponse activateLogin(String userId) throws  Exception {
        Optional<LoginEntity> existingLogin = loginRepository.findByUserId(userId);
        if(existingLogin.isEmpty()) {
            throw new RuntimeException("User: "+userId+" is not exist.");
        }
        LoginEntity loginEntity = existingLogin.get();
        if(loginEntity.getIsActive()) {
            throw new RuntimeException("User: "+userId+" is deactivated.");
        }
        loginEntity.setIsActive(true);
        return this.saveOrUpdate(loginEntity);
    }

    public LoginResponse deactivateLogin(String userId) throws  Exception {
        Optional<LoginEntity> existingLogin = loginRepository.findByUserId(userId);
        if(existingLogin.isEmpty()) {
            throw new RuntimeException("User: "+userId+" is not exist.");
        }
        LoginEntity loginEntity = existingLogin.get();
        if(!loginEntity.getIsActive()) {
            throw new RuntimeException("User: "+userId+" is already deactivated.");
        }
        loginEntity.setIsActive(false);
        return this.saveOrUpdate(loginEntity);
    }

    public LoginResponse updateLastLogin(String userId) throws  Exception {
        Optional<LoginEntity> existingLogin = loginRepository.findByUserId(userId);
        if(existingLogin.isEmpty()) {
            throw new RuntimeException("User: "+userId+" is not exist.");
        }
        LoginEntity loginEntity = existingLogin.get();
        if(!loginEntity.getIsActive()) {
            throw new RuntimeException("User: "+userId+" is deactivated.");
        }
        if(!loginEntity.getIsActive()) {
            throw new RuntimeException("User: "+userId+" is already deactivated.");
        }
        loginEntity.setLastLogin(Utilities.getCurrentDateTime());
        return this.saveOrUpdate(loginEntity);
    }

    public LoginResponse getLoginById(String userId) throws  Exception {
        Optional<LoginEntity> existingLogin = loginRepository.findByUserId(userId);
        if(existingLogin.isEmpty()) {
            throw new RuntimeException("User: "+userId+" is not exist.");
        }
        if(!existingLogin.get().getIsActive()) {
            throw new RuntimeException("User: "+userId+" is deactivated.");
        }
        return this.getLoginResponse(existingLogin.get());
    }


    @Transactional
    public String deleteLogin(String userId) throws  Exception {
        Optional<LoginEntity> existingLogin = loginRepository.findByUserId(userId);
        if(existingLogin.isEmpty()) {
            throw new RuntimeException("User: "+userId+" is not exist.");
        }
        if(!existingLogin.get().getIsActive()) {
            throw new RuntimeException("User: "+userId+" is deactivated.");
        }
        loginRepository.delete(existingLogin.get());
        return "User: "+userId+" is deleted successfully.";
    }

    @Transactional
    public TokenResponse authenticate(HttpServletRequest request,
                                      HttpServletResponse response, LoginBasicAuth loginBasicAuth) throws Exception {
        if(null == loginBasicAuth || null == loginBasicAuth.getUserId() || null == loginBasicAuth.getPassword()) {
            throw new RuntimeException("userId/password should not be empty.");
        }

        String userId= loginBasicAuth.getUserId().trim();
        String password= loginBasicAuth.getPassword().trim();
        Optional<LoginEntity> existingLogin = loginRepository.findByUserId(loginBasicAuth.getUserId().trim());
        if(existingLogin.isEmpty()) {
            throw new RuntimeException("User: "+userId+" is not exist.");
        }
        LoginEntity loginEntity = existingLogin.get();
        if(!loginEntity.getIsActive()) {
            throw new RuntimeException("User: "+userId+" is deactivated.");
        }
        if(!password.equals(Utilities.decryptPassword(loginEntity.getPassword()))) {
            throw new RuntimeException("Invalid credentials.");
        }

        if(!loginEntity.getIsActive()) {
            throw new RuntimeException("User: "+userId+" is already deactivated.");
        }
        LoginResponse loginResponse = this.getLoginResponse(loginEntity);
        String token = Utilities.generateToken(loginResponse, applicationProperties.getTokenExpiryMinutes());
        Utilities.setCookieSession(request, response, token, applicationProperties.getTokenExpiryMinutes());
        loginEntity.setLastLogin(Utilities.getCurrentDateTime());
        this.saveOrUpdate(loginEntity);
        return TokenResponse.builder()
                .message("success")
                .userName(loginBasicAuth.getUserId())
                .token(token)
                .build();
    }

    public List<RoleResponse> getAllRoles() throws  Exception {
        List<RoleResponse> roleResponses = Utilities.getListOfRoles().stream()
                .map(roleName -> RoleResponse.builder().name(roleName).build())
                .collect(Collectors.toList());
        return roleResponses;
    }

}