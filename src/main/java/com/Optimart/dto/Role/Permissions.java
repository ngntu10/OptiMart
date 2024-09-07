package com.Optimart.dto.Role;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permissions {
    private List<String> permissions;
}
