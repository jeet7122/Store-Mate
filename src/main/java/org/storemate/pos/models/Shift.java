package org.storemate.pos.models;

import java.sql.Timestamp;


/**
 * Represents a work shift of an employee in the POS system.
 * <p>
 * Each Shift object tracks the employee's ID, the shift's unique ID,
 * login time, and logout time.
 * @author Jeet
 */
public class Shift {
    private int shiftID;
    private int employeeID;
    private Timestamp loginTime;
    private Timestamp logoutTime;

    public Shift() {
    }

    public Shift(int shiftID, int employeeID, Timestamp loginTime, Timestamp logoutTime) {
        this.shiftID = shiftID;
        this.employeeID = employeeID;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
    }

    public int getShiftID() {
        return shiftID;
    }

    public void setShiftID(int shiftID) {
        this.shiftID = shiftID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    public Timestamp getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Timestamp logoutTime) {
        this.logoutTime = logoutTime;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "shiftID=" + shiftID +
                ", employeeID=" + employeeID +
                ", loginTime=" + loginTime +
                ", logoutTime=" + logoutTime +
                '}';
    }
}
