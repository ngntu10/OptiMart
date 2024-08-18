package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.annotations.UnsecuredSwaggerOperation;
import com.Optimart.dto.Auth.UserRegisterDTO;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.Auth.UserLoginDTO;
import com.Optimart.exceptions.TokenRefreshException;
import com.Optimart.models.RefreshToken;
import com.Optimart.models.Role;
import com.Optimart.models.User;
import com.Optimart.dto.Auth.RefreshTokenDTO;
import com.Optimart.responses.Auth.LoginResponse;
import com.Optimart.responses.Auth.RegisterResponse;
import com.Optimart.responses.Auth.TokenRefreshResponse;
import com.Optimart.responses.Auth.UserLoginResponse;
import com.Optimart.responses.BaseResponse;
import com.Optimart.services.RefreshToken.RefreshTokenService;
import com.Optimart.services.User.UserService;
import com.Optimart.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Auth", description = "Everything about auth")
@RequestMapping(Endpoint.Auth.BASE)
public class AuthController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper mapper;

    @ApiResponse(responseCode = "201", description = "SUCCESS OPERATION", content = @Content(schema = @Schema(implementation = RegisterResponse.class), mediaType = "application/json"))
    @UnsecuredSwaggerOperation(summary = "Register User")
    @PostMapping(Endpoint.Auth.REGISTER)
    public ResponseEntity<RegisterResponse> createUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        RegisterResponse registerResponse = new RegisterResponse();
        try {
            User registerUser = userService.createUser(userRegisterDTO);
            registerResponse.setMessage("Register Successfully");
            registerResponse.setUser(registerUser);
            return ResponseEntity.ok(registerResponse);
        } catch (Exception ex) {
            registerResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerResponse);
        }
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = LoginResponse.class), mediaType = "application/json"))
    @UnsecuredSwaggerOperation(summary = "Login User")
    @PostMapping(Endpoint.Auth.LOGIN)
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
//            refreshTokenService.deleteByUserId(userLoginDTO.getMail());
            String access_token = userService.login(
                    userLoginDTO.getMail(),
                    userLoginDTO.getPassword()
            );
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userLoginDTO.getMail());
            User user = userService.findUserByEmail(userLoginDTO.getMail());
            String refresh_token = refreshToken.getRefreshtoken();
            UserLoginResponse userLoginResponse = mapper.map(user, UserLoginResponse.class);
            userLoginResponse.setUsername(user.getEmail());
            return ResponseEntity.ok(LoginResponse.success(access_token, refresh_token, userLoginResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.failure());
        }
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserLoginResponse.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get my info user")
    @GetMapping (Endpoint.Auth.ME)
    public ResponseEntity<UserLoginResponse> getInfoCurrentUser(@Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            String email = jwtTokenUtil.extractEmail(jwtToken);
            User user = userService.findUserByEmail(email);
            UserLoginResponse userLoginResponse = mapper.map(user, UserLoginResponse.class);
            userLoginResponse.setAvatarURL(user.getImageUrl());
            userLoginResponse.setUsername(user.getEmail());
            userLoginResponse.setRole(user.getRole());
            return ResponseEntity.ok().body(userLoginResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ApiResponse(responseCode = "200",description = "OK", content = @Content(schema = @Schema(implementation = TokenRefreshResponse.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get new access token")
    @PostMapping(Endpoint.Auth.REFRESH_TOKEN)
    public ResponseEntity<TokenRefreshResponse> refreshtoken(@Valid @RequestBody RefreshTokenDTO request) {
        String requestRefreshToken = request.getRefreshToken();
        try {
            RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken)
                    .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token not existed"));
            RefreshToken verifiedRefreshToken = refreshTokenService.verifyExpiration(refreshToken);
            if (!refreshTokenService.isExpired(verifiedRefreshToken)) {
                User user = refreshToken.getUser();
                String newAccessToken = jwtTokenUtil.generateToken(user);
                return ResponseEntity.ok().body(TokenRefreshResponse.success(newAccessToken, requestRefreshToken));
            } else {
                throw new TokenRefreshException(requestRefreshToken, "Refresh token has expired!");
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(TokenRefreshResponse.failure(ex.getMessage()));
        }
    }

//    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
//    @SecuredSwaggerOperation(summary = "Update Info User")
//    @PostMapping(Endpoint.Auth.UPDATE_INFO)
//    public ResponseEntity<BaseResponse> updateinfo(){
//        return ResponseEntity.ok().body();
//    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponse.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Update User Avatar")
    @PostMapping(Endpoint.Auth.CHANGE_AVATAR)
    public ResponseEntity<BaseResponse> updateAvatar(@RequestParam("email") String email, @RequestPart final MultipartFile file){
        userService.uploadImage(email, file);
        return ResponseEntity.ok(new BaseResponse( LocalDate.now(),"Upload successfully"));
    }

}
