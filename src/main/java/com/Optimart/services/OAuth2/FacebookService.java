package com.Optimart.services.OAuth2;

import com.Optimart.responses.OAuth2.FacebookUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class FacebookService {

    private final RestTemplate restTemplate;

    public FacebookUserInfoResponse getUserProfile(String accessToken) {
        String url = "https://graph.facebook.com/me";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("access_token", accessToken)
                .queryParam("fields", "picture,email,first_name,last_name,id");
        FacebookUserInfoResponse response = restTemplate.getForObject(builder.toUriString(), FacebookUserInfoResponse.class);
        return response;
    }
}

