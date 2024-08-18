package com.Optimart.dto.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePassword {
    @JsonProperty("currentPassword")
    private String currentPassword;
    @JsonProperty("newPassword")
    private String newPassword;
}
