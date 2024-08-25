package com.Optimart.constants;

public class MessageKeys {
    // **** Auth
    public static final String REGISTER_SUCCESSFULLY = "auth.register.register_successfully";
    public static final String REGISTER_FAILED = "auth.register.register_failed";
    public static final String LOGIN_SUCCESSFULLY = "auth.login.login_successfully";
    public static final String LOGIN_FAILED = "auth.login.login_failed";
    public static final String USER_ALREADY_EXIST = "error.auth.register.user_already_exists";
    public static final String USER_NOT_EXIST = "error.auth.login.user_not_exists";
    public static final String WRONG_INPUT = "error.auth.login.wrong_email_or_password";
    public static final String ACCOUNT_LOCKED = "error.auth.login.account_locked";
    public static final String DIFFERENT_PASSWORD = "error.auth.change.different_password";
    public static final String WRONG_PASSWORD = "error.auth.change.wrong_password";
    public static final String UPDATE_PASSWORD_SUCCESSFULLY = "auth.change.update_password_success";
    public static final String UPDATE_USER_SUCCESSFULLY = "auth.change.update_user_success";
    public static final String REFRESHTOKEN_NOT_EXIST = "error.auth.refreshtoken.not_exist";
    public static final String REFRESHTOKEN_EXPIRED = "error.auth.refreshtoken.expired";
    public static final String ACCESSTOKEN_SUCCESS = "auth.refreshtoken.get_access_token_success";
    public static final String UPDATE_AVATAR_SUCCESS = "auth.avatar.update_success";

    // ROLES
    public static final String ADD_ROLE_SUCCESS = "role.add_success";
    public static final String EDIT_ROLE_SUCCESS = "role.edit_success";
    public static final String DELETE_ROLE_SUCCESS = "role.delete_success";
}
