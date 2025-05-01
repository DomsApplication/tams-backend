package com.shavika.websever.api.webmodels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class TokenResponse {

    @JacksonXmlProperty(localName = "message")
    private String message;

    @JacksonXmlProperty(localName = "token")
    private String token;

}
