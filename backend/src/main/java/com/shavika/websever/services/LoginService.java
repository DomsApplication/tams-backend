package com.shavika.websever.services;

import com.shavika.websever.api.webmodels.LoginBasicAuth;
import com.shavika.websever.api.webmodels.LoginRequest;
import com.shavika.websever.api.webmodels.LoginResponse;
import com.shavika.websever.api.webmodels.TokenResponse;
import com.shavika.websocket.datasource.entity.Login;
import com.shavika.websocket.datasource.repository.LoginRepository;
import com.shavika.websocket.utils.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Transactional
    private LoginResponse saveOrUpdate(Login login) throws  Exception {
        Login loginSaved = loginRepository.save(login);
        return this.getLoginResponse(loginSaved);
    }

    private LoginResponse getLoginResponse(Login login) throws  Exception {
        return LoginResponse.builder()
                .userId(login.getUserId())
                .firstName(login.getFirstName())
                .lastLogin(login.getLastName())
                .role(login.getRole())
                .isActive(login.getIsActive().toString())
                .lastLogin((null == login.getLastLogin()) ? null :
                        Utilities.formatLocalDateTimeToUTCString(login.getLastLogin(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z))
                .build();
    }

    public LoginResponse createLogin(LoginRequest loginRequest) throws  Exception {
        if(loginRequest.getUserId().isEmpty() || loginRequest.getPassword().isEmpty()
        || loginRequest.getRole().isEmpty() || loginRequest.getFirstName().isEmpty()) {
            throw new RuntimeException("userId/password/firstName/role should not be empty. ");
        }

        Optional<Login> existingLogin = loginRepository.findByUserId(loginRequest.getUserId());
        if(existingLogin.isPresent()) {
            throw new RuntimeException("User: "+loginRequest.getUserId()+" is already exist.");
        }
        Login login = Login.builder()
                .userId(loginRequest.getUserId())
                .password(Utilities.encryptPassword(loginRequest.getPassword()))
                .firstName(loginRequest.getFirstName())
                .lastName(loginRequest.getLastName())
                .role(loginRequest.getRole())
                .isActive(true)
                .build();
        return this.saveOrUpdate(login);
    }

    public LoginResponse updateLogin(LoginRequest loginRequest) throws  Exception {
        if(loginRequest.getUserId().isEmpty() || loginRequest.getRole().isEmpty()
                || loginRequest.getFirstName().isEmpty()) {
            throw new RuntimeException("userId/firstName/role should not be empty.");
        }

        Optional<Login> existingLogin = loginRepository.findByUserId(loginRequest.getUserId());
        if(existingLogin.isEmpty()) {
            throw new RuntimeException("User: "+loginRequest.getUserId()+" is not exist.");
        }
        Login login = existingLogin.get();
        if(!login.getIsActive()) {
            throw new RuntimeException("User: "+loginRequest.getUserId()+" is deactivated.");
        }
        login.setFirstName(loginRequest.getFirstName());
        login.setLastName(loginRequest.getLastName());
        login.setRole(loginRequest.getRole());
        return this.saveOrUpdate(login);
    }

    public LoginResponse activateLogin(String userId) throws  Exception {
        Optional<Login> existingLogin = loginRepository.findByUserId(userId);
        if(existingLogin.isEmpty()) {
            throw new RuntimeException("User: "+userId+" is not exist.");
        }
        Login login = existingLogin.get();
        if(login.getIsActive()) {
            throw new RuntimeException("User: "+userId+" is deactivated.");
        }
        login.setIsActive(true);
        return this.saveOrUpdate(login);
    }

    public LoginResponse deactivateLogin(String userId) throws  Exception {
        Optional<Login> existingLogin = loginRepository.findByUserId(userId);
        if(existingLogin.isEmpty()) {
            throw new RuntimeException("User: "+userId+" is not exist.");
        }
        Login login = existingLogin.get();
        if(!login.getIsActive()) {
            throw new RuntimeException("User: "+userId+" is already deactivated.");
        }
        login.setIsActive(false);
        return this.saveOrUpdate(login);
    }

    public LoginResponse updateLastLogin(String userId) throws  Exception {
        Optional<Login> existingLogin = loginRepository.findByUserId(userId);
        if(existingLogin.isEmpty()) {
            throw new RuntimeException("User: "+userId+" is not exist.");
        }
        Login login = existingLogin.get();
        if(!login.getIsActive()) {
            throw new RuntimeException("User: "+userId+" is deactivated.");
        }
        if(!login.getIsActive()) {
            throw new RuntimeException("User: "+userId+" is already deactivated.");
        }
        login.setLastLogin(Utilities.getCurrentDateTime());
        return this.saveOrUpdate(login);
    }

    public LoginResponse getLoginById(String userId) throws  Exception {
        Optional<Login> existingLogin = loginRepository.findByUserId(userId);
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
        Optional<Login> existingLogin = loginRepository.findByUserId(userId);
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
    public TokenResponse authenticate(LoginBasicAuth loginBasicAuth) throws Exception {
        if(null == loginBasicAuth || null == loginBasicAuth.getUserId() || null == loginBasicAuth.getPassword()) {
            throw new RuntimeException("userId/password should not be empty.");
        }

        String userId= loginBasicAuth.getUserId().trim();
        String password= loginBasicAuth.getPassword().trim();
        Optional<Login> existingLogin = loginRepository.findByUserId(loginBasicAuth.getUserId().trim());
        if(existingLogin.isEmpty()) {
            throw new RuntimeException("User: "+userId+" is not exist.");
        }
        Login login = existingLogin.get();
        if(!login.getIsActive()) {
            throw new RuntimeException("User: "+userId+" is deactivated.");
        }
        if(!password.equals(Utilities.decryptPassword(login.getPassword()))) {
            throw new RuntimeException("Invalid credentials.");
        }

        if(!login.getIsActive()) {
            throw new RuntimeException("User: "+userId+" is already deactivated.");
        }
        LoginResponse loginResponse = this.getLoginResponse(login);
        String token = Utilities.generateToken(loginResponse);
        login.setLastLogin(Utilities.getCurrentDateTime());
        this.saveOrUpdate(login);
        return TokenResponse.builder()
                .message("success")
                .userName(loginBasicAuth.getUserId())
                .token(token)
                .build();
    }
}