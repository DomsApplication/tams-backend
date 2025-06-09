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
public class AuditTrailResponse {

    @JacksonXmlProperty(localName = "trace_id")
    private String traceId;

    @JacksonXmlProperty(localName = "created_on")
    private String createdOn;

    @JacksonXmlProperty(localName = "device_serial_no")
    private String deviceSerialNo;

    @JacksonXmlProperty(localName = "command")
    private String command;

    @JacksonXmlProperty(localName = "duration")
    private String duration;

    @JacksonXmlProperty(localName = "host_address")
    private String hostAddress;

    @JacksonXmlProperty(localName = "host_port")
    private String hostPort;

    @JacksonXmlProperty(localName = "start_time")
    private String startTime;

}
