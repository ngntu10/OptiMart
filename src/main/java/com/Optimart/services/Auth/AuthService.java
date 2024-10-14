package com.Optimart.services.Auth;

import com.Optimart.constants.MessageKeys;
import com.Optimart.constants.Permissions;
import com.Optimart.dto.Auth.ChangePassword;
import com.Optimart.dto.Auth.ChangeUserInfo;
import com.Optimart.dto.Auth.UserRegisterDTO;
import com.Optimart.dto.ShippingAddress.ShippingAddressDTO;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.exceptions.InvalidInput;
import com.Optimart.models.Role;
import com.Optimart.models.User;
import com.Optimart.models.userShippingAddress;
import com.Optimart.repositories.RoleRepository;
import com.Optimart.repositories.AuthRepository;
import com.Optimart.responses.Auth.UserLoginResponse;
import com.Optimart.responses.CloudinaryResponse;
import com.Optimart.services.CloudinaryService;
import com.Optimart.utils.FileUploadUtil;
import com.Optimart.utils.JwtTokenUtil;
import com.Optimart.utils.LocalizationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final AuthRepository authRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper mapper;
    private final LocalizationUtils localizationUtils;

    @Override
    @Transactional
    public User createUser(UserRegisterDTO userRegisterDTO) throws Exception {
        String email = userRegisterDTO.getMail();
        if (authRepository.existsByEmail(email)){
            throw new DataIntegrityViolationException(localizationUtils.getLocalizedMessage(MessageKeys.USER_ALREADY_EXIST));
        }
        Role userRole = roleRepository.findByName("BASIC").get();
        // CONVERT DTO => ENTITY
        User newUser = User.builder()
                .email(userRegisterDTO.getMail())
                .password(userRegisterDTO.getPassword())
                .facebookAccountId(userRegisterDTO.getFacebookAccountId())
                .googleAccountId(userRegisterDTO.getGoogleAccountId())
                .status(1)
                .userType(3)
                .fullName(userRegisterDTO.getMail())
                .userName(userRegisterDTO.getMail())
                .role(userRole)
                .build();

        authRepository.save(newUser);
        if (userRegisterDTO.getFacebookAccountId() == 0 && userRegisterDTO.getGoogleAccountId() == 0) {
            String password = userRegisterDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return authRepository.save(newUser);
    }

    @Override
    public String login(String email, String password) throws Exception {
        Optional<User> user = authRepository.findByEmail(email);
        if(user.isEmpty()){
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST));
        }
        User existingUser = user.get();

        // Check password
        if(existingUser.getFacebookAccountId() == 0 &&
              existingUser.getGoogleAccountId() == 0 ) {
            if(!passwordEncoder.matches(password, existingUser.getPassword())){
                throw new BadCredentialsException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_INPUT));
            }
        }
        if(existingUser.getStatus() == 0) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ACCOUNT_LOCKED));
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email, password,
                existingUser.getAuthorities()
        );
        //authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        return authRepository.findByEmail(email).get();
    }

    @Override
    @Transactional
    public String changeUserPassword(ChangePassword changePassword, String token) throws Exception {
        String jwtToken = token.substring(7);
        String email = jwtTokenUtil.extractEmail(jwtToken);
        User user = this.findUserByEmail(email);
        String currentPassword = changePassword.getCurrentPassword();
        String newPassword = changePassword.getNewPassword();
        if(currentPassword.equals(newPassword)) throw new InvalidInput(localizationUtils.getLocalizedMessage(MessageKeys.DIFFERENT_PASSWORD));
        if(!passwordEncoder.matches(currentPassword, user.getPassword())) throw new InvalidInput(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PASSWORD));
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        authRepository.save(user);
        return localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_PASSWORD_SUCCESSFULLY);
    }

    @Override
    public UserLoginResponse changeUserInfo(ChangeUserInfo changeUserInfo) {
        User user = authRepository.findByEmail(changeUserInfo.getEmail())
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST)));
        mapper.map(changeUserInfo, user);
        List<userShippingAddress> userShippingAddresses = changeUserInfo.getAddresses().stream().map(
                item -> {
                    userShippingAddress userShippingAddress = mapper.map(item, com.Optimart.models.userShippingAddress.class);
                    return userShippingAddress;
                }
        ).toList();
        user.setUserShippingAddressList(userShippingAddresses);
        authRepository.save(user);
        UserLoginResponse userLoginResponse = mapper.map(user, UserLoginResponse.class);
        return userLoginResponse;
    }

    @Transactional
    public CloudinaryResponse uploadImage(String token, final MultipartFile file) {
        try {
            String jwtToken = token.substring(7);
            String email = jwtTokenUtil.extractEmail(jwtToken);
            Optional<User> optionalUser = authRepository.findByEmail(email);
            if(optionalUser.isEmpty()) throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST));
            User user = optionalUser.get();
            FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);
            final String fileName = FileUploadUtil.getFileName(file.getOriginalFilename());
            final CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName);
            user.setImageUrl(response.getUrl());
            user.setCloudinaryImageId(response.getPublicId());
            authRepository.save(user);
            return response;
        } catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
}
