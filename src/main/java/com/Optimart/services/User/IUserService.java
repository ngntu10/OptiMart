package com.Optimart.services.User;

import com.Optimart.dto.Auth.ChangePassword;
import com.Optimart.dto.Auth.UserRegisterDTO;
import com.Optimart.models.User;

import java.util.Optional;

public interface IUserService {
    User createUser(UserRegisterDTO userRegisterDTO) throws Exception;
    String login(String email, String password) throws Exception;
    User findUserByEmail(String email) throws  Exception;
    String changeUserPassword(ChangePassword changePassword, String token) throws Exception;
}
