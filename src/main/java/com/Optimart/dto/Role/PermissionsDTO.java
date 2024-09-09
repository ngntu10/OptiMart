package com.Optimart.dto.Role;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionsDTO {
    private UUID roleId;
    private List<String> permissions;
}
