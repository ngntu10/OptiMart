package com.Optimart.constants;

public class Endpoint {
    public static final String API_PREFIX = "/api/v1";

    public static final class Auth {
        public static final String BASE = API_PREFIX + "/auth";
        public static final String LOGIN = "/login";
        public static final String ME = "/me";
        public static final String REGISTER = "/register";
        public static final String REFRESH_TOKEN = "/refreshtoken";
        public static final String UPDATE_INFO = "/update-info";
        public static final String CHANGE_PASSWORD = "/change-password";
        public static final String CHANGE_AVATAR = "/avatar";
    }

    public static final class User {
        public static final String BASE = API_PREFIX + "/user";

    }
    public static final class Role {
        public static final String Base = API_PREFIX + "/roles";
        public static final String ID = "{roleId}";
        public static final String DELETE_MANY = "/delete-many";
    }

}
