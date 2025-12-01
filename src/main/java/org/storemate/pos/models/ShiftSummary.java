package org.storemate.pos.models;

/**
 * Represents a summary of an employee's work shifts for reporting purposes.
 * <p>
 * Contains the employee's ID, username, total weekly hours worked,
 * and a fixed total of standard hours (default 40 hours per week).
 * @author Khushi
 */
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
