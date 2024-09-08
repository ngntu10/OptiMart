package com.Optimart.services.User;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.User.UserSearchDTO;
import com.Optimart.models.User;
import com.Optimart.repositories.UserRepository;
import com.Optimart.responses.User.PagingUserResponse;
import com.Optimart.responses.User.UserResponse;
import com.Optimart.utils.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserservice {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final LocalizationUtils localizationUtils;
    @Override
    public PagingUserResponse<List<UserResponse>> getUsers(@ModelAttribute UserSearchDTO userSearchDTO) {
        UserResponse userResponse = new UserResponse();
        List<UserResponse> userResponseList = new ArrayList<>();
        if (userSearchDTO.getPage() == -1 && userSearchDTO.getLimit() == -1 ) {
            List<User> userList = userRepository.findAll();
            userResponseList = userList.stream()
                    .map(user -> modelMapper.map(user, UserResponse.class))
                    .toList();
            return new PagingUserResponse<>(userResponseList, localizationUtils.getLocalizedMessage(MessageKeys.USER_GET_SUCCESS), 1, (long) userResponseList.size());
        }
        Pageable pageable = PageRequest.of(userSearchDTO.getPage() - 1, userSearchDTO.getLimit(), Sort.by("createdAt").descending());
        if (StringUtils.hasText(userSearchDTO.getOrder())) {
            String order = userSearchDTO.getOrder();
            String[] orderParams = order.split("-");
            if (orderParams.length == 2) {
                Sort.Direction direction = orderParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                pageable = PageRequest.of(userSearchDTO.getPage() - 1, userSearchDTO.getLimit(), Sort.by(new Sort.Order(direction, orderParams[0])));
            }
        }
        List<Integer> roleIds = parseListFromQueryParam(userSearchDTO.getRoleId());
        List<Integer> statuses = parseListFromQueryParam(userSearchDTO.getStatus());
        List<Integer> cityIds = parseListFromQueryParam(userSearchDTO.getCityId());
        List<Integer> userTypes = parseListFromQueryParam(userSearchDTO.getUserType());

        userSearchDTO.setRoleIds(roleIds);
        userSearchDTO.setStatuses(statuses);
        userSearchDTO.setCityIds(cityIds);
        userSearchDTO.setUserTypes(userTypes);
        Page<User> users = userRepository.findUsersWithFillter(userSearchDTO, pageable);
        return new PagingUserResponse<>(userResponseList, localizationUtils.getLocalizedMessage(MessageKeys.USER_GET_SUCCESS), users.getTotalPages(), users.getTotalElements());
    }

    private List<Integer> parseListFromQueryParam(String queryParam) {
        if (queryParam == null || queryParam.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(queryParam.split("\\|"))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
