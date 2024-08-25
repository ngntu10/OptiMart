package com.Optimart.responses.Role;

import com.Optimart.models.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {
    List<Role> roleList;
    private int totalPage;
    private int totalCount;
}
