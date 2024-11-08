package com.Optimart.services.Role;

import com.Optimart.dto.Role.UpdateRoleDTO;
import com.Optimart.models.Role;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.Role.RoleResponse;

import java.util.List;

public interface IRoleService {
    APIResponse<RoleResponse> getRoles(int limit, int page, String search, String order);
    public APIResponse<Role> addRole(String name);
    Role getOne(String RoleId);
    public APIResponse<Role> editRole(String id, UpdateRoleDTO updateRoleDTO);
    public APIResponse<?> deleteRole(String id);
    public APIResponse<?> deleteMany(List<String> roleList);
}
