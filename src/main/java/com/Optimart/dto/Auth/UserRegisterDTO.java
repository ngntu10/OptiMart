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
public class UserRegisterDTO {
    @Schema(description = "User email address", example = "admin@gmail.com")
    @JsonProperty("email")
    @NotBlank(message = "Mail is required")
    private String mail;

    @Schema(description = "User password", example = "123456789Tu@")
    @NotBlank(message = "Password can not be blank")
    private String password;

    @Schema(description = "User password retype", example = "123456789Tu@")
    @JsonProperty("retype_password")
    private String retypePassword;

}
