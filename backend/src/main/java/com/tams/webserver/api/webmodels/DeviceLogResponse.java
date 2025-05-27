package com.tams.webserver.api.webmodels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class DeviceLogResponse {

    @JacksonXmlProperty(localName = "deviceSerialNo")
    private String deviceSerialNo;

    @JacksonXmlProperty(localName = "deviceTime")
    private String deviceTime;
}
