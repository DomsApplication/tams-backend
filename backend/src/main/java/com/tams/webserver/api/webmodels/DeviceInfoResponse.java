package com.tams.webserver.api.webmodels;

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
public class DeviceInfoResponse {

    @JacksonXmlProperty(localName = "id")
    private String id;

    @JacksonXmlProperty(localName = "device_serial_no")
    private String deviceSerialNo;

    @JacksonXmlProperty(localName = "cloud_id")
    private String cloudId;

    @JacksonXmlProperty(localName = "product_name")
    private String productName;

    @JacksonXmlProperty(localName = "terminal_type")
    private String terminalType;

    @JacksonXmlProperty(localName = "status")
    private String status;

    @JacksonXmlProperty(localName = "registered_on")
    private String registeredOn;

    @JacksonXmlProperty(localName = "logged_on")
    private String loggedOn;

    @JacksonXmlProperty(localName = "session_id")
    private String sessionId;

    @JacksonXmlProperty(localName = "host_address")
    private String hostAddress;

    @JacksonXmlProperty(localName = "host_port")
    private String hostPort;

    @JacksonXmlProperty(localName = "last_connected_time")
    private String lastConnectedTime;

    @JacksonXmlProperty(localName = "is_registered")
    private Boolean isRegistered;

}
