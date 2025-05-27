package com.tams.webserver.api.webmodels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class UserFingerResponse {

    @JacksonXmlProperty(localName = "userId")
    private String userId;

    @JacksonXmlProperty(localName = "fingerId")
    private Integer fingerId;

    @JacksonXmlProperty(localName = "enrolled")
    private boolean enrolled;

    @JacksonXmlProperty(localName = "duress")
    private boolean duress;

    @JacksonXmlProperty(localName = "fingerData")
    private boolean fingerData;

}

