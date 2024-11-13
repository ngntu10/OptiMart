package com.Optimart.constants;

public class Endpoint {
    public static final String API_PREFIX = "/api/v1";

    public static final class Auth {
        public static final String BASE = API_PREFIX + "/auth";
        public static final String LOGIN = "/login";
        public static final String LOGIN_GOOGLE = "/login-google";
        public static final String REGISTER_GOOGLE ="/register-google";
        public static final String LOGIN_FACEBOOK = "/login-facebook";
        public static final String FORGOT_PASSWORD = "/forgot-password";
        public static final String RESET_PASSWORD = "/reset-password";
        public static final String REGISTER_FACEBOOK ="/register-facebook";
        public static final String LOGOUT = "/logout";
        public static final String ME = "/me";
        public static final String REGISTER = "/register";
        public static final String REFRESH_TOKEN = "/refreshtoken";
        public static final String UPDATE_INFO = "/update-info";
        public static final String CHANGE_PASSWORD = "/change-password";
        public static final String CHANGE_AVATAR = "/avatar";
    }

    public static final class User {
        public static final String BASE = API_PREFIX + "/users";
        public static final String ID = "{userId}";
        public static final String DELETE_MANY = "/delete-many";

    }
    public static final class Role {
        public static final String BASE = API_PREFIX + "/roles";
        public static final String ID = "{roleId}";
        public static final String DELETE_MANY = "/delete-many";
    }

    public static  class PaymentType{
        public static final String BASE = API_PREFIX + "/payment-type";
        public static final String ID = "{paymentTypeId}";
        public static final String DELETE_MANY = "/delete-many";
    }

    public static  class DeliveryType{
        public static final String BASE = API_PREFIX + "/delivery-type";
        public static final String ID = "{deliveryTypeId}";
        public static final String DELETE_MANY = "/delete-many";
    }

    public static class ProductType{
        public static final String BASE = API_PREFIX + "/product-types";
        public static final String ID = "{productTypeId}";
        public static final String DELETE_MANY = "/delete-many";
    }

    public static  class CityLocale{
        public static final String BASE = API_PREFIX + "/city";
        public static final String ID = "{cityId}";
        public static final String DELETE_MANY = "/delete-many";
    }

    public static  class Product{
        public static final String BASE = API_PREFIX + "/products";
        public static final String ID = "{productId}";
        public static final String CHANGE_IMAGE = "/image";
        public static final String DELETE_MANY = "/delete-many";
        public static final String PUBLIC = "/public";
        public static final String RELATED = "/related";
        public static final String PUBLIC_ID = "/public/{productId}";
        public static final String PUBLIC_SLUG_ID = "/public/slug/{slugId}";
        public static final String LIKE = "/like";
        public static final String UNLIKE = "/unlike";
        public static final String LIKED_ME = "/liked/me";
        public static final String VIEWED_ME = "viewed/me";
    }

    public static class Order{
        public static final String BASE = API_PREFIX + "/orders";
        public static final String ME = "/me";
        public static final String CANCEL = "/me/cancel/{orderId}";
        public static final String ID_ME = "/me/{orderId}";
        public static final String ID = "/{orderId}";
        public static final String STATUS_ID = "/status/{orderId}";
    }

    public static class Reivew{
        public static final String BASE = API_PREFIX + "/reviews";
        public static final String ID = "/{reviewId}";
        public static final String DELETE_MANY = "/delete-many";
        public static final String ID_ME = "/me/{reviewId}";
    }

    public static class Payment{
        public static final String BASE = API_PREFIX + "/payment";
        public static final String VNPAY = "/vnpay";
        public static final String CALL_BACK = "/vnpay-callback";
    }

    public static class Comment{
        public static final String BASE = API_PREFIX + "/comments";
        public static final String PUBLIC = "/public";
        public static final String ID_ME = "/me/{commentId}";
        public static final String ID = "/{commentId}";
        public static final String REPLY_COMMENT = "/reply";
        public static final String DELETE_MANY = "/delete-many";
    }
    public static class Report{
        public static final String BASE = API_PREFIX + "/report";
        public static final String PRODUCT_TYPE = "/product-type/count";
        public static final String USER_TYPE = "/user-type/count";
        public static final String PRODUCT_STATUS = "/product-status/count";
        public static final String ORDER_STATUS = "/order-status/count";
        public static final String ALL_RECORDS = "/all-records/count";
        public static final String REVENUE_TOTAL = "/revenue-total";
    }

    public static class Notification{
        public static final String BASE = API_PREFIX + "/notifications";
        public static final String MARK_READ = "/{notificationId}/read";
        public static final String DELETE_NOTIFICATION = "/{notificationId}";
        public static final String  MARK_READ_ALL_NOTIFICATION = "/all/read";
    }
}
