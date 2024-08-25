package com.Optimart.services.Role;

import com.Optimart.constants.MessageKeys;
import com.Optimart.models.Role;
import com.Optimart.repositories.RoleRepository;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.Role.RoleResponse;
import com.Optimart.utils.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{

    private final RoleRepository roleRepository;
    private final LocalizationUtils localizationUtils;
    @Override
    public APIResponse<RoleResponse> getRoles(int limit, int page, String search, String order) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending()); // Default
        if (StringUtils.hasText(order)) {
            String[] orderParams = order.split(" ");
            if (orderParams.length == 2) {
                Sort.Direction direction = orderParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                pageable = PageRequest.of(page - 1, limit, Sort.by(new Sort.Order(direction, orderParams[0])));
            }
        }
        Page<Role> rolePage;
        rolePage = StringUtils.hasText(search) ? roleRepository.findByNameContainingIgnoreCase(search, pageable) : roleRepository.findAll(pageable);
        RoleResponse roleResponse = new RoleResponse(
                rolePage.getContent(),
                rolePage.getTotalPages(),
                rolePage.getNumberOfElements());
        return new APIResponse<>(roleResponse);
    }

    @Override
    @Transactional
    public String addRole(String name) {
        Role role = new Role();
        role.setName(name);
        Role savedRole = roleRepository.save(role);
        return localizationUtils.getLocalizedMessage(MessageKeys.ADD_ROLE_SUCCESS);
    }

    @Override
    public Role getOne(String RoleId) {
        return roleRepository.findById(UUID.fromString(RoleId)).get();
    }

    @Override
    public String editRole(String id, String name) {
        Role role = roleRepository.findById(UUID.fromString(id)).get();
        role.setName(name);
        roleRepository.save(role);
        return localizationUtils.getLocalizedMessage(MessageKeys.EDIT_ROLE_SUCCESS);
    }

    @Override
    public String deleteRole(String id) {
        Role role = roleRepository.findById(UUID.fromString(id)).get();
        roleRepository.delete(role);
        return localizationUtils.getLocalizedMessage(MessageKeys.DELETE_ROLE_SUCCESS);
    }
}
