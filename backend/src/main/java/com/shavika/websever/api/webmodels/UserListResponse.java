package com.shavika.websever.api.webmodels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class UserListResponse {

    @JacksonXmlProperty(localName = "user_id")
    private String userId;

    @JacksonXmlProperty(localName = "user_name")
    private String username;

    @JacksonXmlProperty(localName = "privilege")
    private String privilege;

    @JacksonXmlProperty(localName = "enabled")
    private String enabled;

    @JacksonXmlProperty(localName = "department_id")
    private String departmentId;

    @JacksonXmlProperty(localName = "face_data_available")
    private String faceDataAvailable;

    @JacksonXmlProperty(localName = "finger_data_available")
    private String fingerDataAvailable;

    @JacksonXmlProperty(localName = "photo_data_available")
    private String photoDataAvailable;

    @JacksonXmlProperty(localName = "password_available")
    private String passwordAvailable;

    @JacksonXmlProperty(localName = "card_available")
    private String cardAvailable;

}
