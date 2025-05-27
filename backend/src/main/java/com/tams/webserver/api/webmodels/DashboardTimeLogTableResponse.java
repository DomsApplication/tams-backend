package com.tams.webserver.api.webmodels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class DashboardTimeLogTableResponse {

    @JacksonXmlProperty(localName = "device_serial_no")
    private String deviceSerialNo;

    @JacksonXmlProperty(localName = "user_id")
    private String userId;

    @JacksonXmlProperty(localName = "time")
    private String time;

}
