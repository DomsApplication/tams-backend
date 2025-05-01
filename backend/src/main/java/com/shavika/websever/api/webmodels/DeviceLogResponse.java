package com.shavika.websever.api.webmodels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import java.time.LocalDateTime;

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
