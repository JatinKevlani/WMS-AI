package com.wmsai.oopj;

/**
 * Employee extends Person, implements Searchable.
 * Covers T035, OOPJ-18 (inheritance), OOPJ-20 (override searchById with employee ID logic).
 */
public class Employee extends Person implements Searchable {
    private int employeeId;
    private String department;

    public Employee() {}

    public Employee(String name, String email, String phone, int employeeId, String department) {
        super(name, email, phone);
        this.employeeId = employeeId;
        this.department = department;
    }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String getDisplayInfo() {
        return "Employee[ID=" + employeeId + ", Name=" + getName() + ", Dept=" + department + "]";
    }

    /**
     * Override searchById — uses employee ID instead of Aadhaar [OOPJ-20].
     */
    @Override
    public Object searchById(int id) {
        if (this.employeeId == id) {
            return this;
        }
        return null;
    }

    @Override
    public Object searchByName(String name) {
        if (getName() != null && getName().toLowerCase().contains(name.toLowerCase())) {
            return this;
        }
        return null;
    }
}
