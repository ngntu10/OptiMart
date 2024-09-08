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

    private List<Integer> roleIds;
    private List<Integer> statuses;
    private List<Integer> cityIds;
    private List<Integer> userTypes;
}
