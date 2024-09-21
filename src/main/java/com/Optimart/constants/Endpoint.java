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

    public static  class CityLocale{
        public static final String BASE = API_PREFIX + "/city";
        public static final String ID = "{cityId}";
        public static final String DELETE_MANY = "/delete-many";
    }

    public static  class Product{
        public static final String BASE = API_PREFIX + "/products";
        public static final String ID = "{productId}";
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
}
