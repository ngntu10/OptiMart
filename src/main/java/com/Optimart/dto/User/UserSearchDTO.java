package com.Optimart.dto.User;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserSearchDTO {
    private int limit= -1;
    private int page = -1;
    private String search;
    private String order;
    private String roleId;
    private String status;
    private String cityId;
    private String userType;
}
