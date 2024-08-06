package com.Optimart.controllers;

import com.Optimart.dto.UserDTO;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.UserLoginDTO;
import com.Optimart.exceptions.TokenRefreshException;
import com.Optimart.models.RefreshToken;
import com.Optimart.models.User;
import com.Optimart.requests.TokenRefreshRequest;
import com.Optimart.responses.LoginResponse;
import com.Optimart.responses.RegisterResponse;
import com.Optimart.responses.TokenRefreshResponse;
import com.Optimart.services.RefreshToken.RefreshTokenService;
import com.Optimart.services.User.UserService;
import com.Optimart.utils.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping(Endpoint.Auth.BASE)
public class AuthController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenUtil jwtTokenUtil;
    @PostMapping(Endpoint.Auth.REGISTER)
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO,
                                        BindingResult result) {
        RegisterResponse registerResponse = new RegisterResponse();
        try {
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                registerResponse.setMessage(errorMessages.toString());
                return ResponseEntity.badRequest().body(registerResponse);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body("Password does not match");
            }
            User registerUser = userService.createUser(userDTO);
            registerResponse.setMessage("Register Successfully");
            registerResponse.setUser(registerUser);
            return ResponseEntity.ok(registerResponse);
        }catch (Exception ex) {
            registerResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerResponse);
        }
    }

    @PostMapping(Endpoint.Auth.LOGIN)
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO){

        try {
            String token = userService.login(
                    userLoginDTO.getMail(),
                    userLoginDTO.getPassword()
            );
            return ResponseEntity.ok(LoginResponse.builder()
                    .message("Login successfully")
                    .accessToken(token)
//                    .user()
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                LoginResponse.builder()
                        .message("Login failed")
                        .build()
            );
        }
    }
    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        try {
            // Tìm RefreshToken từ cơ sở dữ liệu
            RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken)
                    .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));

            // Xác thực thời gian hết hạn
            if (refreshTokenService.verifyExpiration(refreshToken)) {
                User user = refreshToken.getUser();
                // Tạo token mới
                String token = jwtTokenUtil.generateToken(user);

                // Trả về phản hồi thành công
                return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            } else {
                throw new TokenRefreshException(requestRefreshToken, "Refresh token has expired!");
            }
        } catch (TokenRefreshException ex) {
            // Trả về phản hồi lỗi với mã 400 (Bad Request)
            return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
        } catch (Exception ex) {
            // Xử lý các lỗi khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred"));
        }
    }

}
