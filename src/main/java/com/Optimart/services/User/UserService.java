package com.Optimart.services.User;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.User.CreateUserDTO;
import com.Optimart.dto.User.EditUserDTO;
import com.Optimart.dto.User.UserMutilDeleteDTO;
import com.Optimart.dto.User.UserSearchDTO;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.models.City;
import com.Optimart.models.Role;
import com.Optimart.models.User;
import com.Optimart.repositories.CityLocaleRepository;
import com.Optimart.repositories.RoleRepository;
import com.Optimart.repositories.Specification.UserSpecification;
import com.Optimart.repositories.UserRepository;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.Auth.UserLoginResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.responses.User.UserResponse;
import com.Optimart.utils.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserservice {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CityLocaleRepository cityLocaleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final LocalizationUtils localizationUtils;
    @Override
    public PagingResponse<List<UserResponse>> getUsers(@ModelAttribute UserSearchDTO userSearchDTO) {
        List<UserResponse> userResponseList;
        List<User> userList;
        Pageable pageable;
        if (userSearchDTO.getPage() == 0 && userSearchDTO.getLimit() == 0 ) {
            userList = userRepository.findAll();
            userResponseList = userList.stream()
                    .map(user -> modelMapper.map(user, UserResponse.class))
                    .toList();
            return new PagingResponse<>(userResponseList, localizationUtils.getLocalizedMessage(MessageKeys.USER_GET_SUCCESS), 1, (long) userResponseList.size());
        } else {
            userSearchDTO.setPage(Math.max(userSearchDTO.getPage(),1));
             pageable = PageRequest.of(userSearchDTO.getPage() - 1, userSearchDTO.getLimit(), Sort.by("createdAt").descending());
        }
        if (StringUtils.hasText(userSearchDTO.getOrder())) {
            String order = userSearchDTO.getOrder();
            String[] orderParams = order.split("-");
            if (orderParams.length == 2) {
                Sort.Direction direction = orderParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                pageable = PageRequest.of(userSearchDTO.getPage() - 1, userSearchDTO.getLimit(), Sort.by(new Sort.Order(direction, orderParams[0])));
            }
        }
        Specification<User> specification = UserSpecification.filterUsers(userSearchDTO.getRoleId(), userSearchDTO.getStatus(), userSearchDTO.getCityId(),
                                                                          userSearchDTO.getUserType(), userSearchDTO.getSearch());
        Page<User> userPage = userRepository.findAll(specification, pageable);
        userList = userPage.getContent();
        userResponseList = userList.stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .toList();
        return new PagingResponse<>(userResponseList, localizationUtils.getLocalizedMessage(MessageKeys.USER_GET_SUCCESS), userPage.getTotalPages(), userPage.getTotalElements());
    }

    @Override
    public UserResponse getOneUser(String userId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST)));
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return userResponse;
    }

    @Override
    @Transactional
    public APIResponse<User> createNewUser(CreateUserDTO createUserDTO) {
        if (userRepository.existsByEmail(createUserDTO.getEmail()))
            return new APIResponse<>(null, localizationUtils.getLocalizedMessage(MessageKeys.USER_ALREADY_EXIST));
        if (userRepository.existsByPhoneNumber(createUserDTO.getPhoneNumber()))
                return new APIResponse<>(null, localizationUtils.getLocalizedMessage(MessageKeys.USER_PHONE_EXISTED));
        User user = modelMapper.map(createUserDTO, User.class);
        Role role = roleRepository.findById(UUID.fromString(createUserDTO.getRole())).get();
        user.setRole(role);
        user.setUserType(1);
        user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
        userRepository.save(user);
        return new APIResponse<>(user, localizationUtils.getLocalizedMessage(MessageKeys.USER_CREATE_SUCCESS) );
    }

    @Override
    @Transactional
    public APIResponse<UserResponse> editUser(EditUserDTO editUserDTO) {
        User user = userRepository.findByEmail(editUserDTO.getEmail())
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST)));
        Role role = roleRepository.findByName(editUserDTO.getRole())
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_NOT_FOUND)));
        modelMapper.map(editUserDTO, user);
        user.setRole(role);
        if(!editUserDTO.getCity().isEmpty() && editUserDTO.getCity().length() >=3){
        City city = cityLocaleRepository.findByName(editUserDTO.getCity())
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.CITY_NOT_FOUND)));
        user.setCity(city);
        }else {
            City city = cityLocaleRepository.findById(Long.parseLong(editUserDTO.getCity()))
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.CITY_NOT_FOUND)));
            user.setCity(city);
        }
        userRepository.save(user);
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return new APIResponse<>(userResponse, localizationUtils.getLocalizedMessage(MessageKeys.USER_EDIT_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteUser(String userId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST)));
        System.out.println(user.getRole().getName());
        if(user.getRole().getName().equals(Role.ADMIN)) return new APIResponse<>(null, localizationUtils.getLocalizedMessage(MessageKeys.NOT_DELETE_ADMIN_USER));
        userRepository.delete(user);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.USER_DELETE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteMutilUser(UserMutilDeleteDTO userMutilDeleteDTO) {
        List<String> userList = userMutilDeleteDTO.getUserIds();
        userList.forEach(item -> {
            userRepository.deleteById(UUID.fromString(item));
        });
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.USER_DELETE_SUCCESS));
    }

    @Override
    public UserLoginResponse getUserLoginResponse(String mail){
        User user = userRepository.findByEmail(mail)
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST)));
        UserLoginResponse userLoginResponse = modelMapper.map(user, UserLoginResponse.class);
        userLoginResponse.setCity(user.getCity());
        userLoginResponse.setAddresses(user.getUserShippingAddressList());
        return userLoginResponse;
    }
}
