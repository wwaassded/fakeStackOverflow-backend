package com.what.spring.pojo.user;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class SessionCounter {
    private UserSession userSession = null;
    private int count = 0;

    public SessionCounter() {
    }

    public SessionCounter(UserSession userSession) {
        this.userSession = userSession;
    }

    public SessionCounter(UserSession userSession, int count) {
        this.userSession = userSession;
        this.count = count;
    }

    public void click() {
        ++count;
    }
}
