package com.zy.im.common.security;

public class UserContext {
    private static final ThreadLocal<JwtUser> HOLDER = new ThreadLocal<>();

    public static void set(JwtUser user) {
        HOLDER.set(user);
    }

    public static JwtUser get() {
        return HOLDER.get();
    }

    public static String getUserId() {
        JwtUser user = HOLDER.get();
        return user == null ? null : user.getUuid();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
