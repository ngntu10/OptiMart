package com.Optimart.dto.Auth;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String newPassword;
    private String secretKey;
}
