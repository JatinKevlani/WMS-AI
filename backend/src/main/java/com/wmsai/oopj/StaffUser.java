package com.wmsai.oopj;

/**
 * StaffUser extends Employee — represents a staff member.
 * Covers OOPJ-18 (Person → Employee → StaffUser).
 */
public class StaffUser extends Employee {
    private String shift;

    public StaffUser() {}

    public StaffUser(String name, String email, String phone, int employeeId, String shift) {
        super(name, email, phone, employeeId, "Warehouse Operations");
        this.shift = shift;
    }

    public String getShift() { return shift; }

    @Override
    public String getDisplayInfo() {
        return "Staff[ID=" + getEmployeeId() + ", Name=" + getName() + ", Shift=" + shift + "]";
    }
}
