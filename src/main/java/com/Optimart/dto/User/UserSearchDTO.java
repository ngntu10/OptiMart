package com.Optimart.dto.User;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserSearchDTO {
    private int limit;
    private int page;
    private String search;
    private String order;
    private String roleId;
    private String status;
    private String cityId;
    private String userType;
}
