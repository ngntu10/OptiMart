package com.Optimart.services.User;

import com.Optimart.responses.APIResponse;

public interface IUserservice {
    APIResponse<?> getUsers(int limit, int page, String search, String order,
                                       String roleId, int status, String cityId, int userType);
}
