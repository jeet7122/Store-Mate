package org.storemate.pos.utils;

import org.storemate.pos.models.Employee;

public class Session {
    private static Session instance;
    private Employee currentUser;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setCurrentUser(Employee employee) {
        this.currentUser = employee;

    }
    public Employee getCurrentUser() {
        return currentUser;
    }
    public void logout() {
        currentUser = null;
    }
    public boolean isLoggedIn() {
        return currentUser != null;
    }

}
