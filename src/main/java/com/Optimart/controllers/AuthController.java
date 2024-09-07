package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.annotations.UnsecuredSwaggerOperation;
import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.Auth.*;
import com.Optimart.constants.Endpoint;
import com.Optimart.exceptions.TokenRefreshException;
import com.Optimart.models.RefreshToken;
import com.Optimart.models.User;
import com.Optimart.responses.Auth.LoginResponse;
import com.Optimart.responses.Auth.RegisterResponse;
import com.Optimart.responses.Auth.TokenRefreshResponse;
import com.Optimart.responses.Auth.UserLoginResponse;
import com.Optimart.responses.BaseResponse;
import com.Optimart.responses.CloudinaryResponse;
import com.Optimart.services.RefreshToken.RefreshTokenService;
import com.Optimart.services.Auth.AuthService;
import com.Optimart.utils.JwtTokenUtil;
import com.Optimart.utils.LocalizationUtils;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@Tag(name = "Auth", description = "Everything about auth")
@RequestMapping(Endpoint.Auth.BASE)
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper mapper;
    private final LocalizationUtils localizationUtils;
    @ApiResponse(responseCode = "201", description = "SUCCESS OPERATION", content = @Content(schema = @Schema(implementation = RegisterResponse.class), mediaType = "application/json"))
    @UnsecuredSwaggerOperation(summary = "Register User")
    @PostMapping(Endpoint.Auth.REGISTER)
    public ResponseEntity<RegisterResponse> createUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            User registerUser = authService.createUser(userRegisterDTO);
            return ResponseEntity.ok(new RegisterResponse(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY), registerUser));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponse(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED, ex.getMessage()), null));
        }
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = LoginResponse.class), mediaType = "application/json"))
    @UnsecuredSwaggerOperation(summary = "Login User")
    @PostMapping(Endpoint.Auth.LOGIN)
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String access_token = authService.login(userLoginDTO.getMail(), userLoginDTO.getPassword());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userLoginDTO.getMail());
            User user = authService.findUserByEmail(userLoginDTO.getMail());
            String refresh_token = refreshToken.getRefreshtoken();
            UserLoginResponse userLoginResponse = mapper.map(user, UserLoginResponse.class);
            return ResponseEntity.ok(LoginResponse.success(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY),
                    access_token, refresh_token, userLoginResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.failure(e.getMessage()));
        }
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserLoginResponse.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get my info user")
    @GetMapping (Endpoint.Auth.ME)
    public ResponseEntity<UserLoginResponse> getInfoCurrentUser(@Parameter @RequestHeader("Authorization") String token) {
        try {
            String email = jwtTokenUtil.extractEmail(token.substring(7));
            User user = authService.findUserByEmail(email);
            UserLoginResponse userLoginResponse = mapper.map(user, UserLoginResponse.class);
            return ResponseEntity.ok().body(userLoginResponse);
        } catch (Exception e) { return ResponseEntity.badRequest().body(null);}
    }

    @ApiResponse(responseCode = "200",description = "OK", content = @Content(schema = @Schema(implementation = TokenRefreshResponse.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get new access token")
    @PostMapping(Endpoint.Auth.REFRESH_TOKEN)
    public ResponseEntity<TokenRefreshResponse> refreshtoken(@Valid @RequestBody RefreshTokenDTO request) {
        String requestRefreshToken = request.getRefreshToken();
        try {
            RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken)
                    .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                            localizationUtils.getLocalizedMessage(MessageKeys.REFRESHTOKEN_NOT_EXIST)));
            RefreshToken verifiedRefreshToken = refreshTokenService.verifyExpiration(refreshToken);
            if (refreshTokenService.isExpired(verifiedRefreshToken)) throw new TokenRefreshException(requestRefreshToken, localizationUtils.getLocalizedMessage(MessageKeys.REFRESHTOKEN_EXPIRED) );
            User user = refreshToken.getUser();
            String newAccessToken = jwtTokenUtil.generateToken(user);
            return ResponseEntity.ok().body(TokenRefreshResponse.success(newAccessToken, requestRefreshToken, localizationUtils.getLocalizedMessage(MessageKeys.ACCESSTOKEN_SUCCESS)));
        } catch (Exception ex) { return ResponseEntity.badRequest().body(TokenRefreshResponse.failure(ex.getMessage()));}
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponse.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Update User Password")
    @PatchMapping(Endpoint.Auth.CHANGE_PASSWORD)
    public ResponseEntity<BaseResponse> changePassword(@RequestBody ChangePassword changePassword,
                                                       @Parameter @RequestHeader("Authorization") String token) throws Exception {
        return ResponseEntity.ok().body(new BaseResponse(LocalDate.now(),
                authService.changeUserPassword(changePassword,token)));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CloudinaryResponse.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Update User Avatar")
    @PostMapping(Endpoint.Auth.CHANGE_AVATAR)
    public ResponseEntity<CloudinaryResponse> updateAvatar(@Parameter @RequestHeader("Authorization") String token,
                                                           @RequestParam("file") MultipartFile file){
        CloudinaryResponse response = authService.uploadImage(token, file);
        return ResponseEntity.ok(new CloudinaryResponse(response.getPublicId(), response.getUrl(), localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_AVATAR_SUCCESS)));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponse.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Update User Info")
    @PatchMapping(Endpoint.Auth.UPDATE_INFO)
    public ResponseEntity<BaseResponse> updateInfo(@RequestBody ChangeUserInfo changeUserInfo){
        return ResponseEntity.ok(new BaseResponse(LocalDate.now(), authService.changeUserInfo(changeUserInfo)));
    }
}
