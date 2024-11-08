package com.Optimart.services.Role;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.Role.UpdateRoleDTO;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.models.Role;
import com.Optimart.repositories.RoleRepository;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.Role.RoleResponse;
import com.Optimart.utils.LocalizationUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{

    private final RoleRepository roleRepository;
    private final LocalizationUtils localizationUtils;
    @Override
    public APIResponse<RoleResponse> getRoles(int limit, int page, String search, String order) {
        RoleResponse roleResponse = new RoleResponse();
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending()); // Default
        if (StringUtils.hasText(order)) {
            String[] orderParams = order.split("-");
            if (orderParams.length == 2) {
                Sort.Direction direction = orderParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                pageable = PageRequest.of(page - 1, limit, Sort.by(new Sort.Order(direction, orderParams[0])));
            }
        }
            Page<Role> rolePage;
            rolePage = StringUtils.hasText(search) ? roleRepository.findByNameContainingIgnoreCase(search, pageable) : roleRepository.findAll(pageable);
            roleResponse = RoleResponse.builder()
                .roleList(rolePage.getContent())
                .totalPage(rolePage.getTotalPages())
                .totalCount(rolePage.getNumberOfElements())
                .build();
            return new APIResponse<>(roleResponse, "Get role success");
        }

    @Override
    @Transactional
    public APIResponse<Role> addRole(String name) {
        Role role = new Role();
        Optional<Role> checkExistedRole = roleRepository.findByName(name);
        if (checkExistedRole.isPresent())
            return new APIResponse<>(null,localizationUtils.getLocalizedMessage(MessageKeys.ROLE_EXISTED));
        role.setName(name);
        Role savedRole = roleRepository.save(role);
        return new APIResponse<>(role,localizationUtils.getLocalizedMessage(MessageKeys.ADD_ROLE_SUCCESS));
    }

    @Override
    public Role getOne(String RoleId) {
        return roleRepository.findById(UUID.fromString(RoleId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_NOT_FOUND)));
    }

    @Override
    @Transactional
    public APIResponse<Role> editRole(String id, UpdateRoleDTO updateRoleDTO) {
        Role role = roleRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_NOT_FOUND)));;
        role.setName(updateRoleDTO.getName());
        role.setPermissions(updateRoleDTO.getPermissions());
        roleRepository.save(role);
        return new APIResponse<>(role,localizationUtils.getLocalizedMessage(MessageKeys.EDIT_ROLE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<?> deleteRole(String id) {
        Role role = roleRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_NOT_FOUND)));;
        roleRepository.delete(role);
        return new APIResponse<>(true,localizationUtils.getLocalizedMessage(MessageKeys.DELETE_ROLE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<?> deleteMany(List<String> roleList) {
        return null;
    }
}
