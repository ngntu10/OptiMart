package com.Optimart.services.Auth;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.Auth.ChangePassword;
import com.Optimart.dto.Auth.ChangeUserInfo;
import com.Optimart.dto.Auth.ResetPasswordDTO;
import com.Optimart.dto.Auth.UserRegisterDTO;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.exceptions.InvalidInput;
import com.Optimart.models.City;
import com.Optimart.models.Role;
import com.Optimart.models.User;
import com.Optimart.models.userShippingAddress;
import com.Optimart.repositories.*;
import com.Optimart.responses.Auth.UserLoginResponse;
import com.Optimart.responses.CloudinaryResponse;
import com.Optimart.responses.OAuth2.FacebookUserInfoResponse;
import com.Optimart.responses.OAuth2.GoogleUserInfoResponse;
import com.Optimart.services.Cloudinary.CloudinaryService;
import com.Optimart.services.OAuth2.FacebookService;
import com.Optimart.services.OAuth2.GoogleService;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CityLocaleRepository cityLocaleRepository;
    private final UserShippingAddressRepository userShippingAddressRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final CloudinaryService cloudinaryService;
    private final GoogleService googleService;
    private final FacebookService facebookService;
    private final ModelMapper mapper;
    private final LocalizationUtils localizationUtils;

    @Override
    @Transactional
    public User createUser(UserRegisterDTO userRegisterDTO) throws Exception {
        String email = userRegisterDTO.getMail();
        Optional<User> optionalUser = authRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            if (optionalUser.get().getGoogleAccountId() != null || optionalUser.get().getFacebookAccountId() != null) throw new DataIntegrityViolationException(localizationUtils.getLocalizedMessage(MessageKeys.USER_ALREADY_EXIST));
        }
        Role userRole = roleRepository.findByName("BASIC")
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ERROR)));
        User newUser = User.builder()
                .email(userRegisterDTO.getMail())
                .password(userRegisterDTO.getPassword())
                .status(1)
                .userType(3) // 1: Google, 2: Facebook, 3: email
                .fullName(userRegisterDTO.getMail())
                .userName(userRegisterDTO.getMail())
                .role(userRole)
                .build();
        authRepository.save(newUser);
        String password = userRegisterDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);
        return authRepository.save(newUser);
    }

    @Override
    @Transactional
    public String login(String email, String password) throws Exception {
        Optional<User> user = authRepository.findByEmail(email);
        if(user.isEmpty()){
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST));
        }
        User existingUser = user.get();
        if(!passwordEncoder.matches(password, existingUser.getPassword()))
            throw new BadCredentialsException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_INPUT));
        if(existingUser.getStatus() == 0) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ACCOUNT_LOCKED));
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email, password,
                existingUser.getAuthorities()
        );
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    @Transactional
    public User saveDeviceToken(String email, String deviceToken) {
        User user = authRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST)));
        user.setDeviceToken(deviceToken);
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public String changeUserPassword(ChangePassword changePassword, String token) throws Exception {
        String jwtToken = token.substring(7);
        String email = jwtTokenUtil.extractEmail(jwtToken);
        User user = authRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST)));
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
    @Transactional
    public UserLoginResponse changeUserInfo(ChangeUserInfo changeUserInfo) {
        User user = authRepository.findByEmail(changeUserInfo.getEmail())
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST)));
        mapper.map(changeUserInfo, user);
        if (changeUserInfo.getCityId() != null && !changeUserInfo.getCityId().isEmpty()){
            City city1 = cityLocaleRepository.findById(Long.parseLong(changeUserInfo.getCityId()))
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.CITY_NOT_FOUND)));
            user.setCity(city1);
        }
        if(changeUserInfo.getCity() != null){
            user.setCity(changeUserInfo.getCity());
        }
        if(changeUserInfo.getAddresses() != null){
            List<userShippingAddress> userShippingAddresses = changeUserInfo.getAddresses().stream().map(
                    item -> {
                        City city;
                        if (item.getCityId() != null) {
                            city = cityLocaleRepository.findById(Long.parseLong(item.getCityId()))
                                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.CITY_NOT_FOUND)));
                        } else city = item.getCity();
                        userShippingAddress userShippingAddress = mapper.map(item, userShippingAddress.class);
                        userShippingAddress.setIsDefault(item.getIsDefault());
                        userShippingAddress.setUser(user);
                        userShippingAddress.setCity(city);
                        userShippingAddressRepository.save(userShippingAddress);
                        return userShippingAddress;
                    }
            ).collect(Collectors.toList());
            if (userShippingAddresses.size() == 1) userShippingAddresses.get(0).setIsDefault(true);
            user.setUserShippingAddressList(userShippingAddresses);
        }
        authRepository.save(user);
        UserLoginResponse userLoginResponse = mapper.map(user, UserLoginResponse.class);
        return userLoginResponse;
    }

    @Override
    @Transactional
    public User registerGoogle(String token) {
        GoogleUserInfoResponse googleUserInfoResponse = googleService.getUserInfo(token);
        String email = googleUserInfoResponse.getEmail();
        Optional<User> optionalUser = authRepository.findByEmail(email);
        if(optionalUser.isPresent()) throw new DataIntegrityViolationException(localizationUtils.getLocalizedMessage(MessageKeys.USER_ALREADY_EXIST));
        Role userRole = roleRepository.findByName("BASIC")
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ERROR)));;
        User newUser = User.builder()
                .role(userRole)
                .email(email)
                .googleAccountId(googleUserInfoResponse.getSub())
                .status(1)
                .userType(3) // 1: Google, 2: Facebook, 3: email
                .fullName(googleUserInfoResponse.getName())
                .userName(googleUserInfoResponse.getName())
                .imageUrl(googleUserInfoResponse.getPicture())
                .build();
        authRepository.save(newUser);
        return newUser;
    }



    @Override
    @Transactional
    public String loginGoogle(String token, String deviceToken) throws Exception {
        GoogleUserInfoResponse googleUserInfoResponse = googleService.getUserInfo(token);
        Optional<User> optionalUser = authRepository.findByGoogleAccountId(googleUserInfoResponse.getSub());
        if(optionalUser.isEmpty()){
            Role userRole = roleRepository.findByName("BASIC")
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ERROR)));;
            User newUser = User.builder()
                    .role(userRole)
                    .email(googleUserInfoResponse.getEmail())
                    .googleAccountId(googleUserInfoResponse.getSub())
                    .status(1)
                    .deviceToken(deviceToken)
                    .userType(1) // 1: Google, 2: Facebook, 3: email
                    .fullName(googleUserInfoResponse.getName())
                    .userName(googleUserInfoResponse.getName())
                    .imageUrl(googleUserInfoResponse.getPicture())
                    .build();
            authRepository.save(newUser);
            return jwtTokenUtil.generateToken(newUser);
        }
        else {
            User user = optionalUser.get();
            user.setDeviceToken(deviceToken);
            userRepository.save(user);
            return jwtTokenUtil.generateToken(user);
        }
    }

    @Override
    @Transactional
    public User registerFacebook(String token) {
        FacebookUserInfoResponse facebookUserInfoResponse = facebookService.getUserProfile(token);
        Optional<User> optionalUser = authRepository.findByEmail(facebookUserInfoResponse.getEmail());
        if(optionalUser.isPresent()) throw new DataIntegrityViolationException(localizationUtils.getLocalizedMessage(MessageKeys.USER_ALREADY_EXIST));
        Role userRole = roleRepository.findByName("BASIC")
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ERROR)));;
        User newUser = User.builder()
                .role(userRole)
                .email(facebookUserInfoResponse.getEmail())
                .facebookAccountId(facebookUserInfoResponse.getId())
                .status(1)
                .userType(2) // 1: Google, 2: Facebook, 3: email
                .fullName(facebookUserInfoResponse.getFirst_name()+facebookUserInfoResponse.getLast_name())
                .userName(facebookUserInfoResponse.getFirst_name()+facebookUserInfoResponse.getLast_name())
                .imageUrl(facebookUserInfoResponse.getUrl())
                .build();
        authRepository.save(newUser);
        return newUser;
    }

    @Override
    @Transactional
    public String loginFacebook(String token, String deviceToken) throws Exception {
        FacebookUserInfoResponse facebookUserInfoResponse = facebookService.getUserProfile(token);
        Optional<User> optionalUser = authRepository.findByFacebookAccountId(facebookUserInfoResponse.getId());
        if(optionalUser.isEmpty()){
            Role userRole = roleRepository.findByName("BASIC")
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ERROR)));
            User newUser = User.builder()
                    .role(userRole)
                    .email(facebookUserInfoResponse.getEmail())
                    .facebookAccountId(facebookUserInfoResponse.getId())
                    .status(1)
                    .deviceToken(deviceToken)
                    .userType(2) // 1: Google, 2: Facebook, 3: email
                    .fullName(facebookUserInfoResponse.getFirst_name()+facebookUserInfoResponse.getLast_name())
                    .userName(facebookUserInfoResponse.getFirst_name()+facebookUserInfoResponse.getLast_name())
                    .imageUrl(facebookUserInfoResponse.getUrl())
                    .build();
            authRepository.save(newUser);
            return jwtTokenUtil.generateToken(newUser);
        }
        else {
            User user = optionalUser.get();
            user.setDeviceToken(deviceToken);
            userRepository.save(user);
            return jwtTokenUtil.generateToken(user);
        }
    }


    @Transactional
     public String resetPassword(ResetPasswordDTO resetPasswordDTO){
        User user = authRepository.findByResetToken(resetPasswordDTO.getSecretKey())
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST)));
        if(user.getResetTokenExpiration().before(new Date()))
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.RESET_TOKEN_EXPIRED));
        String encodedNewPassword = passwordEncoder.encode(resetPasswordDTO.getNewPassword());
        user.setPassword(encodedNewPassword);
        authRepository.save(user);
        return localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_PASSWORD_SUCCESSFULLY);
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
