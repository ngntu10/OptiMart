package com.Optimart.controllers;

import com.Optimart.annotations.SwaggerOperation;
import com.Optimart.dto.Auth.UserRegisterDTO;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.Auth.UserLoginDTO;
import com.Optimart.exceptions.TokenRefreshException;
import com.Optimart.models.RefreshToken;
import com.Optimart.models.User;
import com.Optimart.dto.Auth.RefreshTokenRequest;
import com.Optimart.responses.LoginResponse;
import com.Optimart.responses.RegisterResponse;
import com.Optimart.responses.TokenRefreshResponse;
import com.Optimart.services.RefreshToken.RefreshTokenService;
import com.Optimart.services.User.UserService;
import com.Optimart.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Auth", description = "Everything about auth")
@RequestMapping(Endpoint.Auth.BASE)
public class AuthController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenUtil jwtTokenUtil;

    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    schema = @Schema(implementation = RegisterResponse.class),
                    mediaType = "application/json"
            )
    )
    @SwaggerOperation(summary = "Register User")
    @PostMapping(Endpoint.Auth.REGISTER)
    public ResponseEntity<RegisterResponse> createUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO,
                                                       BindingResult result) {
        RegisterResponse registerResponse = new RegisterResponse();
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                registerResponse.setMessage(errorMessages.toString());
                return ResponseEntity.badRequest().body(registerResponse);
            }
            if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getRetypePassword())) {
                registerResponse.setMessage("Password does not match");
                return ResponseEntity.badRequest().body(registerResponse);
            }
            User registerUser = userService.createUser(userRegisterDTO);
            registerResponse.setMessage("Register Successfully");
            registerResponse.setUser(registerUser);
            return ResponseEntity.ok(registerResponse);
        } catch (Exception ex) {
            registerResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerResponse);
        }
    }

    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    schema = @Schema(implementation = LoginResponse.class),
                    mediaType = "application/json"
            )
    )
    @SwaggerOperation(summary = "Login User")
    @PostMapping(Endpoint.Auth.LOGIN)
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            refreshTokenService.deleteByUserId(userLoginDTO.getMail());
            String access_token = userService.login(
                    userLoginDTO.getMail(),
                    userLoginDTO.getPassword()
            );
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userLoginDTO.getMail());
            User user = userService.findUserByEmail(userLoginDTO.getMail());
            String refresh_token = refreshToken.getRefreshtoken();
            return ResponseEntity.ok(LoginResponse.builder()
                            .message("Login successfully")
                            .accessToken(access_token)
                            .refreshToken(refresh_token)
                            .user(user)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message("Login successfully")
                            .accessToken("")
                            .refreshToken("")
                            .user(null)
                            .build()
            );
        }
    }

    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    schema = @Schema(implementation = TokenRefreshResponse.class),
                    mediaType = "application/json"
            )
    )
    @SwaggerOperation(summary = "Get new access token", implementation = TokenRefreshResponse.class)
    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshtoken(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        try {
            RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken)
                    .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token not existed"));
            RefreshToken verifiedRefreshToken = refreshTokenService.verifyExpiration(refreshToken);
            if (refreshTokenService.isExpired(verifiedRefreshToken)) {
                User user = refreshToken.getUser();
                String token = jwtTokenUtil.generateToken(user);
                return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken, "Get access token success"));
            } else {
                throw new TokenRefreshException(requestRefreshToken, "Refresh token has expired!");
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(
                    TokenRefreshResponse.builder()
                            .message(ex.getMessage())
                            .build()
            );
        }
    }

}
