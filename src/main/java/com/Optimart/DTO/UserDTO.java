package com.Optimart.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data //toString
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
//    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @JsonProperty("email")
    @NotBlank(message = "Mail is required")
    private String mail;

    private String address;

    @NotBlank(message = "Password can not be blank")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAccountId;

//    @NotNull(message = "Role id is required")
    @JsonProperty("role_id")
    private Long roleId;
}
