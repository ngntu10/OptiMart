package com.Optimart.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CloudinaryResponse {
    private String publicId;
    private String url;
    private String message;

    public static CloudinaryResponse success(String publicId, String url){
        return new CloudinaryResponse(publicId, url, "Update Successfully");
    }
}
