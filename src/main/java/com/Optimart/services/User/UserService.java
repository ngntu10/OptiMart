package com.Optimart.services.User;

import com.Optimart.models.User;
import com.Optimart.repositories.UserRepository;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.User.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserservice {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public APIResponse<?> getUsers(int limit, int page, String search, String order,
                                   String roleId, int status, String cityId, int userType) {
        UserResponse userResponse = new UserResponse();
        List<UserResponse> userResponseList = new ArrayList<>();
        if (limit == -1 && page == -1 ) {
            List<User> userList = userRepository.findAll();
            userResponseList = userList.stream()
                    .map(user -> modelMapper.map(user, UserResponse.class))
                    .toList();
        }

        APIResponse<List<UserResponse>> response = new APIResponse<>();
        response.setData(userResponseList);
        response.setMessage("success");
        return response;
    }
}
