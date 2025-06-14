package com.tams.webserver.api.webmodels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class RoleResponse {

    @JacksonXmlProperty(localName = "name")
    private String name;

}
