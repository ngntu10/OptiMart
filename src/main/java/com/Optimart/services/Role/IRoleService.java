package com.Optimart.services.Role;

import com.Optimart.models.Role;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.Role.RoleResponse;

public interface IRoleService {
    APIResponse<RoleResponse> getRoles(int limit, int page, String search, String order);
    String addRole(String name);
    Role getOne(String RoleId);
    String editRole(String id, String name);
    String deleteRole(String id);
}
