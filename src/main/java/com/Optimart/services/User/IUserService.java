package com.Optimart.services.User;

import com.Optimart.dto.UserDTO;
import com.Optimart.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String email, String password) throws Exception;
}
