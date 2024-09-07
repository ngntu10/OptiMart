package com.Optimart.responses.Role;

import com.Optimart.models.Role;
import com.Optimart.responses.PagingResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RoleResponse extends PagingResponse {
    List<Role> roleList;

}
