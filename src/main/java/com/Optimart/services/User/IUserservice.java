package com.Optimart.services.User;

import com.Optimart.dto.User.UserSearchDTO;
import com.Optimart.responses.User.PagingUserResponse;
import com.Optimart.responses.User.UserResponse;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public interface IUserservice {
    PagingUserResponse<List<UserResponse>> getUsers(@ModelAttribute UserSearchDTO userSearchDTO);
}
