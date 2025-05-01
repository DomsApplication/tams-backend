package com.shavika.websever.api.webmodels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class DashboardWidgetsResponse {

    @JacksonXmlProperty(localName = "title")
    private String title;

    @JacksonXmlProperty(localName = "description")
    private String description;

    @JacksonXmlProperty(localName = "count")
    private String count;

}
