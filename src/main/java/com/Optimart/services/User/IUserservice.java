package com.Optimart.services.User;

import com.Optimart.dto.User.CreateUserDTO;
import com.Optimart.dto.User.EditUserDTO;
import com.Optimart.dto.User.UserMutilDeleteDTO;
import com.Optimart.dto.User.UserSearchDTO;
import com.Optimart.models.User;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.Auth.UserLoginResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.responses.User.UserResponse;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public interface IUserservice {
    PagingResponse<List<UserResponse>> getUsers(@ModelAttribute UserSearchDTO userSearchDTO);
    UserResponse getOneUser (String userId);
    APIResponse<User> createNewUser(CreateUserDTO createUserDTO);
    APIResponse<UserResponse> editUser(EditUserDTO editUserDTO);
    APIResponse<Boolean> deleteUser(String userId);
    APIResponse<Boolean> deleteMutilUser(UserMutilDeleteDTO userMutilDeleteDTO);
    UserLoginResponse getUserLoginResponse(String mail);
}
