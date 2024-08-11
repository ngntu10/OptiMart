package com.Optimart.dto.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenDTO {
    @NotBlank
    @JsonProperty("refresh_token")
    private String refreshToken;
}
