package com.tams.webserver.api.webmodels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class DeviceDataSyncRequest {

    @JacksonXmlProperty(localName = "deviceSerialNo")
    private String deviceSerialNo;

    @JacksonXmlProperty(localName = "getDepartment")
    private boolean getDepartment;

    @JacksonXmlProperty(localName = "setDepartment")
    private boolean setDepartment;

    @JacksonXmlProperty(localName = "emptyTimeLog")
    private boolean emptyTimeLog;

    @JacksonXmlProperty(localName = "emptyManageLog")
    private boolean emptyManageLog;

    @JacksonXmlProperty(localName = "emptyAllData")
    private boolean emptyAllData;

    @JacksonXmlProperty(localName = "emptyUserEnrollmentData")
    private boolean emptyUserEnrollmentData;


}
