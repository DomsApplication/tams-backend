package com.shavika.websever.api.webmodels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class UserDataSyncRequest {

    @JacksonXmlProperty(localName = "userId")
    private String userId;

    @JacksonXmlProperty(localName = "setUserData")
    private boolean setUserData;

    @JacksonXmlProperty(localName = "setFingerData")
    private boolean setFingerData;

    @JacksonXmlProperty(localName = "setFaceData")
    private boolean setFaceData;

    @JacksonXmlProperty(localName = "setUserPhoto")
    private boolean setUserPhoto;

    @JacksonXmlProperty(localName = "getUserData")
    private boolean getUserData;

    @JacksonXmlProperty(localName = "getFingerData")
    private boolean getFingerData;

    @JacksonXmlProperty(localName = "getFaceData")
    private boolean getFaceData;

    @JacksonXmlProperty(localName = "getUserPhoto")
    private boolean getUserPhoto;

    @JacksonXmlProperty(localName = "selecteddevice")
    private List<String> selectedDevice;

}
