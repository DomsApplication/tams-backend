package com.tams.webserver.api.webmodels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class DashboardDeviceLogTableResponse {

    @JacksonXmlProperty(localName = "device_serial_no")
    private String deviceSerialNo;

    @JacksonXmlProperty(localName = "device_time")
    private String deviceTime;

    @JacksonXmlProperty(localName = "product_name")
    private String productName;

}
