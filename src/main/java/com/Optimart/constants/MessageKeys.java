package com.Optimart.constants;

public class MessageKeys {
    // **** App
    public static final String EMAIL_SENT = "email.sent";
    public static final String GET_STATISTICS = "statistics.get_success";

    // **** Errors
    public static final String ERROR = "optimart.error";

    // **** Refresh Token
    public static final String REFRESH_TOKEN_NOT_FOUND = "refresh_token.not_found";
    public static final String SIGN_IN_AGAIN = "sign_in_again";
    public static final String CREATE_REFRESH_TOKEN_FAILED = "refresh_token.create_failed";
    public static final String DELETE_REFRESH_TOKEN_SUCCESS = "refresh_token.delete_success";

    // **** Auth
    public static final String REGISTER_SUCCESSFULLY = "auth.register.register_successfully";
    public static final String REGISTER_FAILED = "auth.register.register_failed";
    public static final String LOGIN_SUCCESSFULLY = "auth.login.login_successfully";
    public static final String LOGIN_FAILED = "auth.login.login_failed";
    public static final String LOGOUT_SUCCESS = "auth.logout.logout_success";
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
    public static final String RESET_TOKEN_EXPIRED = "auth.reset_token.expired";
    public static final String UPDATE_AVATAR_SUCCESS = "auth.avatar.update_success";

    // ROLES
    public static final String ADD_ROLE_SUCCESS = "role.add_success";
    public static final String EDIT_ROLE_SUCCESS = "role.edit_success";
    public static final String DELETE_ROLE_SUCCESS = "role.delete_success";
    public static final String ROLE_EXISTED = "role.existed";
    public static final String ROLE_NOT_FOUND = "role.not_found";

    // USERS
    public static final String USER_GET_SUCCESS = "users.get_success";
    public static final String USER_CREATE_SUCCESS = "users.create_success";
    public static final String USER_PHONE_EXISTED = "users.phone_existed";
    public static final String USER_EDIT_SUCCESS = "users.edit_success";
    public static final String USER_DELETE_SUCCESS = "user.delete_success";
    public static final String NOT_DELETE_ADMIN_USER = "user.not_delete_admin";

    // DELIVERY TYPES
    public static final String DELIVERY_TYPE_CREATE_SUCCESS = "delivery_type.create_success";
    public static final String DELIVERY_TYPE_UPDATE_SUCCESS = "delivery_type.update_success";
    public static final String DELIVERY_TYPE_DELETE_SUCCESS = "delivery_type.delete_success" ;
    public static final String DELIVERY_TYPE_GET_SUCCESS = "delivery_type.get_success";
    public static final String DELIVERY_TYPE_NOT_FOUND = "delivery_type.not_found";

    // PAYMENT TYPE
    public static final String PAYMENT_TYPE_CREATE_SUCCESS = "payment_type.create_success";
    public static final String PAYMENT_TYPE_UPDATE_SUCCESS = "payment_type.update_success";
    public static final String PAYMENT_TYPE_DELETE_SUCCESS = "payment_type.delete_success" ;
    public static final String PAYMENT_TYPE_GET_SUCCESS = "payment_type.get_success";
    public static final String PAYMENT_TYPE_NOT_FOUND = "payment_type.not_found";

    // PRODUCT TYPE
    public static final String PRODUCT_TYPE_CREATE_SUCCESS = "product_type.create_success";
    public static final String PRODUCT_TYPE_UPDATE_SUCCESS = "product_type.update_success";
    public static final String PRODUCT_TYPE_DELETE_SUCCESS = "product_type.delete_success";
    public static final String PRODUCT_TYPE_GET_SUCCESS = "product_type.get_success";
    public static final String PRODUCT_TYPE_NOT_FOUND = "product_type.not_found";

    // CITY LOCALES
    public static final String CITY_CREATE_SUCCESS = "city.create_success";
    public static final String CITY_UPDATE_SUCCESS = "city.update_success";
    public static final String CITY_DELETE_SUCCESS = "city.delete_success" ;
    public static final String CITY_GET_SUCCESS = "city.get_success" ;
    public static final String CITY_NOT_FOUND = "city.not_found";

    // PRODUCT
    public static final String PRODUCT_CREATE_SUCCESS = "product.create_success";
    public static final String PRODUCT_UPDATE_SUCCESS = "product.update_success";
    public static final String PRODUCT_DELETE_SUCCESS = "product.delete_success" ;
    public static final String PRODUCT_GET_SUCCESS = "product.get_success" ;
    public static final String PRODUCT_NOT_EXISTED = "product.not_existed";
    public static final String PRODUCT_IMAGE_UPDATE_SUCCESS = "product.image.update_success";
    public static final String PRODUCT_LIKED = "product.liked_success";
    public static final String PRODUCT_LIKE_FAILED = "product.like_failed";
    public static final String PRODUCT_UNLIKED = "product.unlike_success";
    public static final String PRODUCT_UNLIKED_FAILED = "product.unlike_failed";

    // ORDER
    public static final String ORDER_CREATE_SUCCESS = "order.create_success";
    public static final String ORDER_CREATE_SUCCESS_ID = "order_id.create_success";
    public static final String ORDER_GET_SUCCESS = "order.get_success";
    public static final String ORDER_LIST_GET_SUCCESS = "order_list.get_success";
    public static final String ORDER_CANCEL_SUCCESS = "order.cancel_success";
    public static final String ORDER_DELETE_SUCCESS = "order.delete_success";
    public static final String ORDER_NOT_FOUND = "order.not_found";
    public static final String ORDER_CANCEL_SUCCESS_ID = "order_id.cancel_success";
    public static final String ORDER_CHANGE_STATUS_SUCCESS = "order.change_status_success";
    public static final String ORDER_CHANGE_STATUS_ID = "order_id.change_status_success";

    // REVIEW
    public static final String REVIEW_GET_SUCCESS = "review.get_success";
    public static final String REVIEW_LIST_GET_SUCCESS = "review_list.get_success";
    public static final String REVIEW_WRITE_SUCCESS = "review.write_success";
    public static final String REVIEW_NOT_EXISTED = "review.not_existed";
    public static final String REVIEW_DELETE_SUCCESS = "review.delete_success";

    // PAYMENT
    public static final String PAY_SUCCESS = "payment.success";
    public static final String PAY_FAILED = "payment.failed";
    public static final String PAYMENT_SUCCESS_ID = "payment.pay_success";

    // COMMENT
    public static final String CREATE_COMMENT_SUCCESS = "comment.create_success";
    public static final String CREATE_COMMENT_FAILED = "comment.create_failed";
    public static final String GET_LIST_COMMENT_SUCCESS = "comment.get_list_success";
    public static final String UPDATE_COMMENT_SUCCESS = "comment.update_success";
    public static final String UPDATE_COMMENT_FAILED = "comment.update_failed";
    public static final String DELETE_COMMENT_SUCCESS = "comment.delete_success";
    public static final String DELETE_COMMENT_FAILED = "comment.delete_failed";
    public static final String COMMENT_NOT_FOUND = "comment.not_found";
    public static final String GET_COMMENT_SUCCESS = "comment.get_success";
    public static final String REPLY_COMMENT_SUCCESS = "comment.reply_success";

    // NOTIFICATION
    public static final String NOTIFICATION_GET_LIST_SUCCESS = "notification.get_list_success";
    public static final String NOTIFICATION_NOT_FOUND = "notification.not_found";
    public static final String NOTIFICATION_READ = "notification.read";
    public static final String NOTIFICATION_GET_LIST_FAILED = "notification.get_list_failed";
    public static final String NOTIFICATION_DELETE_SUCCESS = "notification.delete_success";
    public static final String NOTIFICATION_DELETE_FAILED = "notification.delete_failed";
    public static final String NOTIFICATION_READ_ALL = "notification.read_all_success";
}
