package com.tams.usermanagement.api.webmodels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class LoginResponse {

    @JacksonXmlProperty(localName = "user_id")
    private String userId;

    @JacksonXmlProperty(localName = "first_name")
    private String firstName;

    @JacksonXmlProperty(localName = "last_name")
    private String lastName;

    @JacksonXmlProperty(localName = "role")
    private String role;

    @JacksonXmlProperty(localName = "last_login")
    private String lastLogin;

    @JacksonXmlProperty(localName = "is_active")
    private String isActive = "true";

}
