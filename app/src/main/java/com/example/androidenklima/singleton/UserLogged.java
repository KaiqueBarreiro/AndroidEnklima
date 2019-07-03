package com.example.androidenklima.singleton;

import com.example.androidenklima.login.model.Police;

public class UserLogged {
    private static UserLogged instance;
    private Police police;

    public static UserLogged getInstance() {
        if (instance == null) instance = new UserLogged();
        return instance;
    }

    public Police getPolice() {
        return this.police;
    }

    public void setPolice(Police police) {
        this.police = police;
    }
}
