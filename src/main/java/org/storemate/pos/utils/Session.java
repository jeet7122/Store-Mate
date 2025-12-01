package org.storemate.pos.utils;

import org.storemate.pos.models.Employee;

/**
 * Session manages the currently logged-in user within the application.
 * <p>
 * Implements the Singleton pattern to ensure a single session instance
 * is used across the application.
 * @author Billen
 */
public class Session {
    private static Session instance;
    private Employee currentUser;

    private Session() {}

    /**
     * Returns the single instance of Session.
     * Creates it if it doesn't exist yet.
     *
     * @return the Session instance
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    /**
     * Sets the currently logged-in user.
     *
     * @param employee the Employee object representing the logged-in user
     */
    public void setCurrentUser(Employee employee) {
        this.currentUser = employee;

    }
    /**
     * Returns the currently logged-in user.
     *
     * @return the current Employee, or null if no user is logged in
     */
    public Employee getCurrentUser() {
        return currentUser;
    }

    /**
     * Logs out the current user by clearing the session.
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

}
