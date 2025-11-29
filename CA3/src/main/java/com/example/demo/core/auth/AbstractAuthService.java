package com.example.demo.core.auth;

public abstract class AbstractAuthService<T> {

    public final boolean authenticate(String email, String password) {
        T user = getUserByEmail(email);
        if (user == null) {
            return false;
        }
        String storedPassword = getStoredPassword(user);
        return matchPassword(password, storedPassword);
    }

    protected abstract T getUserByEmail(String email);
    protected abstract String getStoredPassword(T user);

    protected boolean matchPassword(String raw, String stored) {
        return raw.equals(stored);
    }
}
