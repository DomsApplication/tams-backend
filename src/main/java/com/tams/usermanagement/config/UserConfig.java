package com.tams.usermanagement.config;
import com.tams.usermanagement.datasource.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class UserConfig {

    private final LoginRepository loginRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> loginRepository.findByUserId(username)
                .map(login -> User.builder()
                        .username(login.getUserId())
                        .password(login.getPassword()) // must be already encoded in DB
                        .roles(login.getRole())        // assumes single role
                        .disabled(!login.getIsActive())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
