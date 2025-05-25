package ru.akalavan.investmentservice.utils;

import org.springframework.util.Assert;

public class UserContextHolder {
    private static final ThreadLocal<UserContext> USER_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static final UserContext getContext() {
        UserContext userContext = USER_CONTEXT_THREAD_LOCAL.get();

        if (userContext == null) {
            userContext = createEmptyContext();
            USER_CONTEXT_THREAD_LOCAL.set(userContext);
        }

        return userContext;
    }

    public static final void setContext(UserContext userContext) {
        Assert.notNull(userContext, "Only non-null UserContext instances are permitted");
        USER_CONTEXT_THREAD_LOCAL.set(userContext);
    }

    public static final UserContext createEmptyContext() {
        return new UserContext();
    }
}
