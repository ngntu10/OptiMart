package com.Optimart.services.OAuth2;

import com.Optimart.responses.GoogleUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@RequiredArgsConstructor
public class GoogleService {

    private final RestTemplate restTemplate;
    public GoogleUserInfoResponse getUserInfo(String token) {
        String url = "https://www.googleapis.com/oauth2/v3/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserInfoResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, GoogleUserInfoResponse.class);

        return response.getBody();
    }
}
