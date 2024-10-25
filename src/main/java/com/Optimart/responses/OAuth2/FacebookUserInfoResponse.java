package com.Optimart.responses.OAuth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacebookUserInfoResponse {
    private String url;
    private String email;
    private String first_name;
    private String last_name;
    private String id;

    @JsonProperty("picture")
    private void unpackPicture(Map<String, Object> picture) {
        if (picture != null && picture.get("data") instanceof Map) {
            Map<String, Object> data = (Map<String, Object>) picture.get("data");
            this.url = (String) data.get("url");
        }
    }
}
