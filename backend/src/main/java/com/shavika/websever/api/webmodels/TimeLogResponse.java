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
public class TimeLogResponse {

    @JacksonXmlProperty(localName = "id")
    private String id;

    @JacksonXmlProperty(localName = "device_serial_no")
    private String deviceSerialNo;

    @JacksonXmlProperty(localName = "user_id")
    private String userId;

    @JacksonXmlProperty(localName = "user_name")
    private String userName;

    @JacksonXmlProperty(localName = "attend_stat")
    private String attendStat;

    @JacksonXmlProperty(localName = "action")
    private String action;

    @JacksonXmlProperty(localName = "log_time")
    private String logTime;

}
