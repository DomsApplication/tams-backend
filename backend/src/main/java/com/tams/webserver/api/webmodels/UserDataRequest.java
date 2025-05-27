package com.tams.webserver.api.webmodels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Builder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class UserDataRequest {

    @JacksonXmlProperty(localName = "userId")
    private String userId;

    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "departmentId")
    private String departmentId;

    @JacksonXmlProperty(localName = "privilege")
    private String privilege;

    @JacksonXmlProperty(localName = "enabled")
    private boolean enabled;

    @JacksonXmlProperty(localName = "selecteddevice")
    private List<String> selecteddevice;

    @JacksonXmlProperty(localName = "downloadUserInfo")
    private boolean downloadUserInfo;


}
