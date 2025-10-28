package org.storemate.pos.models;

import java.sql.Timestamp;

public class Shift {
    private Integer shiftID;
    private Integer employeeID;
    private Timestamp loginTime;
    private Timestamp logoutTime;

    public Shift() {
    }

    public Shift(Integer shiftID, Integer employeeID, Timestamp loginTime, Timestamp logoutTime) {
        this.shiftID = shiftID;
        this.employeeID = employeeID;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
    }

    public Integer getShiftID() {
        return shiftID;
    }

    public void setShiftID(Integer shiftID) {
        this.shiftID = shiftID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
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
