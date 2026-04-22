package com.wmsai.oopj;

/**
 * AdminUser extends Employee — represents an admin in the system.
 * Covers OOPJ-18 (deep inheritance: Person → Employee → AdminUser).
 */
public class AdminUser extends Employee {
    private String accessLevel;

    public AdminUser() {}

    public AdminUser(String name, String email, String phone, int employeeId, String accessLevel) {
        super(name, email, phone, employeeId, "Administration");
        this.accessLevel = accessLevel;
    }

    public String getAccessLevel() { return accessLevel; }

    @Override
    public String getDisplayInfo() {
        return "Admin[ID=" + getEmployeeId() + ", Name=" + getName() + ", Access=" + accessLevel + "]";
    }
}
