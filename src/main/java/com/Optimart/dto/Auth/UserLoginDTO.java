package com.Optimart.dto.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data //toString
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User login credentials", example = "{ \"email\": \"admin@gmail.com\", \"password\": \"123456789Tu@\" }")
public class UserLoginDTO {
    @JsonProperty("email")
    @Schema(description = "User email address", example = "admin@gmail.com")
    @NotBlank(message = "Mail is required")
    private String mail;

    @Schema(description = "User password", example = "123456789Tu@")
    @NotBlank(message = "Password can not be blank")
    private String password;

    private String deviceToken;
}
