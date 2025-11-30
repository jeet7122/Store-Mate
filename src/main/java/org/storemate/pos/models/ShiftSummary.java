package org.storemate.pos.models;

public class ShiftSummary {
    private int employeeId;
    private String username;
    private int weeklyHours;
    private int totalHours = 40; // fixed

    public ShiftSummary(int employeeId, String username, int weeklyHours) {
        this.employeeId = employeeId;
        this.username = username;
        this.weeklyHours = weeklyHours;
    }

    public int getEmployeeId() { return employeeId; }
    public String getUsername() { return username; }
    public int getWeeklyHours() { return weeklyHours; }
    public int getTotalHours() { return totalHours; }
}
